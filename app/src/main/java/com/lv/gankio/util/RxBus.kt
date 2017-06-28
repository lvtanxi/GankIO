package com.lv.gankio.util

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

/**
 * Date: 2017-06-26
 * Time: 17:53
 * Description:
 */
object RxBus {
    private val mBus: Subject<Any,Any> = PublishSubject.create()

    fun <T> toObservable(clzz: Class<T>): Observable<T> = mBus.ofType(clzz)

    fun toObservable(): Observable<Any> = mBus

    fun post(obj: Any) {
        mBus.onNext(obj)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }
}