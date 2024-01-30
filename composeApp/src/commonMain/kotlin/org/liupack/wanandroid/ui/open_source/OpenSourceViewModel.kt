package org.liupack.wanandroid.ui.open_source

import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import io.ktor.utils.io.core.String
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.jetbrains.compose.resources.resource
import org.liupack.wanandroid.model.UiState

class OpenSourceViewModel : ViewModel() {

    private val mLibraries = MutableStateFlow<UiState<Map<String, List<Library>>>>(UiState.Loading)
    val libraries = mLibraries.asStateFlow()

    init {
        loadLibraries()
    }

    private fun loadLibraries() = flow {
        val data = resource("aboutlibraries.json").readBytes()
            .run { String(this) }
        val result = Libs.Builder().withJson(data).build()
        val libs = result.libraries.groupBy { it.name.first().uppercase() }
        emit(libs)
    }.flowOn(Dispatchers.IO).onEach {
        delay(500)
        mLibraries.emit(UiState.Success(it))
    }.catch {
        mLibraries.emit(UiState.Exception(it))
    }.launchIn(viewModelScope)
}