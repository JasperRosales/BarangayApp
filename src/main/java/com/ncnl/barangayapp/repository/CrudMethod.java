package com.ncnl.barangayapp.repository;

import java.util.Optional;
import java.util.UUID;

public interface CrudMethod<T> {
    Optional<T> create(T object);
    Optional<T> read(UUID id);
    boolean update(UUID id, T newData);
    boolean delete(UUID id);
}
