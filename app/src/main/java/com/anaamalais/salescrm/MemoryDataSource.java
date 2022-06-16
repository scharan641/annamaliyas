package com.anaamalais.salescrm;

import io.reactivex.Observable;

public class MemoryDataSource {
    private Data data;

    public io.reactivex.Observable<Data> getData() {
        return Observable.create(emitter -> {
            if (data != null) {
                emitter.onNext(data);
            }
            emitter.onComplete();
        });
    }

    public void cacheInMemory(Data data) {
        this.data = data.clone();
        this.data.name = "memory";
    }
}
