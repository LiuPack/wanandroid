package org.liupack.wanandroid.ui.coin_count_ranking

import androidx.paging.cachedIn
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.model.Repository

class CoinCountRankingViewModel(
    repository: Repository
) : ViewModel() {

    val coinCountRankingState = repository.coinCountRanking().cachedIn(viewModelScope)

}
