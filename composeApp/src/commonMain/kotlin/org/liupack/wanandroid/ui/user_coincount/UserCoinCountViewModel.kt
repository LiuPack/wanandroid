package org.liupack.wanandroid.ui.user_coincount

import app.cash.paging.cachedIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.entity.UserCoinCountData

class UserCoinCountViewModel(private val repository: Repository) : ViewModel() {

    private val mUserCoinCountState = MutableStateFlow(UserCoinCountData.Empty)
    val userCoinCountState = mUserCoinCountState.asStateFlow()

    private val mToRankingState = MutableSharedFlow<Boolean>()
    val toRankingState = mToRankingState.asSharedFlow()

    fun dispatch(action: UserCoinCountAction) {
        when (action) {
            UserCoinCountAction.Refresh -> {
                userCoinCount()
            }

            UserCoinCountAction.ToRanking -> {
                toRanking()
            }
        }
    }

    private fun toRanking() {
        viewModelScope.launch {
            mToRankingState.emit(true)
        }
    }

    private fun userCoinCount() {
        viewModelScope.launch {
            repository.userCoinCount().catch {
                mUserCoinCountState.emit(UserCoinCountData.Empty)
            }.collectLatest {
                mUserCoinCountState.emit(it)
            }
        }
    }

    val userCoinCountList = repository.userCoinCountList().cachedIn(viewModelScope)
}
