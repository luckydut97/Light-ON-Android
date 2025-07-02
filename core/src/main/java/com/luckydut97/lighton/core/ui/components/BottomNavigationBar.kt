package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.R

enum class NavigationItem(
    val title: String,
    val activeIcon: Int,
    val inactiveIcon: Int
) {
    HOME("홈", R.drawable.ic_home_on, R.drawable.ic_home_off),
    STAGE("공연", R.drawable.ic_stage_on, R.drawable.ic_stage_off),
    MAP("지도", R.drawable.ic_map_on, R.drawable.ic_map_off),
    MYPAGE("마이페이지", R.drawable.ic_mypage_on, R.drawable.ic_mypage_off)
}

@Composable
fun BottomNavigationBar(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(72.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
            )
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 17.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationItem.values().forEach { item ->
                val isSelected = item == selectedItem
                val iconRes = if (isSelected) item.activeIcon else item.inactiveIcon
                val textColor = if (isSelected) BrandColor else AssistiveColor

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onItemSelected(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .height(45.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = item.title,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
