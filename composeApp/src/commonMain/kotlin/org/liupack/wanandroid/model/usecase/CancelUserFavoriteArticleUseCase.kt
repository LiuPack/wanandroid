package org.liupack.wanandroid.model.usecase

import org.liupack.wanandroid.model.Repository

class CancelUserFavoriteArticleUseCase(private val repository: Repository) {

    operator fun invoke(id: Int, originId: Int) = repository.cancelUserFavoriteArticle(id, originId)
}