package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.CollectionArticleAdapter.ApiArticleViewHolder
import com.longjunhao.wanjetpack.data.Article
import com.longjunhao.wanjetpack.databinding.ListItemCollectionBinding

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/06/11
 */
class CollectionArticleAdapter : PagingDataAdapter<Article, ApiArticleViewHolder>(ApiArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiArticleViewHolder {
        return ApiArticleViewHolder(
            ListItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ApiArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    class ApiArticleViewHolder(
        private val binding: ListItemCollectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.apply {
                article = item
                executePendingBindings()
            }
        }

    }

    private class ApiArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
}