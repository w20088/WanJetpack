package com.longjunhao.wanjetpack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.longjunhao.wanjetpack.databinding.ListItemProjectBinding
import com.longjunhao.wanjetpack.adapter.ProjectAdapter.ProjectViewHolder
import com.longjunhao.wanjetpack.data.Article

/**
 * .ProjectAdapter
 *
 * @author Admitor
 * @date 2021/05/31
 */
class ProjectAdapter(
    private val favoriteOnClick: (Article, Int) -> Unit
) : PagingDataAdapter<Article, ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder(
            ListItemProjectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.binding.favorite.setOnClickListener {
                holder.binding.project?.let {
                    favoriteOnClick(it, position)
                }
            }
        }
    }

    class ProjectViewHolder(
        val binding: ListItemProjectBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Article) {
            binding.apply {
                project = item
                executePendingBindings()
            }
        }
    }
}

private class ProjectDiffCallback : DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}