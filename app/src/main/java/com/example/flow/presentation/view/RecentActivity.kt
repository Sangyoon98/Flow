package com.example.flow.presentation.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.flow.databinding.ActivityRecentBinding
import com.example.flow.presentation.viewmodel.RecentActivityViewModel
import kotlinx.coroutines.launch

class RecentActivity : AppCompatActivity() {
    private var mBinding: ActivityRecentBinding? = null
    private val binding get() = mBinding!!
    lateinit var viewModel: RecentActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRecentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RecentActivityViewModel::class.java]
        supportActionBar?.title = "최근 검색"
        createTextView()
    }

    //텍스트 뷰 동적 생성
    private fun createTextView() {
        lifecycleScope.launch {
            //DB에서 최근 10개 데이터를 가져와 데이터마다 텍스트 뷰 생성
            val recent = viewModel.getRecentList()
            recent.forEach {
                //TextView 설정
                val textView = TextView(this@RecentActivity)
                textView.text = it.search
                textView.setPadding(50)
                textView.background = obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground)).getDrawable(0)
                binding.recentView.addView(textView)

                //TextView 클릭 이벤트
                textView.setOnClickListener {
                    lifecycleScope.launch {
                        //선택한 데이터를 DB에서 삭제 (메인 액티비티에서 검색 후 검색어 다시 DB에 저장)
                        viewModel.deleteRecent(textView.text.toString())
                    }

                    //메인 액티비티로 인텐트
                    val intent = Intent(this@RecentActivity, MainActivity::class.java)
                    intent.putExtra("recent", textView.text)
                    setResult(Activity.RESULT_OK, intent)
                    this@RecentActivity.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}