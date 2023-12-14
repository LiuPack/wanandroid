package org.liupack.wanandroid.ui.home

sealed class HomeAction {

    data object Refresh : HomeAction()

    data object OpenGithub : HomeAction()
    data class OpenBanner(val url: String) : HomeAction()
}