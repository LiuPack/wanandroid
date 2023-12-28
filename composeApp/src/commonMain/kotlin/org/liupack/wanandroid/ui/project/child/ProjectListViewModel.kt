package org.liupack.wanandroid.ui.project.child

import androidx.compose.foundation.lazy.LazyListState
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.cancel
import org.liupack.wanandroid.model.Repository

class ProjectListViewModel(private val id: Int, private val repository: Repository) : ScreenModel {

    val lazyListState = LazyListState()

//    private val mProjectList = MutableStateFlow<PagingData<HomeArticleItemData>>(PagingData.empty())
//    val projectList = mProjectList.asStateFlow()
//
//    fun projectList(id: Int) {
//        screenModelScope.launch {
//            repository.projectListFromSort(id).cachedIn(screenModelScope).onEach {
//                mProjectList.emit(it)
//            }.stateIn(screenModelScope)
//        }
//    }

    val projectList = repository.projectListFromSort(id).cachedIn(screenModelScope)

    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}
