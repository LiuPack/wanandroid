package org.liupack.wanandroid.ui.project

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.ProjectSortData

class ProjectViewModel(private val repository: Repository) : ScreenModel {

    private val mProjectSort = MutableStateFlow<List<ProjectSortData>>(emptyList())
    val projectSort = mProjectSort.asStateFlow()

    fun projectSort() {
        screenModelScope.launch {
            repository.projectSort().catch {
                mProjectSort.emit(emptyList())
            }.collectLatest {
                mProjectSort.emit(it)
            }
        }
    }


    override fun onDispose() {
        super.onDispose()
        screenModelScope.cancel()
    }
}
