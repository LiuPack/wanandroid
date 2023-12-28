package org.liupack.wanandroid.ui.system

import org.liupack.wanandroid.model.entity.SystemBaseData

sealed class SystemAction {
    data object Init : SystemAction()
    data object Refresh : SystemAction()
    data class OnItemClick(val data: SystemBaseData) : SystemAction()
}