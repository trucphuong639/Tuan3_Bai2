package com.example.tuan3_bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnBoardingApp()
        }
    }
}

@Composable
fun OnBoardingApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 1
            OnBoardingScreen(navController, index)
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("onboarding/1")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "UTH SmartTasks",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BFFF)
            )
        }
    }
}

@Composable
fun OnBoardingScreen(navController: NavHostController, index: Int) {
    val pages = listOf(
        PageData("Easy Time Management", "With management based on priority...", R.drawable.img_1),
        PageData("Increase Work Effectiveness", "Time management and priority...", R.drawable.img_2),
        PageData("Reminder Notification", "Provides reminders...", R.drawable.img_3)
    )

    val isLastPage = index == pages.size
    val isFirstPage = index == 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp)) // Điều chỉnh khoảng cách hợp lý

        // Row chứa Indicator + Skip (hiển thị Skip cả ở Onboarding 3)
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.padding(start = 16.dp)) {
                repeat(pages.size) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == index - 1) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (i == index - 1) Color.Blue else Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            // Luôn hiển thị Skip
            Text(
                text = "Skip",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { navController.navigate("home") }
            )
        }

        Spacer(modifier = Modifier.height(40.dp)) // Giảm khoảng cách

        // Image
        Image(
            painter = painterResource(id = pages[index - 1].imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp)) // Tăng khoảng cách giữa ảnh và text

        // Title
        Text(
            text = pages[index - 1].title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = pages[index - 1].description,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.85f)
        )

        Spacer(modifier = Modifier.height(400.dp)) // Tăng khoảng cách giữa text và nút Next

        // Xử lý riêng OnBoarding 1
        if (isFirstPage) {
            Button(
                onClick = { navController.navigate("onboarding/2") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
            ) {
                Text(text = "Next", color = Color.White, fontSize = 16.sp)
            }
        } else {
            Spacer(modifier = Modifier.weight(1f)) // Đẩy phần dưới lên

            // Row chứa Back & Next cho OnBoarding 2 & 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Back
                Image(
                    painter = painterResource(id = R.drawable.img_4), // Chèn ảnh của bạn
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate("onboarding/${index - 1}") }
                )

                // Nút Next
                Button(
                    onClick = {
                        if (isLastPage) {
                            navController.navigate("home")
                        } else {
                            navController.navigate("onboarding/${index + 1}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Mở rộng nút ra 80% chiều rộng màn hình
                        .height(55.dp) // Tăng chiều cao nút lên
                        .clip(RoundedCornerShape(30.dp)), // Bo góc mềm hơn
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF))
                ) {
                    Text(text = if (isLastPage) "Get Started" else "Next", color = Color.White, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Data class chứa thông tin từng trang OnBoarding
data class PageData(val title: String, val description: String, val imageRes: Int)
