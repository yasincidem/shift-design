package com.yasincidem.shiftdesign

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import coil.decode.DataSource
import coil.request.Request
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val IMAGE_URL: String = "https://yt3.ggpht.com/a/AATXAJwInaoxUY9oWw2Lw6ZtkmJeHkTfEOjZN0affw=s720-c-k-c0xffffffff-no-rj-mo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shift_image_view_first.setOnClickListener {
            Toast.makeText(this, "First ImageView clicked", Toast.LENGTH_SHORT).show()
        }

        shift_image_view_second.apply {
            load(IMAGE_URL) {
                this.listener(object : Request.Listener {
                    override fun onSuccess(request: Request, source: DataSource) {
                        super.onSuccess(request, source)
                        drawBorder()
                    }
                })
            }
        }
    }
}