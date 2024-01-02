package org.liupack.wanandroid.ui.user_coincount

sealed class UserCoinCountAction {
    data object Refresh : UserCoinCountAction()
    data object ToRanking : UserCoinCountAction()
}