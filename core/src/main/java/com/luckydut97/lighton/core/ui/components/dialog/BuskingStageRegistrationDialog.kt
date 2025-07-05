package com.luckydut97.lighton.core.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun BuskingStageRegistrationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                Text(
                    text = "공연을 등록하시겠습니까?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 본문
                Text(
                    text = "버스킹 공연은 반드시\n허가된 장소에서만 진행하셔야 합니다.\n허가되지 않은 장소에서의 공연으로\n발생하는 모든 책임은 주최자에게 있습니다.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFamily,
                    color = CaptionColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 취소 버튼
                    LightonButton(
                        text = "취소",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        borderColor = Color(0xFFCECECE),
                        borderWidth = 1.dp
                    )

                    // 등록 버튼
                    LightonButton(
                        text = "등록",
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        backgroundColor = BrandColor,
                        contentColor = Color.White
                    )
                }
            }
        }
    }
}