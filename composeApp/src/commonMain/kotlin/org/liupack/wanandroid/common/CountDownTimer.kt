package org.liupack.wanandroid.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun rememberCountDownTimer(
    duration: Int,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): CountDownTimer {
    return remember {
        CountDownTimer(duration, coroutineScope)
    }
}

class CountDownTimer(private val duration: Int, private val coroutineScope: CoroutineScope) {
    private val mCurrentTime = MutableStateFlow(duration)
    val currentTimer = mCurrentTime.asStateFlow()
    private var mJob: Job? = null

    fun start() {
        mJob?.cancel()
        mJob = coroutineScope.launch {
            for (time in duration downTo 0) {
                mCurrentTime.value = time
                delay(1000)
            }
        }
    }

    fun stop() {
        mJob?.cancel()
    }
}