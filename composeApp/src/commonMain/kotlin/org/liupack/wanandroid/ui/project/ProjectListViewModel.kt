package org.liupack.wanandroid.ui.project

import androidx.paging.cachedIn
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.HomeArticleItemData

class ProjectListViewModel(private val repository: Repository) : ViewModel() {

    private val mProjectList = MutableStateFlow<PagingData<HomeArticleItemData>>(PagingData.empty())
    val projectList = mProjectList.asStateFlow()

    fun projectList(id: Int) {
        viewModelScope.launch {
            repository.projectListFromSort(id).cachedIn(viewModelScope).onStart {
                mProjectList.emit(PagingData.empty())
            }.collectLatest {
                mProjectList.emit(it)
            }
        }
    }

}
