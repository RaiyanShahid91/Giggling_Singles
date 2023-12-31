package com.dil.singles.helper

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.d("", "Muiltiple observer registering but only one will be notified of changes")
        }
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }

    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    @MainThread
    override fun postValue(value: T?) {
        mPending.set(true)
        super.postValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }

//    companion object {
//        private val TAG = "SingleLiveEvent"
//    }
}