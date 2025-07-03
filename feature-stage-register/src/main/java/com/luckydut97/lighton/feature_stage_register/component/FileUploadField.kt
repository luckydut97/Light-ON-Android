package com.luckydut97.lighton.feature_stage_register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.*

@Composable
fun FileUploadField(
    fileName: String,
    placeholder: String,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 파일명 표시 필드 (입력 불가능)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(47.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = ThumbLineColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (fileName.isEmpty()) placeholder else fileName,
                color = if (fileName.isEmpty()) AssistiveColor else Color.Black,
                fontFamily = PretendardFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }

        // 오른쪽 파일 업로드 버튼 (109*47)
        Button(
            onClick = onUploadClick,
            modifier = Modifier
                .width(109.dp)
                .height(47.dp)
                .border(
                    width = 1.dp,
                    color = BrandColor,
                    shape = RoundedCornerShape(6.dp)
                ),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = BrandColor
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "파일 업로드",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PretendardFamily,
                textAlign = TextAlign.Center,
                maxLines = 1,
                color = BrandColor
            )
        }
    }
}