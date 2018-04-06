package co.kyash.rxkeyboarddetector;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import co.kyash.rkd.KeyboardDetector
import co.kyash.rkd.KeyboardStatus
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.status)

        compositeDisposable.add(
                KeyboardDetector(this).observe().subscribe({ status ->
                    when (status) {
                        KeyboardStatus.OPENED -> {
                            textView.text = getString(R.string.opened)
                        }
                        KeyboardStatus.CLOSED -> {
                            textView.text = getString(R.string.closed)
                        }
                        else -> {
                            //
                        }
                    }
                })
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
