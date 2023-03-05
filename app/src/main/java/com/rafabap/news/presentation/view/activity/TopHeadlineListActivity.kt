package com.rafabap.news.presentation.view.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import com.rafabap.news.R
import com.rafabap.news.databinding.ActivityTopHeadlineListBinding
import com.rafabap.news.domain.model.Article
import com.rafabap.news.presentation.util.extension.hide
import com.rafabap.news.presentation.util.extension.show
import com.rafabap.news.presentation.view.Navigation
import com.rafabap.news.presentation.view.NetworkState
import com.rafabap.news.presentation.view.adapter.ArticleListAdapter
import com.rafabap.news.presentation.view.viewmodel.TopHeadlineViewModel
import com.rafabap.news.presentation.view.viewmodel.TopHeadlineViewModel.Companion.ERROR_LOADING_NEWS
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

class TopHeadlineListActivity : BaseActivity(),
                                ArticleListAdapter.ArticleViewHolder.TopHeadlineListAdapterEvents {

    private lateinit var binding: ActivityTopHeadlineListBinding
    private lateinit var articleListAdapter: ArticleListAdapter

    //Biometrics
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val viewModel: TopHeadlineViewModel by inject()
    private var loadingMore = false
    //private var source = "bbc-news"
    private var hasAuthenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadlineListBinding.inflate(layoutInflater)
        val view = binding.root
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        setContentView(view)
        setupView()
        initiateListeners()
        checkBiometrics()
    }

    private fun finishSetup() {
        hasAuthenticated = true
        initiateObservers()
        val source = getString(R.string.param_app_source)
        viewModel.loadTopHeadlineArticles(source)

        binding.run {
            appMessageScreen.hide()
            newsContent.show()

        }
    }

    private fun checkBiometrics() {
        val biometricManager = BiometricManager.from(this.applicationContext)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricAuthentication()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                finishSetup()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                finishSetup()
            }
            else -> {
                finishSetup()
            }
        }

    }

    private fun showBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this@TopHeadlineListActivity,
            executor,
            authenticationCallback())

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun authenticationCallback() = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(
            errorCode: Int, errString: CharSequence
        ) {
            super.onAuthenticationError(errorCode, errString)
            if (errorCode == BIOMETRICS_NOT_ENROLLED) {
                finishSetup()
            } else {
                val message = "${getString(R.string.message_biometrics_error)}: $errString"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                showAppMessageScreen(getString(R.string.message_biometrics_fail))
            }
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult
        ) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(applicationContext, getString(R.string.message_biometrics_success), Toast.LENGTH_SHORT).show()
            finishSetup()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(applicationContext, getString(R.string.message_biometrics_fail), Toast.LENGTH_SHORT).show()
            showAppMessageScreen(getString(R.string.message_biometrics_fail))
        }
    }

    override fun onCardClick(article: Article) {
        Navigation.openHeadline(
            this@TopHeadlineListActivity,
            article
        )
    }

    private fun initiateObservers() {
        observe(viewModel.networkState) {
            when (it?.status) {
                NetworkState.NetworkStateStatus.RUNNING -> {
                    startRefreshing()
                }
                NetworkState.NetworkStateStatus.SUCCESS -> {
                    stopRefreshing()
                }
                NetworkState.NetworkStateStatus.ERROR -> {
                    showMessage(it.detail)
                    stopRefreshing()
                }
                NetworkState.NetworkStateStatus.EMPTY -> {
                    showAppMessageScreen(getString(R.string.message_empty_news))
                    stopRefreshing()
                }
                else -> {
                    stopRefreshing()
                }
            }
        }
        observe(viewModel.articleList) {
            it?.let { articleList ->
                onReceiveArticles(articleList)
            }
        }
    }

    private fun showAppMessageScreen(message: String) {
        binding.run {
            newsContent.hide()
            appMessageScreen.text = message
            appMessageScreen.show()
        }
        stopRefreshing()
    }

    private fun onReceiveArticles(articleList: List<Article>) {
        val sourceName = viewModel.getSourceName()
        binding.sourceNameHeadline.text = sourceName
        articleListAdapter.submitList(articleList)
        binding.run {
            newsContent.show()
            appMessageScreen.hide()
        }
    }

    private fun showMessage(error: String?) {
        val message = when (error) {
            ERROR_LOADING_NEWS -> {
                getString(R.string.message_error_loading_news)
            }
            else               -> {
                getString(R.string.message_empty_news)
            }
        }
        showToast(message)
    }

    private fun startRefreshing() {
        binding.run {
            refreshSwipe.isEnabled = false
            refreshSwipe.isRefreshing = true
            loadingMore = true
        }
    }

    private fun stopRefreshing() {
        binding.run {
            refreshSwipe.isEnabled = true
            refreshSwipe.isRefreshing = false
            loadingMore = false
        }
    }

    private fun setupView() {
        articleListAdapter = ArticleListAdapter(this)
        binding.recyclerFeed.adapter = ConcatAdapter(articleListAdapter)
    }

    private fun initiateListeners() {
        binding.run {
            refreshSwipe.isEnabled = false
            refreshSwipe.setOnRefreshListener {
                if (hasAuthenticated) {
                    val source = getString(R.string.param_app_source)
                    viewModel.loadTopHeadlineArticles(source)
                } else {
                    checkBiometrics()
                }
            }
        }
    }

    private companion object {
        const val BIOMETRICS_NOT_ENROLLED = 11
    }
}