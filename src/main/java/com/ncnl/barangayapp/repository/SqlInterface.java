package com.ncnl.barangayapp.repository;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlInterface<T> {
    T apply(Connection conn) throws SQLException;
}
