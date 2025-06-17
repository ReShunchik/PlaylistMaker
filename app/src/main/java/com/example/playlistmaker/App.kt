package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.datas.Track
import com.google.gson.Gson

class App: Application() {

    var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARKTHEME, false)
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences
            .edit()
            .putBoolean(DARKTHEME, darkTheme)
            .apply()
    }

    fun freshHistory(track: Track){
        val tracks = ArrayList<Track>()
        tracks.addAll(readTracks())
        if(tracks.contains(track)){
            tracks.remove(track)
        }
        tracks.add(0, track)
        if(tracks.lastIndex > 9){
            tracks.removeAt(tracks.lastIndex)
        }
        writeTracks(tracks)
    }

    fun clearHistory(){
        sharedPreferences
            .edit()
            .remove(HISTORY)
            .apply()
    }

    fun readTracks(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun writeTracks(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(HISTORY, json)
            .apply()
    }

    companion object{
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARKTHEME = "dark_theme"
        const val HISTORY = "history"
    }
}