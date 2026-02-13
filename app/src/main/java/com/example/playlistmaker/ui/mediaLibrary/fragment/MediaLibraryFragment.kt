package com.example.playlistmaker.ui.mediaLibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.themes.PlaylistMakerTheme
import kotlinx.coroutines.launch
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.tooling.preview.Preview

class MediaLibraryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {
                    MediaLibraryFragmentUi()
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun MediaLibraryFragmentUi() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = pagerState.currentPage  // ИЗМЕНЕНО

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp),
            text = stringResource(R.string.media_library),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.ys_display_medium)),
            color = MaterialTheme.colorScheme.primary
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 16.dp),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            divider = {}
        ) {
            listOf(
                stringResource(R.string.favourite) to 0,
                stringResource(R.string.playlist) to 1
            ).forEachIndexed { index, (title, pageIndex) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pageIndex)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = if (selectedTabIndex == index) 16.sp else 14.sp,
                            fontFamily = FontFamily(
                                Font(
                                    if (selectedTabIndex == index)
                                        R.font.ys_display_medium
                                    else
                                        R.font.ys_display_regular
                                )
                            ),
                            color = if (selectedTabIndex == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when(page) {
                0 -> FirstScreen()
                1 -> SecondScreen()
            }
        }

    }
}

@Composable
fun FirstScreen() {
    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                (context as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(id, FavouriteFragment())
                    .commit()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun SecondScreen() {
    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                (context as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(id, PlaylistFragment())
                    .commit()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}