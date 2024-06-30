package com.markusw.lambda.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.lambda.core.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    usersRepository: UsersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    private val _users = usersRepository.getUsers()
    val state = combine(_state, _users) { state, users ->
        state.copy(
            users = users
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        HomeState()
    )



}