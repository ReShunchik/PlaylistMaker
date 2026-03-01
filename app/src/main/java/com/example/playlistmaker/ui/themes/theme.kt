package com.example.playlistmaker.ui.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import org.koin.java.KoinJavaComponent.getKoin


val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = Grey,
    primaryContainer = Grey,
    onPrimaryContainer = GreyLight,

    secondary = White,
    onSecondary = GreyLight,
    secondaryContainer = White,
    //onSecondaryContainer = LightOnSecondaryContainer,

    background = White,
    //onBackground = LightOnBackground,

    //surface = LightSurface,
    //onSurface = LightOnSurface,

    //error = LightError,
    //onError = LightOnError
)

val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = White,
    primaryContainer = Black,
    onPrimaryContainer = White,

    secondary = Blue,
    onSecondary = BlueLight,
    secondaryContainer = Black,
    //onSecondaryContainer = DarkOnSecondaryContainer,

    background = BlackLight,
    //onBackground = DarkOnBackground,

    //surface = DarkSurface,
    //onSurface = DarkOnSurface,

    //error = DarkError,
    //onError = DarkOnError
)


@Composable
fun PlaylistMakerTheme(
    content: @Composable () -> Unit
) {
    val settingsInteractor: SettingsInteractor = getKoin().get()
    settingsInteractor.applySavedTheme()
    var darkTheme: Boolean = settingsInteractor.getThemeSettings().darkTheme
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}