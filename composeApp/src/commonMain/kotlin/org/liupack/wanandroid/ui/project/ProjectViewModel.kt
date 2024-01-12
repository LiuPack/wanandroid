package org.liupack.wanandroid.ui.project

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.ProjectSortData

class ProjectViewModel(private val repository: Repository) : ViewModel() {

    private val mProjectSort = MutableStateFlow<List<ProjectSortData>>(emptyList())
    val projectSort = mProjectSort.asStateFlow()

    fun projectSort() {
        viewModelScope.launch {
            repository.projectSort().catch {
                mProjectSort.emit(emptyList())
            }.collectLatest {
                mProjectSort.emit(it)
            }
        }
    }

}
