package org.liupack.wanandroid.ui.main

import moe.tlaster.precompose.viewmodel.ViewModel
import org.liupack.wanandroid.model.entity.BottomNavigatorData

class MainViewModel : ViewModel() {

    val bottomNavigatorDataLists = listOf(
        BottomNavigatorData.Home,
        BottomNavigatorData.System,
        BottomNavigatorData.WechatAccount,
        BottomNavigatorData.Project,
        BottomNavigatorData.User
    )
}
