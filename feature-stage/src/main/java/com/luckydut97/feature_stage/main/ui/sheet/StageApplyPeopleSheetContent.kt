package com.luckydut97.feature_stage.main.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.viewmodel.StageApplyPeopleSheetViewModel
import com.luckydut97.lighton.feature.stage.R
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LightonCancelOutlinedButton
import com.luckydut97.lighton.core.ui.components.SmallActionButton
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.theme.ThumbLineColor

@Composable
fun StageApplyPeopleSheetContent(
    viewModel: StageApplyPeopleSheetViewModel,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val captionColor = Color(0xFF555555)
    val peopleCount = viewModel.peopleCount.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(247.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "공연 신청",
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "예매 희망하는 인원수를 알려주세요",
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = captionColor,
                    letterSpacing = (-1).sp,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .width(109.dp)
                        .height(27.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_minus),
                            contentDescription = "minus",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = { viewModel.minusPeople() }),
                            tint = ThumbLineColor
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "${peopleCount}명",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = "plus",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = { viewModel.plusPeople() }),
                            tint = ThumbLineColor
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                LightonCancelOutlinedButton(
                    text = "취소",
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                LightonButton(
                    text = "다음",
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF6137DD)
                )
            }
        }
    }
}
