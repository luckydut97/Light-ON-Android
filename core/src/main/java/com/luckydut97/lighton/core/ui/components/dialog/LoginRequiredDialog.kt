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
import com.luckydut97.lighton.core.ui.components.LightonOutlinedButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily

@Composable
fun LoginRequiredDialog(
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 제목
                Text(
                    text = "로그인 상태가 아닙니다.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PretendardFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 본문
                Text(
                    text = "로그인/회원가입 후 이용해주세요.",
                    fontSize = 16.sp,
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

                    LightonOutlinedButton(
                        text = "로그인",
                        onClick = onLoginClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    )

                    // 회원가입 버튼
                    LightonButton(
                        text = "회원가입",
                        onClick = onSignUpClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        backgroundColor = BrandColor,
                        contentColor = Color.White
                    )
                }
            }
        }
    }
}