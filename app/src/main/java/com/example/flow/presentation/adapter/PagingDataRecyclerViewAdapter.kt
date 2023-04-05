package com.example.flow.presentation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flow.data.model.BookItems
import com.example.flow.databinding.RecyclerviewLayoutBinding

class PagingDataRecyclerViewAdapter(private val context: Context) :
    PagingDataAdapter<BookItems, PagingDataRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    //ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //ViewHolder 바인딩
    override fun onBindViewHolder(holder: PagingDataRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //ViewHolder 클래스
    inner class ViewHolder(private val binding: RecyclerviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookItems?) {
            item?.let {
                Glide.with(itemView).load(item.image).into(binding.image)
                binding.title.text = "제목: " + item.title
                binding.author.text = "저자: " + item.author
                binding.publisher.text = "출판사: " + item.publisher
                binding.discount.text = "가격: " + item.discount

                //아이템 클릭 이벤트
                binding.root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                    context.startActivity(intent)
                }
            }
        }
    }

    //ItemDiffCallback : DiffUtil.Callback 상속하는 콜백 클래스
    object ItemDiffCallback : DiffUtil.ItemCallback<BookItems>() {
        //두 객체를 비교해 링크가 같은지 체크
        override fun areItemsTheSame(oldItem: BookItems, newItem: BookItems): Boolean {
            return oldItem.link == newItem.link
        }

        //두 객체의 내용이 같은지 확인
        override fun areContentsTheSame(oldItem: BookItems, newItem: BookItems): Boolean {
            return oldItem == newItem
        }

    }

}