package com.grupo5.carwashapp.interfaces;

public interface RepositoryCallBack<T> {
    void onSuccess(T result);

    void onFailure(Exception e);
}
