package com.tchl.server3.http.subscriber;

/**
 * Created by happen on 2017/12/18.
 */

public class HttpResult<T> {

    public boolean error;
    //@SerializedName(value = "results", alternate = {"result"})
    public T results;
}
