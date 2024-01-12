package org.liupack.wanandroid.ui.system

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.model.entity.SystemBaseData
import org.liupack.wanandroid.network.exception.DataResultException

class SystemViewModel(private val repository: Repository) : ViewModel() {

    private val mSystemBaseData = MutableStateFlow<UiState<List<SystemBaseData>>>(UiState.Loading)
    val systemBaseData = mSystemBaseData.asStateFlow()
    fun dispatch(action: SystemAction) {
        viewModelScope.launch {
            when (action) {
                is SystemAction.Init -> {
                    systemBaseData()
                }

                is SystemAction.Refresh -> {
                    refreshData()
                }

                is SystemAction.OnItemClick -> {}
            }
        }
    }

    private fun systemBaseData() {
        viewModelScope.launch {
            repository.systemBaseList().catch {
                if (it is DataResultException) {
                    mSystemBaseData.emit(UiState.Failed(it.message))
                } else {
                    mSystemBaseData.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mSystemBaseData.emit(UiState.Success(it))
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            repository.systemBaseList().onStart {
                mSystemBaseData.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mSystemBaseData.emit(UiState.Failed(it.message))
                } else {
                    mSystemBaseData.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mSystemBaseData.emit(UiState.Success(it))
            }
        }
    }
}
