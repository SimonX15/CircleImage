package com.simon.app.circleimage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_change.setOnClickListener {

            if (i % 2 == 0) {
                circle_iv.setImageResource(R.drawable.ic_other)
            } else {
                circle_iv.setImageResource(R.drawable.ic_time)
            }

            i++
        }
    }
}
