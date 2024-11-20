package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 適配全螢幕模式，處理系統邊距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化元件
        val spinner = findViewById<Spinner>(R.id.spinner)
        val listView = findViewById<ListView>(R.id.listView)
        val gridView = findViewById<GridView>(R.id.gridView)

        // 資料初始化
        val countList = (1..10).map { "${it}個" } // 模擬 10 個購買數量資訊
        val itemList = mutableListOf<Item>()
        val priceRange = 10..100

        // 從資源中讀取水果圖片，並建立水果資訊
        val imageArray = resources.obtainTypedArray(R.array.image_list)
        for (index in 0 until imageArray.length()) {
            val photoId = imageArray.getResourceId(index, 0)
            val itemName = "水果${index + 1}"
            val itemPrice = priceRange.random()
            itemList.add(Item(photoId, itemName, itemPrice))
        }
        imageArray.recycle() // 釋放資源

        // 配置 Spinner
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countList)

        // 配置 GridView
        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, itemList, R.layout.adapter_vertical)

        // 配置 ListView
        listView.adapter = MyAdapter(this, itemList, R.layout.adapter_horizontal)
    }
}
