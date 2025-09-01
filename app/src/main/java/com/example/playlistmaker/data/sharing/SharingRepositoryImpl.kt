package com.example.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.api.SharingRepository

class SharingRepositoryImpl(
    private val context: Context
): SharingRepository {

    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
        }
        context.startActivity(shareIntent)
    }

    override fun openTerms() {
        val agreement = Uri.parse(context.getString(R.string.agreement_link))
        val agreementIntent = Intent(Intent.ACTION_VIEW, agreement)
        context.startActivity(agreementIntent)
    }

    override fun openSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("v.gorshenin_2004@mail.ru"))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_message))
        }
        context.startActivity(supportIntent)
    }

}