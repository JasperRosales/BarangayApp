package com.ncnl.barangayapp.repository;


@FunctionalInterface
public interface DatabaseCommand<T> {
    T execute();
}
