package org.liupack.wanandroid

object ActivityManager {

    lateinit var activity: AppActivity

    fun initActivity(activity: AppActivity) {
        this.activity = activity
    }
}