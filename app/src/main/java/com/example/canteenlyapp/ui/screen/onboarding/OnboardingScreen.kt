package com.example.canteenlyapp.ui.screen.onboarding

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canteenlyapp.ui.navigation.Screen
import com.example.canteenlyapp.ui.theme.Black
import com.example.canteenlyapp.ui.theme.GrayLight
import com.example.canteenlyapp.ui.theme.GrayText
import com.example.canteenlyapp.ui.theme.OrangePrimary
import com.example.canteenlyapp.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController
) {

    val pagerState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        HorizontalPager(
            state = pagerState
        ) { page ->

            val onboarding = onboardingPages[page]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = onboarding.image),
                    contentDescription = onboarding.title,
                    modifier = Modifier.size(300.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center
                ) {

                    repeat(onboardingPages.size) { index ->

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(
                                    width =
                                        if (pagerState.currentPage == index)
                                            24.dp
                                        else
                                            8.dp,

                                    height = 8.dp
                                )
                                .clip(CircleShape)
                                .background(
                                    if (pagerState.currentPage == index)
                                        OrangePrimary
                                    else
                                        GrayLight
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = onboarding.title,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = onboarding.description,
                    color = GrayText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (pagerState.currentPage != onboardingPages.lastIndex) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Skip",
                    color = GrayText,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {

                        scope.launch {

                            pagerState.animateScrollToPage(
                                onboardingPages.lastIndex
                            )
                        }
                    }
                )

                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(OrangePrimary)
                        .clickable {

                            scope.launch {

                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1
                                )
                            }
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

        } else {

            Button(
                onClick = {

                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangePrimary
                )
            ) {

                Text(
                    text = "Get Started",
                    color = White
                )
            }
        }
    }
}