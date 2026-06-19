package com.example.canteenlyapp.ui.screen.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.ui.navigation.Screen
import com.example.canteenlyapp.ui.theme.Black
import com.example.canteenlyapp.ui.theme.GrayText
import com.example.canteenlyapp.ui.theme.OrangePrimary
import com.example.canteenlyapp.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController
) {

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }
    val repository = remember {
        AuthRepository()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(OrangePrimary),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Sign Up",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create your new account",
                color = White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // FORM
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp
                    )
                )
                .background(Black)
                .padding(24.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // FULL NAME

            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                },
                label = {
                    Text("FullName")
                },

                placeholder = {
                    Text("John Doe")
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedLabelColor = Color(0xFFFF9800),
                    unfocusedLabelColor = Color.Gray,

                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color(0xFFFF9800)
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // EMAIL

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text("Email")
                },

                placeholder = {
                    Text("example@gmail.com")
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedLabelColor = Color(0xFFFF9800),
                    unfocusedLabelColor = Color.Gray,

                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color(0xFFFF9800)
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text("Password")
                },

                placeholder = {
                    Text("********")
                },

                modifier = Modifier.fillMaxWidth(),

                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,

                            contentDescription = null
                        )
                    }
                },

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedLabelColor = Color(0xFFFF9800),
                    unfocusedLabelColor = Color.Gray,

                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color(0xFFFF9800)
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // CONFIRM PASSWORD
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                label = {
                    Text("Confirm Password")
                },

                placeholder = {
                    Text("********")
                },

                modifier = Modifier.fillMaxWidth(),

                visualTransformation =
                    if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(
                        onClick = {
                            confirmPasswordVisible =
                                !confirmPasswordVisible
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (confirmPasswordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,

                            contentDescription = null
                        )
                    }
                },

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedLabelColor = Color(0xFFFF9800),
                    unfocusedLabelColor = Color.Gray,

                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color(0xFFFF9800)
                ),
            )

            Spacer(modifier = Modifier.height(28.dp))

            // SIGN UP BUTTON
            Button(
                onClick = {
                    if (isLoading) return@Button

                    isLoading = true

                    scope.launch {

                        val result = repository.register(
                            fullName = fullName,
                            email = email,
                            password = password
                        )

                        result
                            .onSuccess {

                                Toast.makeText(
                                    context,
                                    "Register Success",
                                    Toast.LENGTH_SHORT
                                ).show()

                                isLoading = false

                                navController.navigate(
                                    Screen.Login.route
                                ) {
                                    popUpTo(Screen.Register.route) {
                                        inclusive = true
                                    }
                                }
                            }

                            .onFailure {

                                Toast.makeText(
                                    context,
                                    it.message ?: "Register Failed",
                                    Toast.LENGTH_LONG
                                ).show()

                                isLoading = false
                            }
                    }
                },

                enabled = !isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangePrimary
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = if (isLoading)
                            "Signing Up..."
                        else
                            "Sign Up",
                        color = White
                    )

                    if (isLoading) {

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LOGIN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Already have an account? ",
                    color = GrayText
                )

                Text(
                    text = "LOGIN",
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {

                        navController.navigate(
                            Screen.Login.route
                        ) {

                            popUpTo(
                                Screen.Register.route
                            ) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Or",
                color = GrayText,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.google
                    ),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Image(
                    painter = painterResource(
                        id = R.drawable.twitter
                    ),
                    contentDescription = "Twitter",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}