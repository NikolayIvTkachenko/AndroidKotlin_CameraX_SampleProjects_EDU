package com.rsh.tkachenkoni.app_photo002


import android.widget.Toast
import androidx.fragment.app.FragmentActivity


/**
 * Shows a [Toast] on the UI thread.
 *
 * @param text The message to show
 */
fun FragmentActivity.showToast(text: String) {
    runOnUiThread { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
}
