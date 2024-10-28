package com.letianpai.robot.desktop.callback

class LifecycleChangedCallback private constructor() {
    private val mListenerList: MutableList<LifecycleChangedListener?> = ArrayList()

    private object LifecycleChangedCallbackHolder {
        val instance: LifecycleChangedCallback = LifecycleChangedCallback()
    }

    fun interface LifecycleChangedListener {
        fun onLifecycleChange(lifecycle: String)
    }

    fun addLifecycleChangedListener(listener: LifecycleChangedListener?) {
        mListenerList.add(listener)
    }

    fun removeLifecycleChangedListener(listener: LifecycleChangedListener?) {
        mListenerList.remove(listener)
    }

    fun setLifecycle(lifecycle: String) {
        for (i in mListenerList.indices) {
            if (mListenerList[i] != null) {
                mListenerList[i]!!.onLifecycleChange(lifecycle)
            }
        }
    }

    companion object {
        const val ON_PAUSE: String = "onPause"
        const val ON_RESUME: String = "onResume"
        const val ON_DESTROY: String = "onDestroy"
        const val ON_CREATE: String = "onCreate"

        val instance: LifecycleChangedCallback
            get() = LifecycleChangedCallbackHolder.instance
    }
}
