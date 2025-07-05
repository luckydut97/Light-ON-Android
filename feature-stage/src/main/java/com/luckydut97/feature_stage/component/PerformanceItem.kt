package com.luckydut97.feature_stage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.model.Performance
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.stage.R

@Composable
fun PerformanceItem(
    performance: Performance,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val assistive = Color(0xFFC4C4C4)
    val brand = Color(0xFF6137DD)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 썸네일 이미지
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            // 실제 구현시에는 AsyncImage 사용
            val imageResId = when (performance.imageUrl) {
                "ic_test_img1" -> R.drawable.ic_test_img1
                "ic_test_img2" -> R.drawable.ic_test_img2
                "ic_test_img3" -> R.drawable.ic_test_img3
                "ic_test_img4" -> R.drawable.ic_test_img4
                else -> R.drawable.ic_test_img2 // 기본 이미지
            }

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = performance.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 오버레이 태그 (무료공연 등)
            performance.overlayTag?.let { tag ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .height(17.dp)
                        .background(
                            brand,
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 6.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tag,
                        color = Color.White,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PretendardFamily,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // 공연 정보
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            // 해시태그
            performance.tag?.let { tag ->
                Text(
                    text = "#$tag",
                    color = brand,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardFamily
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // 공연명
            Text(
                text = performance.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PretendardFamily,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            /*Spacer(modifier = Modifier.height(6.dp))*/

            // 위치와 시간
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location_assistive),
                    contentDescription = null,
                    modifier = Modifier.size(width = 9.dp, height = 11.dp),
                    tint = assistive
                )
                Text(
                    text = performance.location,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFamily,
                    color = assistive
                )
                Text(
                    text = " · ${performance.date} ${performance.time}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFamily,
                    color = assistive
                )
            }
        }
    }
}
