package org.liupack.wanandroid.model.usecase

import org.liupack.wanandroid.model.Repository

class FavoriteArticleUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Int) = repository.favoriteArticle(id)
}