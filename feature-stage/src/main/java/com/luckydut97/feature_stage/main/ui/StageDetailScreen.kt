package com.luckydut97.feature_stage.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.feature_stage.model.ArtistInfo
import com.luckydut97.feature_stage.model.PerformanceDetail
import com.luckydut97.feature_stage.main.ui.sheet.StageApplyInfoSheetContent
import com.luckydut97.feature_stage.main.ui.sheet.StageApplyPeopleSheetContent
import com.luckydut97.feature_stage.main.ui.sheet.StageApplyAccountSheetContent
import com.luckydut97.feature_stage.viewmodel.StageApplyInfoSheetViewModel
import com.luckydut97.feature_stage.viewmodel.StageApplyPeopleSheetViewModel
import com.luckydut97.feature_stage.viewmodel.StageApplyAccountSheetViewModel
import com.luckydut97.lighton.core.ui.components.CommonTopBar
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.stage.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.SmallActionButton
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageDetailScreen(
    performanceId: String,
    onBackClick: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    // 더미 데이터
    val performanceDetail = remember {
        PerformanceDetail(
            id = performanceId,
            artistName = "Light ON",
            genre = "어쿠스틱",
            title = "여의도 Light ON 홀리데이 버스킹",
            date = "2025.05.01",
            time = "17:00",
            location = "여의도동",
            address = "서울 영등포구 여의도동 81-8",
            price = 10000,
            isPaid = true,
            description = "압도적인 라이브 실력과 폭발적인 히트곡 퍼레이드로 '무대장인'이라 호평받은 신인 인디밴드 단독 콘서트\n\n네번째 미니앨범 [HOLIDAY]를 기반으로 사랑받은 곡들만 뽑아 공연을 진행합니다.",
            artistInfo = ArtistInfo(
                name = "Light ON (라이트 온)",
                description = "라이트 온은 홍익대학교 동아리 출신으로 이루어진 2022년 대뷔한 신입 밴드로 '일탈'이라는 곡을 통해 많은 팬덤을 보유한 4인조 밴드 그룹입니다."
            ),
            seatTypes = listOf("스탠딩석", "자율좌석", "지정좌석"),
            entryNotes = listOf(
                "슬리퍼, 운동복, 등산복 입장 불가",
                "애완동물 입장 불가",
                "외부 음식 반입 금지"
            ),
            imageUrl = "ic_test_img",
            isLiked = false
        )
    }

    val brand = Color(0xFF6137DD)
    val caption = Color(0xFF555555)
    val assistive = Color(0xFFC4C4C4)
    val backgroundGray = Color(0xFFF5F5F5)
    val tagBackground = Color(0xFFEEE7FB)

    val sheetStepList = listOf("INFO", "PEOPLE", "ACCOUNT")
    var currentStep by remember { mutableStateOf("INFO") }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val infoViewModel = remember { StageApplyInfoSheetViewModel() }
    val peopleViewModel = remember { StageApplyPeopleSheetViewModel() }
    val accountViewModel = remember { StageApplyAccountSheetViewModel() }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val peopleCount by peopleViewModel.peopleCount.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // TopBar
            CommonTopBar(
                title = "아티스트 공연 정보",
                onBackClick = onBackClick
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // 메인 이미지
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_test_img),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // 상단 그라디언트
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(57.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 공연 세부 타이틀 컴포넌트
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    // 태그 및 공유 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .background(tagBackground, RoundedCornerShape(4.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = performanceDetail.genre,
                                color = brand,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = PretendardFamily
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_share_caption),
                            contentDescription = "Share",
                            modifier = Modifier
                                .size(width = 17.dp, height = 18.63.dp)
                                .clickable { /* 공유 기능 */ },
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 공연 기본 정보
                    Text(
                        text = performanceDetail.title,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = PretendardFamily
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${performanceDetail.date}  |  ${performanceDetail.time}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = caption,
                        fontFamily = PretendardFamily,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location_caption),
                            contentDescription = null,
                            modifier = Modifier.size(width = 15.dp, height = 16.dp),
                            tint = caption
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = performanceDetail.address,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = caption,
                            fontFamily = PretendardFamily,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "지도보기",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = assistive,
                            fontFamily = PretendardFamily,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { /* 지도 보기 */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_card_caption),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = caption
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (performanceDetail.isPaid) "(유료) ${performanceDetail.price}원" else "무료",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = caption,
                            fontFamily = PretendardFamily,
                            letterSpacing = (-1).sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 공연 소개 섹션
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 18.dp)
                        .background(backgroundGray, RoundedCornerShape(6.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_music_brand),
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = brand
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "공연 소개",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = brand,
                                fontFamily = PretendardFamily
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = performanceDetail.description,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            fontFamily = PretendardFamily,
                            letterSpacing = (-1).sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = backgroundGray
                )
                Spacer(modifier = Modifier.height(40.dp))

                // 아티스트 정보 섹션
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "아티스트 정보",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = PretendardFamily
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                        Text(
                            text = "아티스트 명",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = caption,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.width(90.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = performanceDetail.artistInfo.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = brand,
                            fontFamily = PretendardFamily,
                            textDecoration = TextDecoration.Underline,
                            letterSpacing = (-1).sp,
                            modifier = Modifier.clickable { /* 아티스트 정보 */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "아티스트 소개",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = caption,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.width(90.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = performanceDetail.artistInfo.description,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            fontFamily = PretendardFamily,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(backgroundGray)
                )
                Spacer(modifier = Modifier.height(40.dp))

                // 좌석 정보
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "좌석 정보",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = PretendardFamily
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    performanceDetail.seatTypes.forEach { seat ->
                        Row {
                            Text(
                                text = "• ",
                                fontSize = 14.sp,
                                color = caption
                            )
                            Text(
                                text = seat,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = caption,
                                fontFamily = PretendardFamily
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 입장 시 유의사항
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = "입장 시 유의사항",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = PretendardFamily
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    performanceDetail.entryNotes.forEach { note ->
                        Row {
                            Text(
                                text = "• ",
                                fontSize = 14.sp,
                                color = caption
                            )
                            Text(
                                text = note,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = caption,
                                fontFamily = PretendardFamily
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                // 하단 여백 (버튼 영역 공간 확보)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // 하단 버튼 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                thickness = 1.dp,
                color = backgroundGray
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = backgroundGray,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFDEDEDE),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { isLiked = !isLiked },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isLiked) R.drawable.ic_heart_full_caption else R.drawable.ic_heart_empty_caption
                        ),
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) brand else Color.Gray,
                        modifier = Modifier.size(width = 29.dp, height = 28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 신청하기 버튼 → 하단 시트 열기
                LightonButton(
                    text = "신청하기",
                    onClick = {
                        currentStep = "INFO"
                        isBottomSheetVisible = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                )
            }
        }
        // 신규: BottomSheetDialog
        if (isBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { isBottomSheetVisible = false },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .then(
                            if (currentStep == "ACCOUNT") Modifier.height(412.dp)
                            else Modifier.height(283.dp)
                        )
                ) {
                    when (currentStep) {
                        "INFO" -> {
                            StageApplyInfoSheetContent(
                                viewModel = infoViewModel,
                                onNext = { currentStep = "PEOPLE" },
                                onCancel = { isBottomSheetVisible = false }
                            )
                        }
                        "PEOPLE" -> {
                            StageApplyPeopleSheetContent(
                                viewModel = peopleViewModel,
                                onNext = { currentStep = "ACCOUNT" },
                                onCancel = { isBottomSheetVisible = false }
                            )
                        }
                        "ACCOUNT" -> {
                            StageApplyAccountSheetContent(
                                viewModel = accountViewModel,
                                onConfirm = { isBottomSheetVisible = false },
                                onCancel = { isBottomSheetVisible = false },
                                peopleCount = peopleCount
                            )
                        }
                    }
                }
            }
        }
    }
}
