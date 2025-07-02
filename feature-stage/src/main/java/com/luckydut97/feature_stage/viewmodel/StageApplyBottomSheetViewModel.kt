package com.luckydut97.feature_stage.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// 하단시트 전체 상태/단계(for 단계전환 및 visible 관리)
class StageApplyBottomSheetViewModel : ViewModel() {
    enum class Step { INFO }
    private val _isSheetVisible = MutableStateFlow(false)
    val isSheetVisible: StateFlow<Boolean> = _isSheetVisible
    private val _step = MutableStateFlow(Step.INFO)
    val step: StateFlow<Step> = _step
    fun open(step: Step = Step.INFO) {
        _step.value = step; _isSheetVisible.value = true
    }
}

// [안내 시트 관련 상태/로직 전용] - 추후 API 호출/데이터 Fetch 필요시 이곳 확장
class StageApplyInfoSheetViewModel : ViewModel() {
    // 예시: val guideText = MutableStateFlow<String>("")
    // suspend fun fetchInfo() { ... }
}

// [인원 선택 시트 관련 상태/로직 only]
class StageApplyPeopleSheetViewModel : ViewModel() {
    private val _peopleCount = MutableStateFlow(1)
    val peopleCount: StateFlow<Int> = _peopleCount
    fun plusPeople() {
        _peopleCount.value += 1
    }

    fun minusPeople() {
        if (_peopleCount.value > 1) _peopleCount.value -= 1
    }
}

// [계좌 안내 시트 관련 상태/로직 only] (API 연동시 fetchAccount() 등으로 확장)
class StageApplyAccountSheetViewModel : ViewModel() {
    private val _accountHolder = MutableStateFlow("홍길동")
    val accountHolder: StateFlow<String> = _accountHolder
    private val _accountNumber = MutableStateFlow("20-555-784532(KB국민)")
    val accountNumber: StateFlow<String> = _accountNumber
    private val _applyCost = MutableStateFlow("10,000원")
    val applyCost: StateFlow<String> = _applyCost
    // 추후 네트워크/API로 변경
    // suspend fun fetchAccountData() { ... }
}