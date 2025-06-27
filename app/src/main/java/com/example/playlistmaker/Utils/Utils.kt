package com.example.playlistmaker.Utils

import android.content.Context
import android.util.TypedValue

class Utils {
    companion object{
        fun dpToPx(context: Context, dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }
    }
}