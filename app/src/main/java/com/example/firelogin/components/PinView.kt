package com.example.firelogin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PinView(
    modifier: Modifier = Modifier,
    pinLength: Int = 6,
    onPinEntered: (String) -> Unit // retorna el pin ingresat per l'usuari
) {
    var pin by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current   // Per mostrar el teclat quan s'obre la vista

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pinLength) { index ->
                PinItem(
                    value = if (index < pin.length) pin[index].toString() else "",
                    isFocused = index == pin.length,
                    onClick = { }
                )
            }
        }

        BasicTextField(
            value = pin,
            onValueChange = {
                if (it.length <= pinLength) {
                    pin = it
                    if (it.length == pinLength) {
                        onPinEntered(it)
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = VisualTransformation.None,
            decorationBox = { innerTextField -> Box { innerTextField() } },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                }
                .matchParentSize()
                .background(Color.Transparent)
                .padding(16.dp)
                .alpha(0f)  // Amaga el text
        )
    }

    // Automàticament demanar el focus quan es mostra la vista
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun PinItem(
    value: String,
    isFocused: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) MaterialTheme.colorScheme.primaryContainer else Color.LightGray
        ),
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = value, fontSize = 24.sp)
        }
    }
}