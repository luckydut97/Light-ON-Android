package com.luckydut97.lighton.core.ui.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.InputStream

/**
 * 파일 선택 및 처리 유틸리티
 */
object FilePickerUtil {

    private const val TAG = "FilePickerUtil"
    private const val MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB

    /**
     * 선택된 파일 정보
     */
    data class FileInfo(
        val uri: Uri,
        val fileName: String,
        val fileSize: Long,
        val displayName: String, // UI에 표시할 이름 (길이 제한됨)
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    /**
     * URI에서 파일 정보 추출
     */
    fun getFileInfo(context: Context, uri: Uri): FileInfo {
        try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

                if (it.moveToFirst()) {
                    val fileName = it.getString(nameIndex) ?: "unknown_file"
                    val fileSize = it.getLong(sizeIndex)

                    Log.d(TAG, "선택된 파일 정보: 이름=$fileName, 크기=${fileSize}bytes")

                    // 파일 유효성 검증
                    val validationResult = validateFile(fileName, fileSize)

                    return FileInfo(
                        uri = uri,
                        fileName = fileName,
                        fileSize = fileSize,
                        displayName = fileName.truncateFileName(25), // 표시용 이름
                        isValid = validationResult.first,
                        errorMessage = validationResult.second
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "파일 정보 추출 실패", e)
            return FileInfo(
                uri = uri,
                fileName = "unknown_file",
                fileSize = 0,
                displayName = "unknown_file",
                isValid = false,
                errorMessage = "파일 정보를 읽을 수 없습니다."
            )
        }

        return FileInfo(
            uri = uri,
            fileName = "unknown_file",
            fileSize = 0,
            displayName = "unknown_file",
            isValid = false,
            errorMessage = "파일 정보를 읽을 수 없습니다."
        )
    }

    /**
     * 파일 유효성 검증
     */
    private fun validateFile(fileName: String, fileSize: Long): Pair<Boolean, String?> {
        // 파일 크기 검증
        if (fileSize > MAX_FILE_SIZE) {
            return false to "파일 크기가 10MB를 초과합니다."
        }

        // 파일 확장자 검증
        if (!fileName.isAllowedFileExtension()) {
            return false to "허용되지 않는 파일 형식입니다. (PDF, PNG, JPEG, JPG만 가능)"
        }

        return true to null
    }

    /**
     * 파일 데이터 읽기 (필요시 사용)
     */
    fun readFileData(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val data = inputStream?.readBytes()
            inputStream?.close()

            Log.d(TAG, "파일 데이터 읽기 완료: ${data?.size ?: 0} bytes")
            data
        } catch (e: Exception) {
            Log.e(TAG, "파일 데이터 읽기 실패", e)
            null
        }
    }

    /**
     * 파일 선택 결과 로깅
     */
    fun logFileSelection(fileInfo: FileInfo) {
        Log.d(
            TAG, """
            파일 선택 결과:
            - 파일명: ${fileInfo.fileName}
            - 크기: ${fileInfo.fileSize} bytes (${fileInfo.fileSize / 1024}KB)
            - 표시명: ${fileInfo.displayName}
            - 유효성: ${fileInfo.isValid}
            - 오류메시지: ${fileInfo.errorMessage ?: "없음"}
            - URI: ${fileInfo.uri}
        """.trimIndent()
        )
    }
}