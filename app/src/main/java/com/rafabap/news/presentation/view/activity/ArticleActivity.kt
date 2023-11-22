package com.rafabap.news.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rafabap.news.R
import com.rafabap.news.databinding.ActivityArticleBinding
import com.rafabap.news.domain.model.Article
import com.rafabap.news.common.util.extension.hide
import com.rafabap.news.common.util.extension.show
import com.squareup.picasso.Picasso

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        val view = binding.root
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        setContentView(view)
        setupView()
    }

    private fun setupView() {
        val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(PARAM_ARTICLE, Article::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(PARAM_ARTICLE) as Article
        }
        setupArticle(article)
        binding.toolbarBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupArticle(headlineArticle: Article?) {
        headlineArticle?.let { article ->
            binding.apply {
                titleHeadline.text = article.title
                setupArticleTitle(article)
                setupArticleDescription(article)
                setupArticleImage(article)
            }
        }
    }

    private fun ActivityArticleBinding.setupArticleImage(article: Article) {
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

    private fun ActivityArticleBinding.setupArticleDescription(article: Article) {
        if (article.hasContent) {
            contentHeadline.text = article.content
            contentHeadline.show()
        } else {
            contentHeadline.hide()
        }
    }

    private fun ActivityArticleBinding.setupArticleTitle(article: Article) {
        if (article.hasDescription) {
            descriptionHeadline.text = article.description
            descriptionHeadline.show()
        } else {
            descriptionHeadline.hide()
        }
    }

    companion object {
        private const val PARAM_ARTICLE = "PARAM_ARTICLE"

        fun newIntent(
            context: Context,
            article: Article
        ): Intent {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PARAM_ARTICLE, article)
            }
            return intent
        }
    }
}