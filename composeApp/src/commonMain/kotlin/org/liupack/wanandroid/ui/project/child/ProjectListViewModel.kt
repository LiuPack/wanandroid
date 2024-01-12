package org.liupack.wanandroid.ui.project.child

import androidx.paging.cachedIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class ProjectListViewModel(private val id: Int, private val repository: Repository) : ViewModel() {

    val projectList = repository.projectListFromSort(id).cachedIn(viewModelScope)

}
