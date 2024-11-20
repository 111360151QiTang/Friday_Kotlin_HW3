package com.example.lab9_1

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    // 進度變數，兔子與烏龜的進度
    private var progressRabbit = 0
    private var progressTurtle = 0

    // 綁定元件
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    // CoroutineScope 用於處理協程
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定邊緣顯示
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 綁定元件
        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        // 設定按鈕監聽器
        btnStart.setOnClickListener {
            startRace()
        }
    }

    private fun startRace() {
        // 禁用按鈕避免重複操作
        btnStart.isEnabled = false

        // 初始化進度
        progressRabbit = 0
        progressTurtle = 0
        sbRabbit.progress = 0
        sbTurtle.progress = 0

        // 開始兔子與烏龜的賽跑
        mainScope.launch {
            val rabbitJob = launch { runRabbit() }
            val turtleJob = launch { runTurtle() }
            rabbitJob.join()  // 等待兔子完成
            turtleJob.join()  // 等待烏龜完成
            btnStart.isEnabled = true // 賽跑結束，按鈕可操作
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // 模擬兔子跑步的協程
    private suspend fun runRabbit() {
        val sleepProbability = arrayOf(true, true, false)
        while (progressRabbit < 100 && progressTurtle < 100) {
            delay(100) // 延遲0.1秒更新賽況
            if (sleepProbability.random()) {
                delay(300) // 兔子偷懶0.3秒
            }
            progressRabbit += 3 // 每次跑3步
            sbRabbit.progress = progressRabbit

            if (progressRabbit >= 100 && progressTurtle < 100) {
                showToast("兔子勝利")
                return
            }
        }
    }

    // 模擬烏龜跑步的協程
    private suspend fun runTurtle() {
        while (progressTurtle < 100 && progressRabbit < 100) {
            delay(100) // 延遲0.1秒更新賽況
            progressTurtle += 1 // 每次跑1步
            sbTurtle.progress = progressTurtle

            if (progressTurtle >= 100 && progressRabbit < 100) {
                showToast("烏龜勝利")
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel() // 取消協程作用範圍
    }
}
