package com.applicake.beanstalkclient.tasks;

public interface ResponseHandler<T> {
  public void onResult(T result);
}
