package com.rafabap.news.presentation.di

import com.rafabap.news.data.mapper.ArticleMapper
import com.rafabap.news.data.network.api.BaseApi
import com.rafabap.news.data.network.api.IBaseApi
import com.rafabap.news.data.network.api.NewsApi
import com.rafabap.news.data.repository.ITopHeadlineRepository
import com.rafabap.news.data.repository.TopHeadlineRepository
import com.rafabap.news.domain.usecase.TopHeadlineUseCase
import com.rafabap.news.presentation.view.viewmodel.TopHeadlineViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModules = module() {
    factory { TopHeadlineViewModel(get()) }
}

val serviceModules = module() {
    single { BaseApi() as IBaseApi }
    single {
        NewsApi(get())
    }
    single {
        TopHeadlineRepository(get()) as ITopHeadlineRepository
    }
}

val mapperModules = module() {
    single {
        ArticleMapper()
    }
}

val useCaseModules = module() {
    single {
        TopHeadlineUseCase(get(), get())
    }
}

val appComponent: List<Module> = listOf(
    viewModelModules,
    useCaseModules,
    serviceModules,
    mapperModules
)