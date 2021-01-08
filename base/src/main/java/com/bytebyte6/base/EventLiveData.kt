package com.bytebyte6.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EventLiveData<T> {
    private val liveData = MutableLiveData<Event<T>>()

    fun setEventValue(value: T) {
        liveData.value = Event(value)
    }

    fun postEventValue(value: T) {
        liveData.postValue(Event(value))
    }

    fun liveData(): LiveData<Event<T>> {
        return liveData
    }

    fun getValue(): Event<T>? {
        return liveData.value
    }
}
