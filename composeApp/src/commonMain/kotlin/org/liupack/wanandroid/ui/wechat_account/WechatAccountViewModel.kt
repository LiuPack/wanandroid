package org.liupack.wanandroid.ui.wechat_account

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
import org.liupack.wanandroid.model.entity.WechatAccountSortData

class WechatAccountViewModel(private val repository: Repository) : ViewModel() {
    private val mWechatAccountList =
        MutableStateFlow<UiState<List<WechatAccountSortData>>>(UiState.Loading)
    val wechatAccountList = mWechatAccountList.asStateFlow()

    private val mSelectIndex = MutableStateFlow(0)
    val selectedIndex = mSelectIndex.asStateFlow()

    fun updateSelected(index: Int) {
        viewModelScope.launch {
            mSelectIndex.update { index }
        }
    }

    fun getWechatAccountList() {
        viewModelScope.launch {
            repository.wechatAccountSort().catch {
                mWechatAccountList.emit(UiState.Exception(it))
            }.collectLatest {
                mWechatAccountList.emit(UiState.Success(it))
            }
        }
    }
}
