package org.liupack.wanandroid.network.exception

import okio.IOException

class LoginExpiredException(val code: Int, override val message: String) : IOException(message)