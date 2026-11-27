package com.imagetool.cli;

import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CliParserTest {
    @Test
    void parsesBasicArgs() throws Exception {
        CliOptions o = CliParser.parse(new String[]{"-i", "in.ppm", "-o", "out.ppm"});
        assertEquals("in.ppm", o.inputFile);
        assertEquals("out.ppm", o.outputFile);
    }

    @Test
    void missingRequired() {
        ImageToolException e = assertThrows(ImageToolException.class,
                () -> CliParser.parse(new String[]{"-i", "in.ppm"}));
        assertEquals(ErrorCode.MISSING_ARGUMENT, e.getCode());
    }

    @Test
    void duplicateFlag() {
        ImageToolException e = assertThrows(ImageToolException.class,
                () -> CliParser.parse(new String[]{"-i", "a.ppm", "-i", "b.ppm", "-o", "c.ppm"}));
        assertEquals(ErrorCode.DUPLICATE_ARGUMENT, e.getCode());
    }

    @Test
    void pasteRequiresCopy() {
        ImageToolException e = assertThrows(ImageToolException.class,
                () -> CliParser.parse(new String[]{"-i", "a.ppm", "-o", "b.ppm", "-p", "1,2"}));
        assertEquals(ErrorCode.C_ARGUMENT_MISSING, e.getCode());
    }
}