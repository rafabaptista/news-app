package com.rafabap.news.presentation.view

import android.content.Context
import com.rafabap.news.domain.model.Article
import com.rafabap.news.presentation.view.activity.ArticleActivity

class Navigation {
    companion object {
        @JvmStatic
        fun openHeadline(
            context: Context,
            article: Article
        ) {
            val intent = ArticleActivity.newIntent(context, article)
            context.startActivity(intent)
        }
    }
}