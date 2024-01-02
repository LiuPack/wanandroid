package org.liupack.wanandroid.ui.coin_count_ranking

import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import org.liupack.wanandroid.model.Repository

class CoinCountRankingViewModel(
    repository: Repository
) : ScreenModel {

    val coinCountRankingState = repository.coinCountRanking().cachedIn(screenModelScope)

}
