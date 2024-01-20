package org.liupack.wanandroid.model.usecase

import org.liupack.wanandroid.model.Repository

class CancelFavoriteArticleUseCase(private val repository: Repository) {

    operator fun invoke(id: Int) = repository.cancelFavoriteArticle(id)
}