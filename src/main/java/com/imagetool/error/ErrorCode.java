package com.imagetool.error;

public enum ErrorCode {
    MISSING_ARGUMENT(1),
    UNRECOGNIZED_ARGUMENT(2),
    DUPLICATE_ARGUMENT(3),
    INPUT_FILE_MISSING(4),
    OUTPUT_FILE_UNWRITABLE(5),
    C_ARGUMENT_MISSING(6),
    C_ARGUMENT_INVALID(7),
    P_ARGUMENT_INVALID(8),
    R_ARGUMENT_INVALID(9);

    private final int code;
    ErrorCode(int code) { this.code = code; }
    public int getCode() { return code; }
}