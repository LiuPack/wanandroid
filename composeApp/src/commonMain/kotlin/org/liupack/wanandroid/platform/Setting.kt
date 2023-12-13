package org.liupack.wanandroid.platform

import com.russhwolf.settings.Settings

val settings = createSetting()

/**
 * 键值对本地缓存
 *
 * @return
 */
expect fun createSetting(): Settings