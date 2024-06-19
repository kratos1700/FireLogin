package com.example.firelogin.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firelogin.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    fun logout(navigateToLogin: () -> Unit) {
        //   viewModelScope.launch(Dispatchers.IO) {

        //   authService.logout()

        // }
        //   navigateToLogin()

        viewModelScope.launch {
            authService.logout()
            withContext(Dispatchers.Main) {
                navigateToLogin()
            }
        }

    }

}