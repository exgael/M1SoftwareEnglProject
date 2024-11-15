package org.sudokusolver.Gameplay.Reader;

public class SudokuFileReader implements SudokuReader {
    private final FileReader fileReader;
    private final SudokuStringReader parser;

    public SudokuFileReader() {
        this.fileReader = new FileReader();
        this.parser = new SudokuStringReader();
    }

    @Override
    public int[] readGridFrom(String absolutePath) throws RuntimeException{
        String content = fileReader.readFile(absolutePath);
        return parser.readGridFrom(content);
    }
}

