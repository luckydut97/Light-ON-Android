package com.luckydut97.lighton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.luckydut97.lighton.core.ui.theme.AssistiveColor
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.theme.ThumbLineColor

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
    val listState = rememberLazyListState()

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
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = ThumbLineColor,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { expanded = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .padding(start = 20.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayText,
                        color = if (selectedItem.isNotEmpty()) Color.Black else AssistiveColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PretendardFamily,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        painter = painterResource(id = com.luckydut97.lighton.core.R.drawable.ic_arrow_below),
                        contentDescription = "드롭다운 열기",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 커스텀 드롭다운 메뉴
            if (expanded) {
                Popup(
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    val maxDropdownHeight = 329.dp
                    val itemHeight = 47.dp
                    val totalItemsHeight = items.size * 47
                    val needsScrollbar = totalItemsHeight > 329

                    Card(
                        modifier = Modifier
                            .width(maxWidth)
                            .height(
                                when {
                                    items.isEmpty() -> itemHeight
                                    totalItemsHeight > 329 -> maxDropdownHeight
                                    else -> (items.size * 47).dp
                                }
                            ),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(items) { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(itemHeight)
                                            .clickable {
                                                onItemSelected(item)
                                                expanded = false
                                            }
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = item,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = PretendardFamily,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }

                            // 커스텀 스크롤바
                            if (needsScrollbar) {
                                val scrollbarHeight by remember {
                                    derivedStateOf {
                                        if (items.isEmpty()) 0f else {
                                            val visibleItemsRatio = 329f / totalItemsHeight
                                            (329 * visibleItemsRatio).coerceAtMost(329f)
                                        }
                                    }
                                }

                                val scrollbarOffset by remember {
                                    derivedStateOf {
                                        if (listState.layoutInfo.totalItemsCount == 0) 0f else {
                                            val firstVisibleItemIndex =
                                                listState.firstVisibleItemIndex.toFloat()
                                            val firstVisibleItemScrollOffset =
                                                listState.firstVisibleItemScrollOffset.toFloat()
                                            val totalScrollableHeight = totalItemsHeight - 329f
                                            val currentScrollPosition =
                                                (firstVisibleItemIndex * 47f) + firstVisibleItemScrollOffset
                                            val scrollRatio =
                                                currentScrollPosition / totalScrollableHeight.coerceAtLeast(
                                                    1f
                                                )
                                            (scrollRatio * (329f - scrollbarHeight)).coerceAtLeast(
                                                0f
                                            )
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .width(4.dp)
                                        .fillMaxHeight()
                                        .padding(end = 2.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(scrollbarHeight.dp.coerceAtLeast(16.dp))
                                            .offset(y = scrollbarOffset.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ThumbLineColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
