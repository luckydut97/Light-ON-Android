package com.luckydut97.feature_stage.main.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.viewmodel.StageApplyInfoSheetViewModel
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.SmallActionButton
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun StageApplyInfoSheetContent(
    viewModel: StageApplyInfoSheetViewModel,
    onNext: () -> Unit,
    onCancel: () -> Unit
) {
    val captionColor = Color(0xFF555555)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(283.dp)
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
                    text = "본 공연은 유료 공연입니다.",
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = captionColor,
                    letterSpacing = (-1).sp,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "안내드리는 계좌로 입금 확인 후\n신청이 완료 됩니다.",
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = captionColor,
                    letterSpacing = (-1).sp,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                SmallActionButton(
                    text = "취소",
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color.White,
                    contentColor = captionColor,
                    borderColor = Color(0xFF6137DD),
                    textColor = Color(0xFF6137DD),
                    fontWeight = FontWeight.Bold
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
