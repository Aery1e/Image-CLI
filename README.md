# Image CLI

A Java command-line tool for converting images and applying lightweight edits. It supports P3 PPM, PNG, JPEG, and a project-specific SBU format, along with region copy/paste and bitmap-font text rendering.

## Features

- Convert between P3 PPM, PNG, JPEG, and SBU files.
- Copy a rectangular region and paste it at another location.
- Render text from a user-provided bitmap font.
- Report command-line and file errors with defined exit codes.

## Supported formats

| Format | Extensions | Notes |
|---|---|---|
| PPM | `.ppm` | ASCII P3. Input values are expected on a 0–255 scale; comments are not supported. |
| PNG | `.png` | Lossless RGB output. Transparent input pixels are composited onto white. |
| JPEG | `.jpg`, `.jpeg` | Lossy RGB format; color values may change during encoding. |
| SBU | `.sbu` | Project-specific palette and run-length encoding format, retained as a secondary format. |

The image model is RGB-only, so all output files are opaque. The output format is selected from the output filename extension.

## Requirements

- JDK 17 or later
- Apache Maven

PNG and JPEG support use Java's built-in Image I/O APIs. JUnit 5 is used for tests.

## Build and test

Run these commands from the project root:

```shell
mvn clean package
mvn test
```

The packaged application is created at `target/java-image-cli-1.0.0.jar`.

## Usage

Run the packaged application by specifying its main class:

```shell
java -cp target/java-image-cli-1.0.0.jar com.imagetool.Main -i input.png -o output.jpg
```

### Convert an image

The input and output extensions determine the formats:

```shell
java -cp target/java-image-cli-1.0.0.jar com.imagetool.Main -i input.ppm -o output.png
java -cp target/java-image-cli-1.0.0.jar com.imagetool.Main -i input.png -o output.jpg
```

### Copy and paste a region

Coordinates are zero-based and specified as `row,column`. Copy dimensions are `width,height`; regions are clipped at image boundaries.

```shell
java -cp target/java-image-cli-1.0.0.jar com.imagetool.Main -i input.png -o edited.png -c "0,0,100,80" -p "20,30"
```

`-p` requires `-c`.

### Render text

```shell
java -cp target/java-image-cli-1.0.0.jar com.imagetool.Main -i input.png -o labeled.png -r '"HELLO","font.txt",2,10,20'
```

The render specification is `message,fontPath,size,row,column`. Text is drawn in white. The font file must contain 26 horizontally arranged glyphs (`A`–`Z`), separated by columns with no `*` pixels; `*` marks a filled pixel. Lowercase letters are supported, while other characters render as spaces.

## Command-line options

| Option | Value | Description |
|---|---|---|
| `-i` | File path | Input image (required). |
| `-o` | File path | Output image (required). |
| `-c` | `row,column,width,height` | Copy a rectangular region. |
| `-p` | `row,column` | Paste the copied region. Requires `-c`. |
| `-r` | `message,fontPath,size,row,column` | Render text using a bitmap font. |

Operations run in this order: copy, paste, then text rendering.

## Exit codes

| Code | Meaning |
|---:|---|
| 1 | Missing required argument or value |
| 2 | Unrecognized argument |
| 3 | Duplicate argument |
| 4 | Input file missing or unreadable |
| 5 | Output file unwritable |
| 6 | Paste requested without copy |
| 7 | Invalid copy specification |
| 8 | Invalid paste specification |
| 9 | Invalid text-render specification |

## Project structure

```text
src/
  main/java/com/imagetool/
    cli/       Command-line parsing
    error/     Application errors and exit codes
    font/      Bitmap-font loading
    image/     Image model and format readers/writers
    ops/       Copy, paste, and text rendering
  test/java/   Unit and format round-trip tests
```
