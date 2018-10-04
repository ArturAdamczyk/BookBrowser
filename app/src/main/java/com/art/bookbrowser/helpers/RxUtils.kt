package com.art.bookbrowser.helpers

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RxUtils @Inject constructor(){

    fun <T> Observable<T>.baseCall(): Observable<T> {
        return this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> Single<T>.baseCall(): Single<T> {
        return this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> Single<T>.backgroundCall(): Single<T> {
        return this
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
    }
}