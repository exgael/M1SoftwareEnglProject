package org.sudokusolver.File;

public class SudokuFileParser {

    private final ReadFileFromResources fileReader;
    private final ParseStringToIntArray parser;

    public SudokuFileParser() {
        this.fileReader = new ReadFileFromResources();
        this.parser = new ParseStringToIntArray();
    }

    public int[] parseFileTo1DArray(String fileName) {
        String content = fileReader.readFile(fileName);
        return parser.parseString(content);
    }

}

