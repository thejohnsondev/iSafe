package com.thejohnsondev.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.thejohnsondev.common.R

@Composable
fun getPrivacyPolicyAcceptText(): AnnotatedString {
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
        ) {
            append(stringResource(R.string.accept_and_agree))
            append(" ")
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation(tag = TERMS_OF_USE_TAG, annotation = "click")
            append(stringResource(R.string.terms_of_use))
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
        ) {
            append(" ")
            append(stringResource(R.string.and))
            append(" ")
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation(tag = PRIVACY_POLICY_TAG, annotation = "click")
            append(stringResource(R.string.privacy_policy))
        }
    }
    return text
}

const val PRIVACY_POLICY_TAG = "privacy-policy"
const val TERMS_OF_USE_TAG = "terms-of-use"