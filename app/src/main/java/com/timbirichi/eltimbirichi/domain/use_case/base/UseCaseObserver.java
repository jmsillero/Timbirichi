package com.timbirichi.eltimbirichi.domain.use_case.base;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by JM on 12/5/2017.
 * Clase base para los observer de cada caso de uso.
 */

public abstract class UseCaseObserver<T> extends DisposableObserver<T>{
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
