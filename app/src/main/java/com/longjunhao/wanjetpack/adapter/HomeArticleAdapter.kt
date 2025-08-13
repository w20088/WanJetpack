package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.adapter.HomeArticleAdapter.ArticleViewHolder
import com.longjunhao.wanjetpack.data.Article
import com.longjunhao.wanjetpack.databinding.ListItemArticleBinding

/**
 * .ArticleAdapter
 *
 * @author Admitor
 * @date 2021/05/21
 */
class HomeArticleAdapter(
    private val favoriteOnClick: (Article, Int) -> Unit
) : PagingDataAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ListItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * favorite的点击事件没有放在ArticleViewHolder的init{}中，因为在ArticleViewHolder中获取不到点击的position
     * todo： favorite的点击事件放在ViewHolder的init{}和adapter的onBindViewHolder中有什么区别吗？
     */
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
        val binding: ListItemArticleBinding
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