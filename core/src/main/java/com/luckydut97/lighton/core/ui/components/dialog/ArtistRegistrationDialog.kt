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
import com.luckydut97.lighton.core.ui.components.dialog.DialogButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.InfoTextColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun ArtistRegistrationDialog(
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
                    text = "아티스트 회원이 아닙니다",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                // 본문
                Text(
                    text = "일반 공연은 아티스트 회원만\n등록할 수 있습니다.\n아티스트 회원 신청을 진행하시겠습니까?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFamily,
                    color = CaptionColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 안내 문구
                Text(
                    text = "심사는 영업일 기준 1~2일 소요 예정",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = PretendardFamily,
                    color = InfoTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 취소 버튼
                    DialogButton(
                        text = "취소",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        backgroundColor = Color.White,
                        textColor = Color.Black,
                        borderColor = Color(0xFFCECECE),
                        borderWidth = 1.dp,
                        fontWeight = FontWeight.Normal
                    )

                    // 확인 버튼
                    DialogButton(
                        text = "확인",
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        backgroundColor = BrandColor,
                        textColor = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}