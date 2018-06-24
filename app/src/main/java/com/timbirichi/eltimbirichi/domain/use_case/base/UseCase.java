package com.timbirichi.eltimbirichi.domain.use_case.base;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by JM on 12/5/2017.
 * Clase base para los caso de uso.. (Funcionalidades de la app)
 */

public abstract class UseCase<T> {
    CompositeDisposable compositeDisposable;
    Scheduler executorThread;
    Scheduler uiThread;

    public UseCase(Scheduler executorThread, Scheduler uiThread) {
        this.compositeDisposable = new CompositeDisposable();
        this.executorThread = executorThread;
        this.uiThread = uiThread;
    }

    public void execute(DisposableObserver<T> disposableObserver) {
        if (disposableObserver == null){
            throw new IllegalArgumentException("Disposable can't be null");
        }
        final Observable<T> observable =
                this.createObservableCaseUse()
                .subscribeOn(executorThread)
                .observeOn(uiThread);

        DisposableObserver observer = observable.subscribeWith(disposableObserver);

        compositeDisposable.add(observer);
    }

    public void dispose(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    protected abstract Observable<T> createObservableCaseUse();
}
