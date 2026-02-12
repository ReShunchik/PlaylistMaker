package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.R
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ColorFilter
import com.example.playlistmaker.ui.themes.Blue
import com.example.playlistmaker.ui.themes.BlueLight
import com.example.playlistmaker.ui.themes.PlaylistMakerTheme



class SettingsFragment: Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme() {
                    SettingsFragmentUI(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SettingsFragmentUI(viewModel: SettingsViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            text = stringResource( R.string.settings ),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier
            .fillMaxWidth()
            .height(
                24.dp
            )
        )
        ThemeSettings(stringResource(R.string.dark_theme), viewModel)
        SettingsButton(
            stringResource(R.string.share_app),
            R.drawable.ic_share_16,
            { viewModel.shareApp() }
        )
        SettingsButton(
            stringResource(R.string.email_support),
            R.drawable.ic_support_24,
            { viewModel.openSupport() }
        )
        SettingsButton(
            stringResource(R.string.user_agreement),
            R.drawable.ic_move_on_24,
            { viewModel.openTerms() }
        )
    }
}

@Composable
fun ThemeSettings(title: String, viewModel: SettingsViewModel){
    val darkThemeEnabled: Boolean by viewModel.darkTheme.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
                .padding(vertical = 20.dp),
            color = MaterialTheme.colorScheme.primary

        )
        Switch(
            checked = darkThemeEnabled,
            onCheckedChange = { viewModel.switchTheme(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueLight,
            )
        )
    }
}

@Composable
fun SettingsButton(title: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable{ onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            color = MaterialTheme.colorScheme.primary
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}




