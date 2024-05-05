package com.thejohnsondev.ui.ui_model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.thejohnsondev.common.R

sealed class SettingsSubSection(
    val sectionTitleRes: Int,
    val sectionDescriptionRes: Int? = null,
    val sectionIcon: ImageVector,
    val expanded: Boolean
) {
    object ManageAccountSub : SettingsSubSection(
        sectionTitleRes = R.string.manage_account,
        sectionIcon = Icons.Default.Person,
        expanded = false
    )

    object GeneralSettingsSub : SettingsSubSection(
        sectionTitleRes = R.string.general_title,
        sectionDescriptionRes = R.string.general_description,
        sectionIcon = Icons.Default.Settings,
        expanded = false
    )

    object StyleSettingsSub : SettingsSubSection(
        sectionTitleRes = R.string.setting_title_style,
        sectionDescriptionRes = R.string.setting_description_style,
        sectionIcon = Icons.Default.FormatPaint,
        expanded = false
    )

    object PrivacySettingsSub : SettingsSubSection(
        sectionTitleRes = R.string.security_and_privacy_title,
        sectionDescriptionRes = R.string.security_and_privacy_description,
        sectionIcon = Icons.Default.Security,
        expanded = false
    )

}