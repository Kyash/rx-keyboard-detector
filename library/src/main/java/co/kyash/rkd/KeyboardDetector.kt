package co.kyash.rkd

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import io.reactivex.Observable

class KeyboardDetector constructor(
        private val activity: Activity?
) {

    companion object {
        const val TAG = "KeyboardDetector"
        const val MIN_KEYBOARD_HEIGHT_RATIO = 0.15
    }

    fun observe(): Observable<KeyboardStatus> {
        if (activity == null) {
            Log.w(TAG, "Activity is null")
            return Observable.just(KeyboardStatus.CLOSED)
        }

        val softInputMethod = activity.window.attributes.softInputMode
        if (WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE != softInputMethod
                && WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED != softInputMethod) {
            throw IllegalArgumentException("Activity window SoftInputMethod is not ADJUST_RESIZE")
        }

        val rootView = (activity.findViewById<View>(android.R.id.content) as ViewGroup)

        val windowHeight = DisplayMetrics().let {
            activity.windowManager.defaultDisplay.getMetrics(it)
            it.heightPixels
        }

        return Observable.create<KeyboardStatus> { emitter ->
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                val rect = Rect().apply { rootView.getWindowVisibleDisplayFrame(this) }
                val keyboardHeight = windowHeight - rect.height()

                if (keyboardHeight > windowHeight * MIN_KEYBOARD_HEIGHT_RATIO) {
                    emitter.onNext(KeyboardStatus.OPENED)
                } else {
                    emitter.onNext(KeyboardStatus.CLOSED)
                }
            }

            rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)

            emitter.setCancellable {
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }

        }.distinctUntilChanged()
    }

}