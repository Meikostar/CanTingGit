package com.zhongchuang.canting.widget;

import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/***
 * 功能描述:事件总线
 * 作者chewei
 * 时间:2016-09-18
 * 版本:1.0
 ***/
public class RxBus {

    private static RxBus rxBus;
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
    List<Subscription> list = new ArrayList<>();


    private RxBus() {
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void send(final Object o) {
        if (Looper.myLooper()!= Looper.getMainLooper()){
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    subscriber.onNext(o);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            _bus.onNext(o);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            _bus.onNext(o);
                        }
                    });
        }else {
            _bus.onNext(o);
        }
    }

    public <T> Observable<T> toObserverable(final Class<T> type) {
        return _bus.ofType(type);
    }

    public void addSubscription(Subscription s){
        list.add(s);
    }

    public void unSub(Subscription s){
        if (s!=null && !s.isUnsubscribed()){
            s.unsubscribe();
        }
        if (list!=null && list.size()>0){
            list.remove(s);
        }
    }
}
