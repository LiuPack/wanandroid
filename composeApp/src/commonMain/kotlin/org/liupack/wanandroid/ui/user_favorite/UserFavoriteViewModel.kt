package org.liupack.wanandroid.ui.user_favorite

import androidx.paging.cachedIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class UserFavoriteViewModel(private val repository: Repository) : ViewModel() {

    val favoriteArticles = repository.userFavoriteArticles().cachedIn(viewModelScope)
}