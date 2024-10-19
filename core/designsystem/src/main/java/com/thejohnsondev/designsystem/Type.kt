package com.thejohnsondev.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_black, FontWeight.Black),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_thin, FontWeight.Thin),
    Font(R.font.poppins_extra_light, FontWeight.ExtraLight),
    Font(R.font.poppins_italic, FontWeight.Normal, style = FontStyle.Italic),
)

val ubuntuFontFamily = FontFamily(
    Font(R.font.ubuntu_regular),
    Font(R.font.ubuntu_medium, FontWeight.Medium),
    Font(R.font.ubuntu_medium, FontWeight.SemiBold),
    Font(R.font.ubuntu_bold, FontWeight.Bold),
    Font(R.font.ubuntu_bold, FontWeight.Black),
    Font(R.font.ubuntu_light, FontWeight.Light),
    Font(R.font.ubuntu_light, FontWeight.Thin),
    Font(R.font.ubuntu_regular, FontWeight.Normal, style = FontStyle.Italic),
)

fun getGlobalFontFamily(): FontFamily {
    return FontFamily.Serif
}

// Set of Material typography styles to start with
val Typography = Typography(
    // Display Large - Montserrat 57/64 . -0.25px
    displayLarge = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),

    // Display Medium - Montserrat 45/52 . 0px
    displayMedium = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),

    // Display Small - Montserrat 36/44 . 0px
    displaySmall = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Large - Montserrat 32/40 . 0px
    headlineLarge = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Medium - Montserrat 28/36 . 0px
    headlineMedium = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),

    // Headline Small - Montserrat 24/32 . 0px
    headlineSmall = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // Title Large - Montserrat 22/28 . 0px
    titleLarge = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),

    // Title Medium - Montserrat 16/24 . 0.15px
    titleMedium = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),

    // Title Small - Montserrat 14/20 . 0.1px
    titleSmall = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Label Large - Montserrat 14/20 . 0.1px
    labelLarge = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Label Medium - Montserrat 12/16 . 0.5px
    labelMedium = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),

    // Label Small - Montserrat 11/16 . 0.5px
    labelSmall = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),

    // Body Large - Montserrat 16/24 . 0.5px
    bodyLarge = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),

    // Body Medium - Montserrat 14/20 . 0.25px
    bodyMedium = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    // Body Small - Montserrat 12/16 . 0.4px
    bodySmall = TextStyle(
        fontFamily = getGlobalFontFamily(),
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
)