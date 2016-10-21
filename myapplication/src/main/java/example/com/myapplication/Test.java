package example.com.myapplication;

import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func2;

/**
 * Created by zhanghongqiang on 2016/10/13  上午10:21
 * ToDo:
 */
public class Test {
    public void main(String args){
        test();
    }
    private void test() {
        Observable<String> o1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("o1");
                subscriber.onNext("o2");
                subscriber.onNext("o3");
            }
        });
        Observable<String> o2 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("o4");
                subscriber.onNext("o5");
                subscriber.onNext("o6");
            }
        });
        Observable.combineLatest(o2, o1, new Func2<String, String, String>() {
            @Override public String call(String s, String s2) {
                Log.e("combine --- >", "s = " + s + " | s2 = " + s2);
                return s + s2;
            }
        }).subscribe(new Observer<String>() {
            @Override public void onCompleted() {
                Log.e("onCompleted --- >", "onCompleted");
            }

            @Override public void onError(Throwable e) {
                Log.e("onError --- >", "onError");

            }

            @Override public void onNext(String o) {
                Log.e("onNext --- >", o);
            }
        });
    }
}
