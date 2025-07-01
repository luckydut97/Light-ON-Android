package com.luckydut97.feature_home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.home.R
import com.luckydut97.lighton.feature_home.main.viewmodel.PopularPerformance

@Composable
fun PopularPerformanceSection(
    performances: List<PopularPerformance>,
    onMoreClick: () -> Unit = {},
    onPerformanceClick: (PopularPerformance) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val itemWidth = 278.dp
    val itemHeight = 230.17.dp
    val imageHeight = 158.17.dp
    val horizontalPadding = 16.dp
    val itemSpacing = 12.dp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Title Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(37.dp)
                .clickable { onMoreClick() }
        ) {
            Text(
                text = "현재 인기 있는 공연",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = horizontalPadding)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "더보기",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = horizontalPadding)
                    .size(15.dp),
                tint = AssistiveColor
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing),
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) {
            items(performances) { perf ->
                Column(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(itemHeight)
                        .background(Color.White)
                        .clickable { onPerformanceClick(perf) },
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = perf.imageResId),
                        contentDescription = perf.title,
                        modifier = Modifier
                            .width(itemWidth)
                            .height(imageHeight),
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = perf.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontFamily = PretendardFamily,
                        letterSpacing = (-1).sp
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(-6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock_caption),
                                contentDescription = "clock",
                                modifier = Modifier.size(10.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = perf.date,
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.alignByBaseline()
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(12.dp)
                                    .background(AssistiveColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = perf.time,
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_location_caption),
                                contentDescription = "location",
                                modifier = Modifier.size(12.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = perf.place,
                                fontSize = 12.sp,
                                color = CaptionColor,
                                fontFamily = PretendardFamily,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                    }
                }
            }
        }
    }
}
