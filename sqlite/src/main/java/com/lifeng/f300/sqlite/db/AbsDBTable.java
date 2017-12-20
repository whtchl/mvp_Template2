package com.lifeng.f300.sqlite.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by happen on 2017/8/22.
 */

public abstract class AbsDBTable<T> {

    public abstract String create();
    public abstract ContentValues to(T object);
    public abstract T from(Cursor cursor);

}
