package com.bytebyte6.rtmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;


public class dad {
    public static void ff() {
        List<Dict> lst = new ArrayList<>();
        lst.add(new Dict("1", "A"));
        lst.add(new Dict("2", "B"));
        lst.add(new Dict("1", "B"));
        lst.add(new Dict("2", "A"));
        lst.add(new Dict("3", "B"));
        lst.add(new Dict("3", "A"));
        HashMap<String, Integer> res =
                Observable.fromIterable(lst)
                        .groupBy(dict -> dict.getA() + "group")
                        .collect(HashMap<String, Single<Long>>::new, (m, e) -> m.put(e.getKey(), e.count()))
                        .flatMap((Function<HashMap<String, Single<Long>>, SingleSource<HashMap<String, Integer>>>) stringSingleHashMap -> {
                            HashMap<String, Integer> map = new HashMap<>();
                            stringSingleHashMap.forEach((k, v) -> map.put(k, v.blockingGet().intValue()));
                            return Single.just(map);
                        })
                        .blockingGet();
        System.out.println(res);
    }
}
