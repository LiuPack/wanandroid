package org.liupack.wanandroid.ui.main

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Constants
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.BottomNavigatorData
import org.liupack.wanandroid.platform.settings
import org.liupack.wanandroid.router.Router

class MainViewModel(private val repository: Repository) : ViewModel() {

    val navigators = listOf(
        BottomNavigatorData.Home,
        BottomNavigatorData.System,
        BottomNavigatorData.WechatAccount,
        BottomNavigatorData.Project,
        BottomNavigatorData.User
    )
    val mainRoute = listOf(
        Router.Home.path,
        Router.System.path,
        Router.WechatAccount.path,
        Router.Project.path,
        Router.User.path
    )
    val isLogin get() = mutableStateOf(settings.getBooleanOrNull(Constants.isLogin) ?: false)

    private val mCurrentNavigator = MutableStateFlow<BottomNavigatorData>(BottomNavigatorData.Home)
    val currentNavigator = mCurrentNavigator.asStateFlow()

    fun updateSelectedNavigator(data: BottomNavigatorData) {
        viewModelScope.launch {
            mCurrentNavigator.update { data }
        }
    }
}
