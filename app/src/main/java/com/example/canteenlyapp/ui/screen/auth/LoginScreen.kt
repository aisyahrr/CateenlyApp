package com.example.canteenlyapp.ui.screen.auth

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun LoginScreen(
    navController: NavController
) {
//fun LoginScreenPreview() {
    var email by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }
    var passwordVisible by remember{
        mutableStateOf(false)
    }
    var rememberMe by remember{
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
    ){
        // Orange Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(OrangePrimary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Log In",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please sign in to your existing account",
                color = White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
        }
            // FORM Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp
                    )
                )
                .background(Black)
                .padding(24.dp)
        ){
            Spacer(modifier = Modifier.height(30.dp))

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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedLabelColor = Color(0xFFFF9800),
                    unfocusedLabelColor = Color.Gray,

                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color.Gray,

                    cursorColor = Color(0xFFFF9800)
                ),
                shape = RoundedCornerShape(12.dp)
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

                shape = RoundedCornerShape(12.dp),

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

                            contentDescription = "Password"
                        )
                    }
                },

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

            Spacer(modifier = Modifier.height(12.dp))
            // REMEMBER & FORGOT
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = {
                            rememberMe = it
                        }
                    )

                    Text(
                        text = "Remember me",
                        color = GrayText
                    )
                }

                Text(
                    text = "Forgot Password",
                    color = OrangePrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LOGIN BUTTON
            Button(
                onClick = {

                    if (email.isBlank() || password.isBlank()) {

                        Toast.makeText(
                            context,
                            "Email dan Password wajib diisi",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    scope.launch {

                        val result = repository.login(
                            email = email,
                            password = password
                        )

                        result
                            .onSuccess {

                                isLoading = false

                                val user =
                                    repository.getCurrentUser()

                                Toast.makeText(
                                    context,
                                    "Login Success",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val destination =
                                    if (user?.role == "merchant")
                                        Screen.Merchant
                                            .route
                                    else
                                        Screen.Main.route

                                navController.navigate(
                                    destination
                                ) {

                                    popUpTo(
                                        Screen.Login.route
                                    ) {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }

                            .onFailure {

                                isLoading = false

                                Toast.makeText(
                                    context,
                                    "Email atau Password salah",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangePrimary
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            if (isLoading)
                                "Logging In..."
                            else
                                "Log In",

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

            // SIGN UP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    text = "Don't have an account? ",
                    color = GrayText
                )

                Text(
                    text = "SIGN UP",
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Register.route) {
                            popUpTo(Screen.Onboarding.route) {
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // SOCIAL LOGIN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Image(
                    painter = painterResource(id = R.drawable.twitter),
                    contentDescription = "Twitter",
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }

}