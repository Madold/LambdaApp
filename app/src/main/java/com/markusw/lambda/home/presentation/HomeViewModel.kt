package com.markusw.lambda.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.auth.domain.AuthService
import com.markusw.lambda.core.domain.model.Mentoring
import com.markusw.lambda.core.domain.model.User
import com.markusw.lambda.core.domain.repository.UsersRepository
import com.markusw.lambda.core.utils.Result
import com.markusw.lambda.home.domain.repository.MentoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    usersRepository: UsersRepository,
    private val mentoringRepository: MentoringRepository,
    private val authService: AuthService
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
                        mentoringCoverUrl = event.coverUrl
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

            is HomeEvent.StartLiveMentoring -> {

            }
        }
    }


}