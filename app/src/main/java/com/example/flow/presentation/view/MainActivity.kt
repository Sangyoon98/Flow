package com.example.flow.presentation.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.flow.R
import com.example.flow.databinding.ActivityMainBinding
import com.example.flow.presentation.adapter.PagingDataRecyclerViewAdapter
import com.example.flow.presentation.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var recyclerViewAdapter: PagingDataRecyclerViewAdapter
    private lateinit var resultIntent: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setAdapter()
        recentSearchIntent()
        searchButtonAction()
    }

    //어댑터 설정
    private fun setAdapter() {
        recyclerViewAdapter = PagingDataRecyclerViewAdapter(this@MainActivity)
        binding.recyclerView.adapter = recyclerViewAdapter
        //화면 회전 시 데이터 유지를 위해 searchResult 옵저빙
        viewModel.searchResult.observe(this) {
            //아이템 어댑터 전달
            recyclerViewAdapter.submitData(lifecycle, it)
        }
    }

    //인텐트 결과 받기
    private fun recentSearchIntent() {
        resultIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val recent = it.data?.getStringExtra("recent")
                binding.searchEdt.setText(recent)
                //불러온 검색어로 검색 실행
                viewModel.search(binding.searchEdt.text.toString())
            }
        }
    }

    //검색 버튼
    private fun searchButtonAction() {
        binding.searchBtn.setOnClickListener {
            viewModel.search(binding.searchEdt.text.toString())
            // 키보드 내리기
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.searchEdt.windowToken, 0)
        }
    }

    //메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴 클릭시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.recentBtn -> {
                val intent = Intent(this, RecentActivity::class.java)
                resultIntent.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}