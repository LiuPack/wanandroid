package org.liupack.wanandroid.ui.user_shared.add_shared

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.liupack.wanandroid.common.Logger
import org.liupack.wanandroid.model.Repository
import org.liupack.wanandroid.model.UiState
import org.liupack.wanandroid.network.connect
import org.liupack.wanandroid.network.exception.DataResultException

class UserAddSharedViewModel(private val repository: Repository) : ViewModel() {

    private val channel = Channel<String>(capacity = Channel.Factory.UNLIMITED)
    private val mShareTitle = MutableStateFlow("")
    val shareTitle = mShareTitle.asStateFlow()
    private val mShareLink = MutableStateFlow("")
    val shareLink = mShareLink.asStateFlow()
    private val mSubmitShared = MutableSharedFlow<UiState<String>>()
    val submitShared = mSubmitShared.asSharedFlow()

    init {
        listenerInput()
    }

    fun dispatch(action: UserAddSharedAction) {
        viewModelScope.launch {
            when (action) {
                is UserAddSharedAction.InputLink -> {
                    inputLinkUpdate(action.link)
                }

                is UserAddSharedAction.InputTitle -> {
                    inputTitleUpdate(action.title)
                }

                is UserAddSharedAction.Shared -> {
                    submitShared(shareTitle.value, shareLink.value)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun listenerInput() {
        viewModelScope.launch {
            shareLink.flatMapMerge {
                Logger.i("数据监听：$it")
                channel.consumeAsFlow().flatMapMerge { url ->
                    runCatching {
                        val isUrl = linkRegexPattern.toRegex().matches(url)
                        if (isUrl) {
                            val result = connect().get(url).bodyAsText().let {
                                titleRegex.toRegex().find(it)?.groups?.get(1)?.value ?: ""
                            }
                            Logger.i("数据查询：$result")
                            flowOf(result)
                        } else {
                            emptyFlow()
                        }
                    }.getOrElse { emptyFlow() }
                }
            }.onEach { title ->
                Logger.i("数据结果：$title")
                mShareTitle.emit(title)
            }.launchIn(viewModelScope)
        }
    }

    private fun inputTitleUpdate(title: String) {
        viewModelScope.launch {
            mShareTitle.emit(title)
        }
    }

    private fun inputLinkUpdate(link: String) {
        viewModelScope.launch {
            mShareLink.emit(link)
            launch {
                channel.send(link)
            }
        }
    }

    private fun submitShared(title: String, link: String) {
        viewModelScope.launch {
            if (title.isEmpty()) {
                mSubmitShared.emit(UiState.Failed("请输入文章标题"))
                return@launch
            }
            if (link.isEmpty()) {
                mSubmitShared.emit(UiState.Failed("请输入文章链接"))
                return@launch
            }
            repository.userAddSharedArticle(title, link).onStart {
                mSubmitShared.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    mSubmitShared.emit(UiState.Failed(it.message))
                } else {
                    mSubmitShared.emit(UiState.Exception(it))
                }
            }.collectLatest {
                mSubmitShared.emit(UiState.Success("提交成功"))
            }
        }
    }

    override fun onCleared() {
        channel.close()
        super.onCleared()
    }

    companion object {
        private const val linkRegexPattern = "^(https?://)?" + // scheme
                "([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}" + // domain name
                "(/\\S*)?$"
        private const val titleRegex = "<title>(.*?)</title>"
    }
}