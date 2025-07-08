package com.ncnl.barangayapp.repository;

@FunctionalInterface
public interface ModalCommand<T> {
    T show();
}
