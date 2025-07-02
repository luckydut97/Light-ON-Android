package com.luckydut97.feature_stage.main.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.viewmodel.StageApplyAccountSheetViewModel
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.SmallActionButton
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import androidx.compose.ui.text.SpanStyle

@Composable
fun StageApplyAccountSheetContent(
    viewModel: StageApplyAccountSheetViewModel,
    peopleCount: Int,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val captionColor = Color(0xFF555555)
    val backgroundGray = Color(0xFFF5F5F5)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(412.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "공연 신청",
                    style = TextStyle(
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp,
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .background(backgroundGray, RoundedCornerShape(6.dp)),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "• ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                            Text(
                                "예금주 : ${viewModel.accountHolder.value}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "• ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                            Text(
                                "계좌번호 : ${viewModel.accountNumber.value}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "• ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                            Text(
                                "공연 비용 : ",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                            Text(
                                "${viewModel.applyCost.value}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "• ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                            Text(
                                "신청 인원 : ${peopleCount}명",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = captionColor,
                                    fontFamily = PretendardFamily
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "* 반드시 신청자 이름으로 입금 부탁드립니다.",
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = captionColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "* 영업일 기준 1~2일정도 소요됩니다.",
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = captionColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val accountDesc = buildAnnotatedString {
                        append("* 입금 계좌 정보는 ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("[마이페이지]>[신청 내역]")
                        }
                        append("에서 확인하실 수 있습니다.")
                    }
                    Text(
                        text = accountDesc,
                        style = TextStyle(
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = captionColor
                        )
                    )
                }
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
                    text = "확인",
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    backgroundColor = Color(0xFF6137DD)
                )
            }
        }
    }
}
