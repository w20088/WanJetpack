package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.SearchAdapter.ArticleViewHolder
import com.longjunhao.wanjetpack.data.Article
import com.longjunhao.wanjetpack.databinding.ListItemSearchBinding

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/06/21
 */
class SearchAdapter(
    private val favoriteOnClick: (Article, Int) -> Unit
) : PagingDataAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ListItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
            holder.binding.favorite.setOnClickListener {
                holder.binding.article?.let {
                    favoriteOnClick(it, position)
                }
            }
        }
    }

    class ArticleViewHolder(
        val binding: ListItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Article) {
            binding.apply {
                article = item
                executePendingBindings()
            }
        }

    }

    private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
}