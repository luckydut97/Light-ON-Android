package com.luckydut97.lighton.feature_stage_register.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luckydut97.lighton.core.ui.components.*
import com.luckydut97.lighton.core.ui.theme.CaptionColor
import com.luckydut97.lighton.core.ui.theme.LightonTheme
import com.luckydut97.lighton.core.ui.theme.PretendardFamily
import com.luckydut97.lighton.core.ui.utils.FilePickerUtil
import com.luckydut97.lighton.core.ui.utils.PermissionUtil
import com.luckydut97.lighton.feature_stage_register.component.*
import com.luckydut97.lighton.core.ui.components.LightonDropdown
import com.luckydut97.lighton.core.ui.components.dialog.ArtistRegistrationConfirmDialog

@Composable
fun ArtistRegisterScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 입력 상태들
    var profileImageFileName by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var artistDescription by remember { mutableStateOf("") }
    var activityLocation by remember { mutableStateOf("") }
    var activityDistrict by remember { mutableStateOf("") }
    var activityGenre by remember { mutableStateOf("") }
    var activityHistory by remember { mutableStateOf("") }
    var activityPhotosFileName by remember { mutableStateOf("") }
    var evidenceDocumentFileName by remember { mutableStateOf("") }

    // 파일 정보 상태들
    var profileImageFileInfo by remember { mutableStateOf<FilePickerUtil.FileInfo?>(null) }
    var activityPhotosFileInfo by remember { mutableStateOf<FilePickerUtil.FileInfo?>(null) }
    var evidenceDocumentFileInfo by remember { mutableStateOf<FilePickerUtil.FileInfo?>(null) }

    // 파일 선택 후 실행할 파일 선택 타입 추적
    var pendingFileSelection by remember { mutableStateOf<String?>(null) }

    // 아티스트 등록 확인 다이얼로그 상태
    var showArtistRegistrationConfirmDialog by remember { mutableStateOf(false) }

    // 파일 선택 런처들
    val profileImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedUri ->
            val fileInfo = FilePickerUtil.getFileInfo(context, selectedUri)
            FilePickerUtil.logFileSelection(fileInfo)

            if (fileInfo.isValid) {
                profileImageFileInfo = fileInfo
                profileImageFileName = fileInfo.displayName
            } else {
                println("프로필 이미지 선택 실패: ${fileInfo.errorMessage}")
            }
        }
    }

    val activityPhotosLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedUri ->
            val fileInfo = FilePickerUtil.getFileInfo(context, selectedUri)
            FilePickerUtil.logFileSelection(fileInfo)

            if (fileInfo.isValid) {
                activityPhotosFileInfo = fileInfo
                activityPhotosFileName = fileInfo.displayName
            } else {
                println("활동 사진 선택 실패: ${fileInfo.errorMessage}")
            }
        }
    }

    val evidenceDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedUri ->
            val fileInfo = FilePickerUtil.getFileInfo(context, selectedUri)
            FilePickerUtil.logFileSelection(fileInfo)

            if (fileInfo.isValid) {
                evidenceDocumentFileInfo = fileInfo
                evidenceDocumentFileName = fileInfo.displayName
            } else {
                println("증빙자료 선택 실패: ${fileInfo.errorMessage}")
            }
        }
    }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            println("파일 접근 권한이 허용되었습니다.")
            pendingFileSelection?.let { fileType ->
                when (fileType) {
                    "profile" -> profileImageLauncher.launch("*/*")
                    "photos" -> activityPhotosLauncher.launch("*/*")
                    "evidence" -> evidenceDocumentLauncher.launch("*/*")
                }
                pendingFileSelection = null
            }
        } else {
            println("파일 접근 권한이 거부되었습니다.")
            pendingFileSelection = null
        }
    }

    // 파일 선택 핸들러
    fun handleFileSelection(fileType: String) {
        if (PermissionUtil.hasFileAccessPermission(context)) {
            when (fileType) {
                "profile" -> profileImageLauncher.launch("*/*")
                "photos" -> activityPhotosLauncher.launch("*/*")
                "evidence" -> evidenceDocumentLauncher.launch("*/*")
            }
        } else {
            pendingFileSelection = fileType
            permissionLauncher.launch(PermissionUtil.getRequiredPermissions())
        }
    }

    // 전체 지역 데이터 
    val regionData = mapOf(
        101 to ("서울특별시" to "종로구"),
        102 to ("서울특별시" to "중구"),
        103 to ("서울특별시" to "용산구"),
        104 to ("서울특별시" to "성동구"),
        105 to ("서울특별시" to "광진구"),
        106 to ("서울특별시" to "동대문구"),
        107 to ("서울특별시" to "중랑구"),
        108 to ("서울특별시" to "성북구"),
        109 to ("서울특별시" to "강북구"),
        110 to ("서울특별시" to "도봉구"),
        111 to ("서울특별시" to "노원구"),
        112 to ("서울특별시" to "은평구"),
        113 to ("서울특별시" to "서대문구"),
        114 to ("서울특별시" to "마포구"),
        115 to ("서울특별시" to "양천구"),
        116 to ("서울특별시" to "강서구"),
        117 to ("서울특별시" to "구로구"),
        118 to ("서울특별시" to "금천구"),
        119 to ("서울특별시" to "영등포구"),
        120 to ("서울특별시" to "동작구"),
        121 to ("서울특별시" to "관악구"),
        122 to ("서울특별시" to "서초구"),
        123 to ("서울특별시" to "강남구"),
        124 to ("서울특별시" to "송파구"),
        125 to ("서울특별시" to "강동구"),
        201 to ("부산광역시" to "중구"),
        202 to ("부산광역시" to "서구"),
        203 to ("부산광역시" to "동구"),
        204 to ("부산광역시" to "영도구"),
        205 to ("부산광역시" to "부산진구"),
        206 to ("부산광역시" to "동래구"),
        207 to ("부산광역시" to "남구"),
        208 to ("부산광역시" to "북구"),
        209 to ("부산광역시" to "해운대구"),
        210 to ("부산광역시" to "사하구"),
        211 to ("부산광역시" to "금정구"),
        212 to ("부산광역시" to "연제구"),
        213 to ("부산광역시" to "수영구"),
        214 to ("부산광역시" to "사상구"),
        215 to ("부산광역시" to "기장군"),
        301 to ("대구광역시" to "중구"),
        302 to ("대구광역시" to "동구"),
        303 to ("대구광역시" to "서구"),
        304 to ("대구광역시" to "남구"),
        305 to ("대구광역시" to "북구"),
        306 to ("대구광역시" to "수성구"),
        307 to ("대구광역시" to "달서구"),
        308 to ("대구광역시" to "달성군"),
        401 to ("인천광역시" to "중구"),
        402 to ("인천광역시" to "동구"),
        403 to ("인천광역시" to "서구"),
        404 to ("인천광역시" to "미추홀구"),
        405 to ("인천광역시" to "연수구"),
        406 to ("인천광역시" to "남동구"),
        407 to ("인천광역시" to "부평구"),
        408 to ("인천광역시" to "계양구"),
        410 to ("인천광역시" to "강화군"),
        411 to ("인천광역시" to "옹진군"),
        501 to ("광주광역시" to "동구"),
        502 to ("광주광역시" to "서구"),
        503 to ("광주광역시" to "남구"),
        504 to ("광주광역시" to "북구"),
        505 to ("광주광역시" to "광산구"),
        601 to ("대전광역시" to "동구"),
        602 to ("대전광역시" to "중구"),
        603 to ("대전광역시" to "서구"),
        604 to ("대전광역시" to "유성구"),
        605 to ("대전광역시" to "대덕구"),
        701 to ("울산광역시" to "중구"),
        702 to ("울산광역시" to "남구"),
        703 to ("울산광역시" to "동구"),
        704 to ("울산광역시" to "북구"),
        705 to ("울산광역시" to "울주군"),
        801 to ("세종특별자치시" to "세종시"),
        901 to ("경기도" to "수원시"),
        902 to ("경기도" to "성남시"),
        903 to ("경기도" to "고양시"),
        904 to ("경기도" to "용인시"),
        905 to ("경기도" to "부천시"),
        906 to ("경기도" to "안산시"),
        907 to ("경기도" to "안양시"),
        908 to ("경기도" to "남양주시"),
        909 to ("경기도" to "화성시"),
        910 to ("경기도" to "평택시"),
        911 to ("경기도" to "의정부시"),
        912 to ("경기도" to "시흥시"),
        913 to ("경기도" to "파주시"),
        914 to ("경기도" to "김포시"),
        915 to ("경기도" to "광명시"),
        916 to ("경기도" to "군포시"),
        917 to ("경기도" to "이천시"),
        918 to ("경기도" to "오산시"),
        919 to ("경기도" to "하남시"),
        920 to ("경기도" to "의왕시"),
        921 to ("경기도" to "양주시"),
        922 to ("경기도" to "구리시"),
        923 to ("경기도" to "안성시"),
        924 to ("경기도" to "포천시"),
        925 to ("경기도" to "광주시"),
        926 to ("경기도" to "동두천시"),
        927 to ("경기도" to "양평군"),
        928 to ("경기도" to "여주시"),
        929 to ("경기도" to "가평군"),
        930 to ("경기도" to "연천군"),
        1001 to ("강원특별자치도" to "춘천시"),
        1002 to ("강원특별자치도" to "원주시"),
        1003 to ("강원특별자치도" to "강릉시"),
        1004 to ("강원특별자치도" to "동해시"),
        1005 to ("강원특별자치도" to "태백시"),
        1006 to ("강원특별자치도" to "속초시"),
        1007 to ("강원특별자치도" to "삼척시"),
        1008 to ("강원특별자치도" to "홍천군"),
        1009 to ("강원특별자치도" to "횡성군"),
        1010 to ("강원특별자치도" to "영월군"),
        1011 to ("강원특별자치도" to "평창군"),
        1012 to ("강원특별자치도" to "정선군"),
        1013 to ("강원특별자치도" to "철원군"),
        1014 to ("강원특별자치도" to "화천군"),
        1015 to ("강원특별자치도" to "양구군"),
        1016 to ("강원특별자치도" to "인제군"),
        1017 to ("강원특별자치도" to "고성군"),
        1018 to ("강원특별자치도" to "양양군"),
        1101 to ("충청북도" to "청주시"),
        1102 to ("충청북도" to "충주시"),
        1103 to ("충청북도" to "제천시"),
        1104 to ("충청북도" to "보은군"),
        1105 to ("충청북도" to "옥천군"),
        1106 to ("충청북도" to "영동군"),
        1107 to ("충청북도" to "진천군"),
        1108 to ("충청북도" to "괴산군"),
        1109 to ("충청북도" to "음성군"),
        1110 to ("충청북도" to "단양군"),
        1111 to ("충청북도" to "증평군"),
        1201 to ("충청남도" to "천안시"),
        1202 to ("충청남도" to "공주시"),
        1203 to ("충청남도" to "보령시"),
        1204 to ("충청남도" to "아산시"),
        1205 to ("충청남도" to "서산시"),
        1206 to ("충청남도" to "논산시"),
        1207 to ("충청남도" to "계룡시"),
        1208 to ("충청남도" to "당진시"),
        1209 to ("충청남도" to "금산군"),
        1210 to ("충청남도" to "부여군"),
        1211 to ("충청남도" to "서천군"),
        1212 to ("충청남도" to "청양군"),
        1213 to ("충청남도" to "홍성군"),
        1214 to ("충청남도" to "예산군"),
        1215 to ("충청남도" to "태안군"),
        1301 to ("전라북도" to "전주시"),
        1302 to ("전라북도" to "군산시"),
        1303 to ("전라북도" to "익산시"),
        1304 to ("전라북도" to "정읍시"),
        1305 to ("전라북도" to "남원시"),
        1306 to ("전라북도" to "김제시"),
        1307 to ("전라북도" to "완주군"),
        1308 to ("전라북도" to "진안군"),
        1309 to ("전라북도" to "무주군"),
        1310 to ("전라북도" to "장수군"),
        1311 to ("전라북도" to "임실군"),
        1312 to ("전라북도" to "순창군"),
        1313 to ("전라북도" to "고창군"),
        1314 to ("전라북도" to "부안군"),
        1401 to ("전라남도" to "목포시"),
        1402 to ("전라남도" to "여수시"),
        1403 to ("전라남도" to "순천시"),
        1404 to ("전라남도" to "나주시"),
        1405 to ("전라남도" to "광양시"),
        1406 to ("전라남도" to "담양군"),
        1407 to ("전라남도" to "곡성군"),
        1408 to ("전라남도" to "구례군"),
        1409 to ("전라남도" to "고흥군"),
        1410 to ("전라남도" to "보성군"),
        1411 to ("전라남도" to "화순군"),
        1412 to ("전라남도" to "장흥군"),
        1413 to ("전라남도" to "강진군"),
        1414 to ("전라남도" to "해남군"),
        1415 to ("전라남도" to "영암군"),
        1416 to ("전라남도" to "무안군"),
        1417 to ("전라남도" to "함평군"),
        1418 to ("전라남도" to "영광군"),
        1419 to ("전라남도" to "장성군"),
        1420 to ("전라남도" to "완도군"),
        1421 to ("전라남도" to "진도군"),
        1422 to ("전라남도" to "신안군"),
        1501 to ("경상북도" to "포항시"),
        1502 to ("경상북도" to "경주시"),
        1503 to ("경상북도" to "김천시"),
        1504 to ("경상북도" to "안동시"),
        1505 to ("경상북도" to "구미시"),
        1506 to ("경상북도" to "영주시"),
        1507 to ("경상북도" to "영천시"),
        1508 to ("경상북도" to "상주시"),
        1509 to ("경상북도" to "문경시"),
        1510 to ("경상북도" to "경산시"),
        1511 to ("경상북도" to "의성군"),
        1512 to ("경상북도" to "청송군"),
        1513 to ("경상북도" to "영양군"),
        1514 to ("경상북도" to "영덕군"),
        1515 to ("경상북도" to "청도군"),
        1516 to ("경상북도" to "고령군"),
        1517 to ("경상북도" to "성주군"),
        1518 to ("경상북도" to "칠곡군"),
        1519 to ("경상북도" to "예천군"),
        1520 to ("경상북도" to "봉화군"),
        1521 to ("경상북도" to "울진군"),
        1522 to ("경상북도" to "울릉군"),
        1601 to ("경상남도" to "창원시"),
        1602 to ("경상남도" to "진주시"),
        1603 to ("경상남도" to "통영시"),
        1604 to ("경상남도" to "사천시"),
        1605 to ("경상남도" to "김해시"),
        1606 to ("경상남도" to "밀양시"),
        1607 to ("경상남도" to "거제시"),
        1608 to ("경상남도" to "양산시"),
        1609 to ("경상남도" to "의령군"),
        1610 to ("경상남도" to "함안군"),
        1611 to ("경상남도" to "창녕군"),
        1612 to ("경상남도" to "고성군"),
        1613 to ("경상남도" to "남해군"),
        1614 to ("경상남도" to "하동군"),
        1615 to ("경상남도" to "산청군"),
        1616 to ("경상남도" to "함양군"),
        1617 to ("경상남도" to "거창군"),
        1618 to ("경상남도" to "합천군"),
        1701 to ("제주특별자치도" to "제주시"),
        1702 to ("제주특별자치도" to "서귀포시")
    )

    // 대분류 목록 (시/도)
    val cities = regionData.values.map { it.first }.distinct().sorted()

    // 중분류 목록 (선택된 시/도에 해당하는 구/군)
    val districts = if (activityLocation.isNotEmpty()) {
        regionData.values.filter { it.first == activityLocation }.map { it.second }.sorted()
    } else {
        emptyList()
    }

    LightonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 상단바
                CommonTopBar(
                    title = "아티스트 등록",
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                    )
                )

                // 메인 콘텐츠를 스크롤 가능하게
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 상단 여백
                    Spacer(modifier = Modifier.height(24.dp))

                    // 아티스트 정보 섹션
                    Text(
                        text = "아티스트 정보",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PretendardFamily,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 프로필 이미지
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "프로필 이미지",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    FileUploadField(
                        fileName = profileImageFileName,
                        placeholder = "공연 포스터 및 사진 업로드",
                        onUploadClick = { handleFileSelection("profile") }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "* 10mb 이하 PDF, png, jpeg, jpg, 파일만 업로드 가능합니다.",
                        fontSize = 12.sp,
                        fontFamily = PretendardFamily,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 아티스트명
                    CountingInputField(
                        label = "아티스트명",
                        value = artistName,
                        onValueChange = { artistName = it },
                        maxLength = 20,
                        isRequired = true,
                        placeholder = "아티스트명을 입력해주세요 (20자 이내)"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 아티스트 소개
                    CountingInputField(
                        label = "아티스트 소개",
                        value = artistDescription,
                        onValueChange = { artistDescription = it },
                        maxLength = 200,
                        isRequired = true,
                        placeholder = "아티스트 소개글을 입력해주세요 (200자 이내)",
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 주요 활동 장소
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "주요 활동 장소",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 주요 활동 장소 드롭다운 (대분류/중분류)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        LightonDropdown(
                            label = "",
                            selectedItem = activityLocation,
                            items = cities,
                            onItemSelected = {
                                activityLocation = it
                                activityDistrict = "" // 대분류 변경시 중분류 리셋
                            },
                            placeholder = "대분류",
                            isRequired = false,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )

                        LightonDropdown(
                            label = "",
                            selectedItem = activityDistrict,
                            items = districts,
                            onItemSelected = { activityDistrict = it },
                            placeholder = "중분류",
                            isRequired = false,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 주요 활동 장르
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "주요 활동 장르",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    SimpleDropdownField(
                        value = activityGenre,
                        onValueChange = { activityGenre = it },
                        items = listOf("음악", "연극", "댄스", "뮤지컬", "기타"),
                        placeholder = "장르를 선택해주세요",
                        onClick = { /* TODO: 드롭다운 메뉴 표시 */ }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 주요 활동 이력
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "주요 활동 이력",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    CountingInputField(
                        label = "",
                        value = activityHistory,
                        onValueChange = { activityHistory = it },
                        maxLength = 500,
                        isRequired = false,
                        placeholder = "주요 활동 이력을 작성해 주세요 (500자 이내)",
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 활동 사진 업로드
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "활동 사진 업로드",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    FileUploadField(
                        fileName = activityPhotosFileName,
                        placeholder = "공연 포스터 및 사진 업로드",
                        onUploadClick = { handleFileSelection("photos") }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "* 10mb 이하 PDF, png, jpeg, jpg, 파일만 업로드 가능합니다.",
                        fontSize = 12.sp,
                        fontFamily = PretendardFamily,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 아티스트 증빙자료
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(18.dp)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "아티스트 증빙자료",
                            color = CaptionColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PretendardFamily
                        )

                        Text(
                            text = " *",
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = PretendardFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    FileUploadField(
                        fileName = evidenceDocumentFileName,
                        placeholder = "공연 포스터 및 사진 업로드",
                        onUploadClick = { handleFileSelection("evidence") }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "* 10mb 이하 PDF, png, jpeg, jpg, 파일만 업로드 가능합니다.",
                        fontSize = 12.sp,
                        fontFamily = PretendardFamily,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    // 하단 여백
                    Spacer(modifier = Modifier.height(30.dp))
                }

                // 하단 버튼 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                ) {
                    LightonButton(
                        text = "등록하기",
                        onClick = {
                            // 아티스트 등록 확인 다이얼로그 표시
                            showArtistRegistrationConfirmDialog = true
                        }
                    )
                }
            }
        }
    }

    // 아티스트 등록 확인 다이얼로그
    if (showArtistRegistrationConfirmDialog) {
        ArtistRegistrationConfirmDialog(
            onDismiss = { showArtistRegistrationConfirmDialog = false },
            onConfirm = {
                showArtistRegistrationConfirmDialog = false
                // 실제 등록 로직 (서버 API 호출)
                // TODO: 아티스트 등록 API 호출
                onRegisterClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistRegisterScreenPreview() {
    LightonTheme {
        ArtistRegisterScreen()
    }
}