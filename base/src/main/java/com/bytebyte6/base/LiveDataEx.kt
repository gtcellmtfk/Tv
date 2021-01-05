package com.bytebyte6.base

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations


//fun <I, O> LiveData<I>.map(f: ((I, O) -> Unit)): LiveData<O> {
//    return Transformations.map(this, object : Function)
//}
//
//public inline fun <X, Y> LiveData<X>.map(crossinline transform: (X) -> Y): LiveData<Y> =
//    Transformations.map(this) { transform(it) }