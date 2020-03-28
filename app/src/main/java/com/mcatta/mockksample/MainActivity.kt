package com.mcatta.mockksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mcatta.mockksample.ui.MainContract
import com.mcatta.mockksample.ui.UiDataModel

class MainActivity : AppCompatActivity(), MainContract.View {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResult(result: List<UiDataModel>) {
        Log.d(TAG, "$result")
    }

    override fun onError(error: Throwable) {
        Log.e(TAG, "Error on MainActivity result", error)
    }
}
