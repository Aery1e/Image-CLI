package com.imagetool.cli;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;

public class CliParser {
    public static CliOptions parse(String[] args) throws ImageToolException {
        CliOptions o = new CliOptions();
        int iCount = 0, oCount = 0, cCount = 0, pCount = 0, rCount = 0;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.length() != 2 || arg.charAt(0) != '-') {
                throw new ImageToolException(ErrorCode.UNRECOGNIZED_ARGUMENT, "Unknown arg: " + arg);
            }
            if (i + 1 >= args.length) {
                throw new ImageToolException(ErrorCode.MISSING_ARGUMENT, "Missing value for " + arg);
            }
            String val = args[++i];
            switch (arg.charAt(1)) {
                case 'i': o.inputFile = val; iCount++; break;
                case 'o': o.outputFile = val; oCount++; break;
                case 'c': o.copySpec = val; o.hasCopy = true; cCount++; break;
                case 'p': o.pasteSpec = val; o.hasPaste = true; pCount++; break;
                case 'r': o.renderSpec = val; o.hasRender = true; rCount++; break;
                default:
                    throw new ImageToolException(ErrorCode.UNRECOGNIZED_ARGUMENT, "Unknown flag: " + arg);
            }
        }

        if (iCount > 1 || oCount > 1 || cCount > 1 || pCount > 1 || rCount > 1) {
            throw new ImageToolException(ErrorCode.DUPLICATE_ARGUMENT, "Duplicate flag");
        }
        if (iCount == 0 || oCount == 0) {
            throw new ImageToolException(ErrorCode.MISSING_ARGUMENT, "-i and -o are required");
        }
        if (o.hasPaste && !o.hasCopy) {
            throw new ImageToolException(ErrorCode.C_ARGUMENT_MISSING, "-p requires -c");
        }
        return o;
    }
}