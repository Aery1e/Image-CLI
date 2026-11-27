package com.imagetool;

import com.imagetool.cli.CliOptions;
import com.imagetool.cli.CliParser;
import com.imagetool.error.ErrorCode;
import com.imagetool.error.ImageToolException;
import com.imagetool.image.Image;
import com.imagetool.image.ImageIoReader;
import com.imagetool.image.ImageIoWriter;
import com.imagetool.image.PpmReader;
import com.imagetool.image.PpmWriter;
import com.imagetool.image.SbuReader;
import com.imagetool.image.SbuWriter;
import com.imagetool.ops.CopyOperation;
import com.imagetool.ops.PasteOperation;
import com.imagetool.ops.TextRenderOperation;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            run(args);
            System.exit(0);
        } catch (ImageToolException e) {
            System.err.println(e.getMessage());
            System.exit(e.getCode().getCode());
        }
    }

    static void run(String[] args) throws ImageToolException {
        CliOptions opts = CliParser.parse(args);

        if (!validExt(opts.inputFile)) {
            throw new ImageToolException(
                    ErrorCode.INPUT_FILE_MISSING, "Input must be .ppm, .sbu, .png, .jpg, or .jpeg");
        }
        if (!new File(opts.inputFile).canRead()) {
            throw new ImageToolException(ErrorCode.INPUT_FILE_MISSING, "Cannot read: " + opts.inputFile);
        }
        if (!validExt(opts.outputFile)) {
            throw new ImageToolException(
                    ErrorCode.OUTPUT_FILE_UNWRITABLE, "Output must be .ppm, .sbu, .png, .jpg, or .jpeg");
        }
        File out = new File(opts.outputFile);
        if (out.getParentFile() != null && !out.getParentFile().exists()) {
            throw new ImageToolException(ErrorCode.OUTPUT_FILE_UNWRITABLE, "Output directory missing");
        }
        if (out.exists() && !out.canWrite()) {
            throw new ImageToolException(ErrorCode.OUTPUT_FILE_UNWRITABLE, "Cannot write: " + opts.outputFile);
        }

        Image img;
        if (opts.inputFile.endsWith(".sbu")) {
            img = SbuReader.read(opts.inputFile);
        } else if (opts.inputFile.endsWith(".ppm")) {
            img = PpmReader.read(opts.inputFile);
        } else {
            img = ImageIoReader.read(opts.inputFile);
        }

        int[][] copyBuf = null;
        if (opts.hasCopy) copyBuf = CopyOperation.copy(img, opts.copySpec);
        if (opts.hasPaste) PasteOperation.paste(img, opts.pasteSpec, copyBuf);
        if (opts.hasRender) TextRenderOperation.render(img, opts.renderSpec);

        if (opts.outputFile.endsWith(".sbu")) {
            SbuWriter.write(img, opts.outputFile);
        } else if (opts.outputFile.endsWith(".ppm")) {
            PpmWriter.write(img, opts.outputFile);
        } else {
            String formatName = opts.outputFile.endsWith(".png") ? "png" : "jpeg";
            ImageIoWriter.write(img, opts.outputFile, formatName);
        }
    }

    private static boolean validExt(String p) {
        return p != null && (p.endsWith(".ppm") || p.endsWith(".sbu")
                || p.endsWith(".png") || p.endsWith(".jpg") || p.endsWith(".jpeg"));
    }
}