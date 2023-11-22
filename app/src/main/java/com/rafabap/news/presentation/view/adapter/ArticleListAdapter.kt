package com.rafabap.news.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rafabap.news.R
import com.rafabap.news.databinding.TopHeadlineItemLayoutBinding
import com.rafabap.news.domain.model.Article
import com.rafabap.news.common.util.extension.hide
import com.rafabap.news.common.util.extension.show
import com.squareup.picasso.Picasso

class ArticleListAdapter(private val adapterEvents: ArticleViewHolder.TopHeadlineListAdapterEvents)
    : ListAdapter<Article, ArticleListAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent, adapterEvents)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ArticleViewHolder(
        private val itemBinding: TopHeadlineItemLayoutBinding,
        private val adapterEvents: TopHeadlineListAdapterEvents
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(article: Article) {
            itemBinding.run {
                titleHeadline.text = article.title
                bindPhoto(article)
                cardLayout.setOnClickListener {
                    adapterEvents.onCardClick(article)
                }
            }
        }

        private fun TopHeadlineItemLayoutBinding.bindPhoto(article: Article) {
            if (article.hasImageUrl) {
                imageViewPhoto.show()
                Picasso.get()
                    .load(article.urlToImage)
                    .fit()
                    .centerCrop()
                    .placeholder(R.mipmap.empty_photo)
                    .error(R.mipmap.empty_photo)
                    .into(imageViewPhoto)
            } else {
                imageViewPhoto.hide()
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                adapterEvents: TopHeadlineListAdapterEvents
            ): ArticleViewHolder {
                val itemBinding = TopHeadlineItemLayoutBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return ArticleViewHolder(
                    itemBinding,
                    adapterEvents
                )
            }
        }

        interface TopHeadlineListAdapterEvents {
            fun onCardClick(article: Article)
        }
    }
}