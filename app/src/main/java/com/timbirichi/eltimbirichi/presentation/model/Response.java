package com.timbirichi.eltimbirichi.presentation.model;

import android.support.annotation.Nullable;

public class Response <T>{
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    public Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

//    public Response loading() {
//        return new Response(LOADING, null, null);
//    }
//
//    public static Response success(@NonNull  T data) {
//        return new Response(SUCCESS, data, null);
//    }
//
//    public static Response error(@NonNull Throwable error) {
//        return new Response(ERROR, null, error);
//        }

}
