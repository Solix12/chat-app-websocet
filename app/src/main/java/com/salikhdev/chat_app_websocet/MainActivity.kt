package com.salikhdev.chat_app_websocet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.salikhdev.chat_app_websocet.databinding.ActivityMainBinding
import java.util.Random


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pulsator = binding.pulsator
        val random = Random()

        pulsator.start()

        binding.join.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("Name", "Anonymous- ${random.nextInt(9999)}")
            startActivity(intent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
            pulsator.stop()
        }

    }
}