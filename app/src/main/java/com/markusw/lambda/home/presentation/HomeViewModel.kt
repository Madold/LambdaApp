package com.markusw.lambda.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.core.domain.VideoClient
import com.markusw.lambda.core.domain.model.Donation
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.repository.UsersRepository
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.domain.remote.RemoteStorage
import com.markusw.lambda.home.domain.repository.DonationRepository
import com.markusw.lambda.home.domain.repository.MentoringRepository
import com.markusw.lambda.home.domain.repository.PaymentRepository
import com.markusw.lambda.home.domain.use_cases.ValidateCoverUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    usersRepository: UsersRepository,
    private val mentoringRepository: MentoringRepository,
    private val authService: AuthService,
    private val videoClient: VideoClient,
    private val remoteStorage: RemoteStorage,
    private val donationRepository: DonationRepository,
    private val validateCoverUri: ValidateCoverUri,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _users = usersRepository.getUsers()
    private val _tutorials = mentoringRepository.getTutoringSessions()
    val state = combine(_state, _users, _tutorials) { state, users, tutorials ->
        state.copy(
            users = users,
            tutorials = tutorials
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        HomeState()
    )

    init {
        _state.update {
            it.copy(
                loggedUser = authService.getLoggedUser() ?: User()
            )
        }
    }

    private val channel = Channel<HomeViewModelEvent>()
    val events = channel.receiveAsFlow()

    companion object {
        const val TAG = "HomeViewModel"
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeRequestTutoringDialogVisibility -> {
                _state.update {
                    it.copy(isRequestTutoringDialogVisible = event.isVisible)
                }
            }

            is HomeEvent.ChangeDonationAmount -> {
                _state.update {
                    it.copy(
                        donationAmount = event.amount
                    )
                }
            }

            is HomeEvent.ChangeMentoringCoverUrl -> {
                _state.update {
                    it.copy(
                        mentoringCoverUri = event.coverUrl
                    )
                }
            }

            is HomeEvent.ChangeMentoringDescription -> {
                _state.update {
                    it.copy(
                        mentoringDescription = event.description
                    )
                }
            }

            is HomeEvent.ChangeMentoringRequesterDescription -> {
                _state.update {
                    it.copy(
                        mentoringRequesterDescription = event.description
                    )
                }
            }

            is HomeEvent.ChangeMentoringPrice -> {
                _state.update {
                    it.copy(
                        mentoringPrice = event.price
                    )
                }
            }

            is HomeEvent.ChangeMentoringTitle -> {
                _state.update {
                    it.copy(
                        mentoringTitle = event.title
                    )
                }
            }

            is HomeEvent.CreateMentoringRequest -> {
                _state.update {
                    it.copy(
                        isSavingMentoring = true
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d(TAG, state.value.mentoringRequesterDescription)
                    when (val result = mentoringRepository.saveMentoring(
                        Mentoring(
                            title = state.value.mentoringTitle,
                            requesterDescription = state.value.mentoringRequesterDescription,
                            requester = authService.getLoggedUser() ?: User()
                        )
                    )) {
                        is Result.Error -> {

                        }

                        is Result.Success -> {

                        }
                    }
                    _state.update {
                        it.copy(
                            isSavingMentoring = false,
                            isRequestTutoringDialogVisible = false
                        )
                    }
                }
            }

            is HomeEvent.ChangeSelectedMentoring -> {
                _state.update {
                    it.copy(
                        selectedMentoring = event.mentoring
                    )
                }
            }

            is HomeEvent.ChangeProvideMentoringDialogVisibility -> {
                _state.update {
                    it.copy(
                        isProvideMentoringDialogVisible = event.isVisible
                    )
                }
            }

            is HomeEvent.BringLiveMentoring -> {

                val coverUriValidationResult = validateCoverUri(state.value.mentoringCoverUri)

                val isAnyError = listOf(coverUriValidationResult)
                    .any { !it.success }

                if (isAnyError) {
                    _state.update {
                        it.copy(
                            coverUriError = coverUriValidationResult.errorMessage
                        )
                    }
                    return
                }

                _state.update {
                    it.copy(
                        isStartingLiveMentoring = true,
                        coverUriError = null
                    )
                }


                viewModelScope.launch(Dispatchers.IO) {
                    val coverUrl = remoteStorage.uploadImage(state.value.mentoringCoverUri)
                    val loggedUser = authService.getLoggedUser() ?: return@launch

                    val updatedMentoring = state.value.selectedMentoring?.copy(
                        coverUrl = coverUrl,
                        description = state.value.mentoringDescription,
                        price = state.value.mentoringPrice,
                        author = loggedUser
                    ) ?: return@launch

                    mentoringRepository.updateMentoring(updatedMentoring)

                    videoClient.initVideoClient(
                        username = loggedUser.displayName,
                        userId = loggedUser.id,
                        photoUrl = loggedUser.photoUrl
                    )

                    channel.send(HomeViewModelEvent.VideoClientInitialized(updatedMentoring.roomId))

                    _state.update {
                        it.copy(
                            isProvideMentoringDialogVisible = false,
                            mentoringDescription = "",
                            mentoringPrice = 0,
                            mentoringTitle = "",
                            mentoringRequesterDescription = "",
                            mentoringCoverUri = "",
                            selectedMentoring = null,
                            isStartingLiveMentoring = false
                        )
                    }
                }
            }

            is HomeEvent.DonateToMentoring -> {
                _state.update {
                    it.copy(
                        isDonating = true,
                        donationState = DonationState.InProgress
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    val result = donationRepository.donate(
                        Donation(
                            author = state.value.loggedUser,
                            mentoring = event.mentoring,
                            amount = event.amount
                        )
                    )

                    when (result) {
                        is Result.Error -> {

                        }

                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    donationState = DonationState.DonationSuccess
                                )
                            }

                            delay(2500)
                            _state.update {
                                it.copy(
                                    donationState = DonationState.InProgress,
                                    isDonating = false
                                )
                            }
                        }
                    }
                }
            }

            is HomeEvent.PayMentoringAccess -> {
                _state.update {
                    it.copy(
                        isPaymentProcessing = true
                    )
                }

                viewModelScope.launch {
                    val loggedUser = authService.getLoggedUser() ?: return@launch
                    val result = paymentRepository.payMentoringAccess(event.mentoring, loggedUser)

                    when (result) {
                        is Result.Error -> {

                        }
                        is Result.Success -> {
                            _state.update {
                                it.copy(
                                    paymentState = PaymentState.Success
                                )
                            }

                            delay(2500)

                            _state.update {
                                it.copy(
                                    isPaymentProcessing = false,
                                    paymentState = PaymentState.InProcess,
                                    isJoiningLiveMentoring = true
                                )
                            }

                            videoClient.initVideoClient(
                                userId = loggedUser.id,
                                username = loggedUser.displayName,
                                photoUrl = loggedUser.photoUrl
                            )
                            channel.send(HomeViewModelEvent.VideoClientInitialized(roomId = event.mentoring.roomId))
                            _state.update {
                                it.copy(
                                    isJoiningLiveMentoring = false
                                )
                            }
                        }
                    }

                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        channel.close()
    }

}