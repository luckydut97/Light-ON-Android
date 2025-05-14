package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.theme.TextFieldBackgroundColor
import com.luckydut97.lighton.core.ui.theme.TextFieldBorderColor

/**
 * 라이트온 앱의 드롭다운 컴포넌트
 */
@Composable
fun LightonDropdown(
    label: String,
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "선택",
    isRequired: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = if (selectedItem.isEmpty()) placeholder else selectedItem

    Column(modifier = modifier.fillMaxWidth()) {
        // 라벨 + 필수 표시(*) - 라벨이 있을 때만 표시
        if (label.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = PretendardFamily,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isRequired) {
                    Text(
                        text = " *",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontFamily = PretendardFamily,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }

        // 드롭다운 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TextFieldBackgroundColor)
                .border(
                    width = 1.dp,
                    color = TextFieldBorderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    color = if (selectedItem.isEmpty()) Color.Gray else Color.Black,
                    fontSize = 16.sp,
                    fontFamily = PretendardFamily,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "드롭다운 열기",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                fontFamily = PretendardFamily
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
