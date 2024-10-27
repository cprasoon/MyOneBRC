package org.demoapps;

public class MyRuntimeException extends RuntimeException {
    public MyRuntimeException(String message) {
        super(message);
    }

    public MyRuntimeException(Exception e) {
        super(e);
    }
}
