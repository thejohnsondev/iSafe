package com.thejohnsondev.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.PersistableBundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.google.gson.Gson
import com.thejohnsondev.model.EmailIncorrectReason
import com.thejohnsondev.model.EmailValidationState
import com.thejohnsondev.model.IncorrectPasswordReason
import com.thejohnsondev.model.PasswordValidationState
import java.io.File
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

const val NOTE_TIME_FORMAT = "dd MMM yyyy, hh:mm"

fun String.isPasswordValid(): PasswordValidationState {
    val length = this.length
    if (length < PASS_MIN_SIZE) return PasswordValidationState.PasswordIncorrectState(
        IncorrectPasswordReason.BAD_LENGTH
    )

    val containsNumbers = this.any { it.isDigit() }
    if (!containsNumbers) return PasswordValidationState.PasswordIncorrectState(
        IncorrectPasswordReason.NO_NUMBERS
    )

    val containsUpperCase = this.any { it.isUpperCase() }
    if (!containsUpperCase) return PasswordValidationState.PasswordIncorrectState(
        IncorrectPasswordReason.NO_CAPITAL
    )

    val containsLowerCase = this.any { it.isLowerCase() }
    if (!containsLowerCase) return PasswordValidationState.PasswordIncorrectState(
        IncorrectPasswordReason.NO_SMALL
    )

    return PasswordValidationState.PasswordCorrectState
}

fun String.isEmailValid(): EmailValidationState {
    return if (this.isNotEmpty() && getEmailPattern().matcher(this).matches()) {
        EmailValidationState.EmailCorrectState
    } else {
        EmailValidationState.EmailIncorrectState(EmailIncorrectReason.INCORRECT)
    }
}

private fun getEmailPattern(): Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

fun getFriendlyMessage(rawMessage: String?): String {
    return if (rawMessage?.any { it.isLowerCase() } != true) {
        getAuthErrorMessage(rawMessage)
    } else rawMessage
}

fun Any?.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String?.fromJson(): T {
    val jsonReader = com.google.gson.stream.JsonReader(StringReader(this))
    jsonReader.isLenient = true
    return Gson().fromJson(jsonReader, T::class.java)
}

@Suppress("DEPRECATION")
fun Uri.asFile(context: Context): File? {
    context.contentResolver
        .query(this, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        ?.use { cursor ->
            cursor.moveToFirst()
            val cursorData =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

            return if (cursorData == null) {
                returnCursorData(this, context)?.let { File(it) }
            } else {
                File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)))
            }
        }
    return null
}

@Suppress("DEPRECATION")
private fun returnCursorData(uri: Uri?, context: Context): String? {
    if (DocumentsContract.isDocumentUri(context, uri)) {
        val wholeID = DocumentsContract.getDocumentId(uri)
        val splits = wholeID.split(":".toRegex()).toTypedArray()
        if (splits.size == 2) {
            val id = splits[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            val sel = MediaStore.Images.Media._ID + "=?"

            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )

            val columnIndex: Int? = cursor?.getColumnIndex(column[0])
            if (cursor?.moveToFirst() == true) {
                return columnIndex?.let { cursor.getString(it) }
            }
            cursor?.close()
        }
    } else {
        return uri?.path
    }
    return null
}

fun Long.getTimeFormatted(format: String): String {
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun String.hidden(): String {
    return replace(Regex("[\\s\\S]"), "*")
}

fun ClipboardManager.copySensitiveData(string: String) {
    setPrimaryClip(ClipData.newPlainText(string, string).apply {
        description.extras = PersistableBundle().apply {
            putBoolean("android.content.extra.IS_SENSITIVE", true)
        }
    })
}


fun ClipboardManager.copyData(string: String) {
    setPrimaryClip(ClipData.newPlainText(string, string))
}