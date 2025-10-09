package com.mms.controllers;

import com.mms.util.Logger;
import java.sql.SQLException;


public abstract class BaseController {
    protected void handleException(Exception e, String message) {
        Logger.log(e);
        throw new RuntimeException(message + ": " + e.getMessage(), e);
    }

    protected void validateNotNull(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    protected void validateNotEmpty(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
    }

    protected boolean isValidId(int id) {
        return id > 0;
    }

    protected <T> T executeWithErrorHandling(DatabaseOperation<T> operation, String errorMessage) {
        try {
            return operation.execute();
        } catch (SQLException e) {
            handleException(e, "Database error: " + errorMessage);
            return null; 
        } catch (Exception e) {
            handleException(e, errorMessage);
            return null;
        }
    }

    @FunctionalInterface
    protected interface DatabaseOperation<T> {
        T execute() throws Exception;
    }
}
