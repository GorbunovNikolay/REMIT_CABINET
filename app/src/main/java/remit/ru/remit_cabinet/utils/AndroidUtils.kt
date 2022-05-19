package remit.ru.remit_cabinet.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtils {

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun vibratePhone(view: View) {
        val vibe: Vibrator = view.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibe.vibrate(500)
    }
}