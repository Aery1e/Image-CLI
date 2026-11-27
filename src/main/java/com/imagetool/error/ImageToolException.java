package com.imagetool.error;

public class ImageToolException extends Exception {
    private final ErrorCode code;

    public ImageToolException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() { return code; }
}