package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    // 變數綁定元件
    private lateinit var btnCalculate: Button
    private lateinit var edHeight: EditText
    private lateinit var edWeight: EditText
    private lateinit var edAge: EditText
    private lateinit var tvWeightResult: TextView
    private lateinit var tvFatResult: TextView
    private lateinit var tvBmiResult: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var llProgress: LinearLayout
    private lateinit var btnBoy: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定邊界處理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 綁定 XML 元件
        btnCalculate = findViewById(R.id.btnCalculate)
        edHeight = findViewById(R.id.edHeight)
        edWeight = findViewById(R.id.edWeight)
        edAge = findViewById(R.id.edAge)
        tvWeightResult = findViewById(R.id.tvWeightResult)
        tvFatResult = findViewById(R.id.tvFatResult)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        tvProgress = findViewById(R.id.tvProgress)
        progressBar = findViewById(R.id.progressBar)
        llProgress = findViewById(R.id.llProgress)
        btnBoy = findViewById(R.id.btnBoy)

        // 計算按鈕監聽器
        btnCalculate.setOnClickListener {
            if (edHeight.text.isEmpty() || edWeight.text.isEmpty() || edAge.text.isEmpty()) {
                showToast("請輸入所有欄位")
            } else {
                runCalculationThread()
            }
        }
    }

    // 顯示提示訊息
    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    // 計算並顯示結果
    private fun runCalculationThread() {
        // 初始化顯示內容
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
        progressBar.progress = 0
        tvProgress.text = "0%"
        llProgress.visibility = View.VISIBLE

        // 計算處理執行緒
        Thread {
            var progress = 0
            // 模擬進度條
            while (progress < 100) {
                try {
                    Thread.sleep(50)
                    progress++
                    runOnUiThread {
                        progressBar.progress = progress
                        tvProgress.text = "$progress%"
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // 取得輸入資料
            val height = edHeight.text.toString().toDouble()
            val weight = edWeight.text.toString().toDouble()
            val age = edAge.text.toString().toDouble()
            val bmi = weight / ((height / 100).pow(2)) // 計算 BMI

            // 計算標準體重和體脂肪
            val (standWeight, bodyFat) = calculateBodyStats(height, weight, age, bmi)

            // 更新 UI 顯示結果
            runOnUiThread {
                llProgress.visibility = View.GONE
                tvWeightResult.text = "標準體重 \n${String.format("%.2f", standWeight)}"
                tvFatResult.text = "體脂肪 \n${String.format("%.2f", bodyFat)}"
                tvBmiResult.text = "BMI \n${String.format("%.2f", bmi)}"
            }
        }.start()
    }

    // 根據性別計算標準體重與體脂肪
    private fun calculateBodyStats(height: Double, weight: Double, age: Double, bmi: Double): Pair<Double, Double> {
        return if (btnBoy.isChecked) {
            // 男性：計算標準體重與體脂肪
            Pair((height - 80) * 0.7, 1.39 * bmi + 0.16 * age - 19.34)
        } else {
            // 女性：計算標準體重與體脂肪
            Pair((height - 70) * 0.6, 1.39 * bmi + 0.16 * age - 9)
        }
    }
}
