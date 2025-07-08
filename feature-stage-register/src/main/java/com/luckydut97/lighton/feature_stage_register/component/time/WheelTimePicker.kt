package com.luckydut97.lighton.feature_stage_register.component.time

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import kotlin.math.abs
import kotlin.math.absoluteValue

@Composable
fun WheelTimePicker(
    amPm: String,
    hour: Int,
    minute: Int,
    onAmPmChanged: (String) -> Unit,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 오전/오후 휠
            WheelColumn(
                items = listOf("오전", "오후"),
                selectedIndex = if (amPm == "오전") 0 else 1,
                onSelectionChanged = { index ->
                    onAmPmChanged(if (index == 0) "오전" else "오후")
                },
                onCenterItemChanged = { index ->
                    onAmPmChanged(if (index == 0) "오전" else "오후")
                },
                modifier = Modifier.weight(1f)
            )

            // 시간 휠 (1-12시)
            WheelColumn(
                items = (1..12).map { "${it}시" },
                selectedIndex = (hour - 1).coerceIn(0, 11),
                onSelectionChanged = { index ->
                    onHourChanged((index + 1).coerceIn(1, 12))
                },
                onCenterItemChanged = { index ->
                    onHourChanged((index + 1).coerceIn(1, 12))
                },
                modifier = Modifier.weight(1f),
                isCircular = true
            )

            // 분 휠 (0-59분) - 0분 인식 개선
            WheelColumn(
                items = (0..59).map { "${String.format("%02d", it)}분" },
                selectedIndex = minute.coerceIn(0, 59),
                onSelectionChanged = { index ->
                    println("분 휠 - 최종 선택: ${index}분")
                    onMinuteChanged(index.coerceIn(0, 59))
                },
                onCenterItemChanged = { index ->
                    println("분 휠 - 중앙 아이템 변경: ${index}분")
                    onMinuteChanged(index.coerceIn(0, 59))
                },
                modifier = Modifier.weight(1f),
                isCircular = true
            )
        }

        // 중앙 선택 영역 하이라이트
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Gray.copy(alpha = 0.1f))
                .align(Alignment.Center)
        )
    }
}

@Composable
fun WheelColumn(
    items: List<String>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    onCenterItemChanged: (Int) -> Unit = {}, // 중앙 아이템 실시간 변경 콜백
    modifier: Modifier = Modifier,
    isCircular: Boolean = false
) {
    val listState = rememberLazyListState()
    val rowCount = 3 // 보이는 아이템 수

    // 페이드 효과를 위한 그라데이션
    val topBottomFade = Brush.verticalGradient(
        0f to Color.Transparent,
        0.3f to Color.Black,
        0.7f to Color.Black,
        1f to Color.Transparent
    )

    // 현재 중앙에 있는 아이템 인덱스 - 실시간 계산
    val currentCenterIndex = remember {
        derivedStateOf {
            calculateCenterItemIndex(listState, items.size, isCircular)
        }
    }.value

    // 중앙 아이템이 변경될 때마다 실시간으로 콜백 호출
    LaunchedEffect(currentCenterIndex) {
        if (currentCenterIndex >= 0 && currentCenterIndex < items.size) {
            onCenterItemChanged(currentCenterIndex)
        }
    }

    // 초기 위치 설정 - 한 번만 실행
    var isInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!isInitialized) {
            val targetIndex = if (isCircular) {
                // 순환 스크롤의 경우 중간 지점에서 시작
                val virtualCount = items.size * 500 // 더 큰 수로 증가
                val startOffset = virtualCount / 2 - (virtualCount / 2) % items.size
                startOffset + selectedIndex
            } else {
                selectedIndex.coerceIn(0, items.size - 1)
            }
            listState.scrollToItem(targetIndex)
            isInitialized = true
        }
    }

    // 외부에서 selectedIndex가 변경될 때만 스크롤 (중앙 아이템과 다를 때만)
    var lastExternalIndex by remember { mutableStateOf(selectedIndex) }
    LaunchedEffect(selectedIndex) {
        if (isInitialized && selectedIndex != lastExternalIndex &&
            selectedIndex != currentCenterIndex
        ) {

            val targetIndex = if (isCircular) {
                val virtualCount = items.size * 500
                val startOffset = virtualCount / 2 - (virtualCount / 2) % items.size
                startOffset + selectedIndex
            } else {
                selectedIndex.coerceIn(0, items.size - 1)
            }

            listState.animateScrollToItem(targetIndex)
            lastExternalIndex = selectedIndex
        }
    }

    // 스크롤 완료 후 최종 선택값 확정
    var isUserScrolling by remember { mutableStateOf(false) }
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            isUserScrolling = true
        } else if (isUserScrolling && isInitialized) {
            val finalIndex = calculateCenterItemIndex(listState, items.size, isCircular)

            if (finalIndex != selectedIndex && finalIndex in 0 until items.size) {
                lastExternalIndex = finalIndex
                onSelectionChanged(finalIndex)
            }
            isUserScrolling = false
        }
    }

    // 실제 표시할 아이템 리스트 (순환 스크롤 지원)
    val displayItems = remember(items, isCircular) {
        if (isCircular) {
            val virtualCount = items.size * 500 // 더 큰 수로 증가
            (0 until virtualCount).map { items[it % items.size] }
        } else {
            items
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxHeight()
            .fadingEdge(topBottomFade),
        flingBehavior = rememberSnapFlingBehavior(listState),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 40.dp) // 위아래 여백
    ) {
        items(displayItems.size) { index ->
            val item = displayItems[index]

            // 현재 아이템의 실제 인덱스 계산
            val actualIndex = if (isCircular) {
                index % items.size
            } else {
                index
            }

            // 현재 아이템이 중앙에 있는지 확인
            val isCenterItem = actualIndex == currentCenterIndex

            // 애니메이션 알파값 계산
            val alpha = calculateAnimatedAlpha(
                lazyListState = listState,
                index = index,
                rowCount = rowCount
            )

            Text(
                text = item,
                fontFamily = PretendardFamily,
                fontWeight = if (isCenterItem) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (isCenterItem) 20.sp else 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .alpha(if (isCenterItem) 1f else alpha) // 중앙 아이템은 완전 불투명
            )
        }
    }
}

// 개선된 중앙 아이템 인덱스 계산 함수
private fun calculateCenterItemIndex(
    lazyListState: LazyListState,
    itemCount: Int,
    isCircular: Boolean
): Int {
    val layoutInfo = lazyListState.layoutInfo
    if (layoutInfo.visibleItemsInfo.isEmpty()) return 0

    val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2

    // 중앙에 가장 가까운 아이템 찾기
    val centerItem = layoutInfo.visibleItemsInfo.minByOrNull { visibleItem ->
        val itemCenter = visibleItem.offset + visibleItem.size / 2
        kotlin.math.abs(itemCenter - viewportCenter)
    }

    return centerItem?.let { item ->
        if (isCircular) {
            item.index % itemCount
        } else {
            item.index.coerceIn(0, itemCount - 1)
        }
    } ?: 0
}

// 페이드 효과 함수
private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

// 알파 애니메이션 계산 함수
@Composable
private fun calculateAnimatedAlpha(
    lazyListState: LazyListState,
    index: Int,
    rowCount: Int
): Float {
    val layoutInfo = lazyListState.layoutInfo
    val viewportCenter = layoutInfo.viewportSize.height / 2

    val currentItem = layoutInfo.visibleItemsInfo.find { it.index == index }

    return currentItem?.let { item ->
        val itemCenter = item.offset + item.size / 2
        val distanceFromCenter = kotlin.math.abs(itemCenter - viewportCenter).toFloat()
        val maxDistance = layoutInfo.viewportSize.height / (rowCount * 2)

        when {
            distanceFromCenter <= maxDistance * 0.5f -> 1f
            distanceFromCenter <= maxDistance -> 0.6f
            else -> 0.3f
        }
    } ?: 0.3f
}