package com.luckydut97.lighton.core.ui.utils

/**
 * 파일명을 지정된 길이로 제한하고 필요시 ...을 추가
 */
fun String.truncateFileName(maxLength: Int = 20): String {
    if (this.length <= maxLength) return this

    val extension = this.substringAfterLast('.', "")
    val nameWithoutExtension = this.substringBeforeLast('.')

    // 확장자가 있는 경우
    if (extension.isNotEmpty()) {
        val availableLength = maxLength - extension.length - 4 // "..." + "." 고려
        if (availableLength > 0) {
            return "${nameWithoutExtension.take(availableLength)}...$extension"
        }
    }

    // 확장자가 없거나 너무 긴 경우
    return "${this.take(maxLength - 3)}..."
}

/**
 * 파일 확장자 추출
 */
fun String.getFileExtension(): String {
    return this.substringAfterLast('.', "").lowercase()
}

/**
 * 허용된 파일 확장자인지 확인
 */
fun String.isAllowedFileExtension(): Boolean {
    val allowedExtensions = listOf("pdf", "png", "jpeg", "jpg")
    return this.getFileExtension() in allowedExtensions
}