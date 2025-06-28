package com.luckydut97.lighton.feature_auth.signup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.LightonBackButton
import com.luckydut97.lighton.core.ui.components.LightonButton
import com.luckydut97.lighton.core.ui.components.LightonOutlinedButton
import com.luckydut97.lighton.core.ui.theme.BrandColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.feature.auth.R
import kotlin.math.ceil

/**
 * 음악 장르 데이터 클래스
 * @param id 장르 고유 ID
 * @param name 장르 이름
 * @param imageResId 장르 이미지 리소스 ID
 */
data class MusicGenre(
    val id: String,
    val name: String,
    val imageResId: Int
)

/**
 * 음악 취향 선택 화면
 * @param onSkipClick 건너뛰기 버튼 클릭 시 호출될 콜백
 * @param onNextClick 다음 버튼 클릭 시 호출될 콜백 (선택된 장르 목록 전달)
 */
@Composable
fun MusicPreferenceScreen(
    onBackClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onNextClick: (List<String>) -> Unit = {}
) {
    // 모든 음악 장르 데이터
    // 실제 앱에서는 ViewModel에서 가져오거나 데이터 소스에서 로드할 수 있음
    val allGenres = remember {
        listOf(
            MusicGenre("pop", "팝", R.drawable.ic_pop),
            MusicGenre("rock", "록", R.drawable.ic_rock),
            MusicGenre("hiphop", "힙합", R.drawable.ic_hiphop),
            MusicGenre("rnb", "R&B", R.drawable.ic_rnb),
            MusicGenre("edm", "EDM", R.drawable.ic_edm),
            MusicGenre("ballad", "발라드", R.drawable.ic_ballad),
            MusicGenre("jazz", "재즈", R.drawable.ic_jazz),
            MusicGenre("classic", "클래식", R.drawable.ic_classic),
            MusicGenre("reggae", "레게", R.drawable.ic_reggae),
            MusicGenre("country", "컨트리", R.drawable.ic_country),
            MusicGenre("acoustic", "어쿠스틱", R.drawable.ic_acoustic),
            MusicGenre("alternative", "얼터너티브", R.drawable.ic_alternative),
            MusicGenre("etc", "기타", R.drawable.ic_etc)
        )
    }

    // 선택된 장르 ID 목록
    val selectedGenreIds = remember { mutableStateListOf<String>() }

    LightonTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 상단 닫기 버튼
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, end = 16.dp)
                    ) {

                        // 닫기 아이콘 버튼
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_btn),
                            contentDescription = "닫기",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(24.dp)
                                .clickable { onSkipClick() },
                            tint = Color.Black
                        )
                    }

                    // 메인 콘텐츠
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        // 제목
                        Text(
                            text = "음악 취향을 알려주세요!",
                            fontFamily = PretendardFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 부제목
                        Text(
                            text = "좋아하는 음악 장르를 알려주시면\n취향에 맞는 공연을 알려드려요",
                            fontFamily = PretendardFamily,
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // 장르 그리드 (3열 그리드)
                        MusicGenreGrid(
                            genres = allGenres,
                            selectedGenreIds = selectedGenreIds,
                            onGenreClicked = { genreId ->
                                if (selectedGenreIds.contains(genreId)) {
                                    selectedGenreIds.remove(genreId)
                                } else {
                                    selectedGenreIds.add(genreId)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 하단 버튼 영역
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // 건너뛰기 버튼
                        LightonOutlinedButton(
                            text = "건너뛰기",
                            onClick = { onSkipClick() },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.48f)
                        )

                        // 다음 버튼
                        LightonButton(
                            text = "다음",
                            onClick = { onNextClick(selectedGenreIds.toList()) },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxWidth(0.48f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 음악 장르 그리드 컴포넌트
 * 3열 N행으로 장르를 표시합니다.
 */
@Composable
fun MusicGenreGrid(
    genres: List<MusicGenre>,
    selectedGenreIds: List<String>,
    onGenreClicked: (String) -> Unit
) {
    // 화면 너비에 따라 열의 개수 결정 (여기서는 고정 3열)
    val columns = 3
    // 행의 개수 계산
    val rows = ceil(genres.size / columns.toFloat()).toInt()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < genres.size) {
                        val genre = genres[index]
                        GenreItem(
                            genre = genre,
                            isSelected = selectedGenreIds.contains(genre.id),
                            onClick = { onGenreClicked(genre.id) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        // 빈 공간을 채우기 위한 Spacer
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/**
 * 개별 음악 장르 아이템 컴포넌트
 */
@Composable
fun GenreItem(
    genre: MusicGenre,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        // 장르 이미지와 이름
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(101.dp) // 101x101 크기
                .clip(CircleShape)
                .background(Color.LightGray) // 기본 배경색
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) BrandColor else Color.Transparent,
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
        ) {
            // 장르 이미지
            Image(
                painter = painterResource(id = genre.imageResId),
                contentDescription = genre.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 선택된 상태일 때 반투명 오버레이 및 텍스트
            if (isSelected) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x66000000)) // 반투명 검정색
                ) {
                    // 장르 이름
                    Text(
                        text = genre.name,
                        color = Color.White,
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // 선택되지 않은 상태에서 텍스트
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 장르 이름
                    Text(
                        text = genre.name,
                        color = Color.White,
                        fontFamily = PretendardFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPreferenceScreenPreview() {
    LightonTheme {
        MusicPreferenceScreen()
    }
}
