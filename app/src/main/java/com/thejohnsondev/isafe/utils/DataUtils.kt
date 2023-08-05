package com.thejohnsondev.isafe.utils

import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.EmailValidationState
import com.thejohnsondev.isafe.domain.models.PasswordValidationState
import java.util.regex.Pattern

fun String.isPasswordValid(): PasswordValidationState {
    val length = this.length
    if (length < PASS_MIN_SIZE) return PasswordValidationState.PasswordIncorrectState(R.string.password_error_bad_length)

    val containsNumbers = this.any { it.isDigit() }
    if (!containsNumbers) return PasswordValidationState.PasswordIncorrectState(R.string.password_error_no_numbers)

    val containsUpperCase = this.any { it.isUpperCase() }
    if (!containsUpperCase) return PasswordValidationState.PasswordIncorrectState(R.string.password_error_no_capital)

    val containsLowerCase = this.any { it.isLowerCase() }
    if (!containsLowerCase) return PasswordValidationState.PasswordIncorrectState(R.string.password_error_no_small)

    return PasswordValidationState.PasswordCorrectState
}

fun String.isEmailValid(): EmailValidationState {
    return if (this.isNotEmpty() && getEmailPattern().matcher(this).matches()) {
        EmailValidationState.EmailCorrectState
    } else {
        EmailValidationState.EmailIncorrectState(R.string.email_error_incorrect)
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