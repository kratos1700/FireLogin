package com.example.firelogin.ui.singup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firelogin.data.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor(private val authService: AuthService): ViewModel(){



    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading




    fun singUp(email: String, password: String, navegateToDetail: () -> Unit) {

        viewModelScope.launch {
            _loading.value = true

           try {
               val result =   withContext(Dispatchers.IO) {
                  authService.singUp(email, password)
                }

               if (result != null) {
                   _loading.value = false
                   navegateToDetail()

               }else{
                   _loading.value = false
                   // error

               }


            } catch (e: Exception) {
                //_loading.value = false
               // mostrariamos el error
                Log.e("SingUpViewModel", "singUp: ", e)
            }



            _loading.value = false


        }


    }


}