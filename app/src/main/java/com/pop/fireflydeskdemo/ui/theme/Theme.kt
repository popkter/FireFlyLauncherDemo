package com.pop.fireflydeskdemo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.pop.fireflydeskdemo.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FireFlyDeskDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val Monoton = FontFamily(
    Font(R.font.monoton_regular)
)

val TiltWrap = FontFamily(
    Font(R.font.tiltwarp_regular)
)

val Mulish = FontFamily(
    Font(R.font.mulish_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.mulish_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.mulish_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.mulish_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.mulish_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.mulish_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.mulish_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.mulish_black, FontWeight.Black, FontStyle.Normal),

    Font(R.font.mulish_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.mulish_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.mulish_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.mulish_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.mulish_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.mulish_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.mulish_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.mulish_black_italic, FontWeight.Black, FontStyle.Italic)
)