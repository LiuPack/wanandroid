package org.liupack.wanandroid.ui.project

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.ProjectSortData
import org.liupack.wanandroid.network.exception.DataResultException

class ProjectViewModel(private val repository: Repository) : ViewModel() {

    private val mProjectSort = MutableStateFlow<UiState<List<ProjectSortData>>>(UiState.Loading)
    val projectSort = mProjectSort.asStateFlow()

    private val mSelectIndex = MutableStateFlow(0)
    val selectIndex = mSelectIndex.asStateFlow()

    fun updateSelected(index: Int) {
        mSelectIndex.update { index }
    }

    fun projectSort() {
        viewModelScope.launch {
            repository.projectSort().catch {
                if (it is DataResultException) {
                    mProjectSort.emit(UiState.Failed(it.message))
                } else {
                    mProjectSort.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mProjectSort.emit(UiState.Success(it))
            }
        }
    }

}
