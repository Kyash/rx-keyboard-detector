package co.kyash.rxkeyboarddetector;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import co.kyash.rkd.KeyboardDetector

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.status)

        KeyboardDetector(this).observe().subscribe({
            textView.text = it.name
        })
    }
}
