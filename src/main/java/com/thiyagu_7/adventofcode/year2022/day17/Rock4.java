package com.thiyagu_7.adventofcode.year2022.day17;

class Rock4 implements Rock {
    private final char[][] rockStructure;
    private int row;
    private int column = 2;

    Rock4() {
        this.rockStructure = new char[][]{
                {'#'},
                {'#'},
                {'#'},
                {'#'}
        };
    }
    @Override
    public int beginRock(char[][] chamber, int highestRock) {
        this.row = highestRock - 4 - 3;
        int tmpRow = row;
        for (int i = 0; i < 4; i++) {
            chamber[tmpRow][column] = rockStructure[i][0];
            tmpRow++;
        }
        return row;
    }
    @Override
    public boolean moveRight(char[][] chamber) {
        if (column + 1 == chamber[0].length
                || chamber[row][column + 1] != EMPTY
                || chamber[row + 1][column + 1] != EMPTY
                || chamber[row + 2][column + 1] != EMPTY
                || chamber[row + 3][column + 1] != EMPTY) {
            return false;
        }
        for (int i = row; i < row + 4; i++) {
            chamber[i][column + 1] = chamber[i][column];
            chamber[i][column] = EMPTY;
        }
        column++;
        return true;
    }

    @Override
    public boolean moveLeft(char[][] chamber) {
        if (column - 1 < 0
                || chamber[row][column - 1] != EMPTY
                || chamber[row + 1][column - 1] != EMPTY
                || chamber[row + 2][column - 1] != EMPTY
                || chamber[row + 3][column - 1] != EMPTY) {
            return false;
        }
        for (int i = row; i < row + 4; i++) {
            chamber[i][column - 1] = chamber[i][column];
            chamber[i][column] = EMPTY;
        }
        column--;
        return true;
    }

    @Override
    public boolean moveDown(char[][] chamber) {
        int lowestRow = row + 3;
        if (lowestRow + 1 == chamber.length || chamber[lowestRow + 1][column] != EMPTY) {
            return false;
        }
        chamber[lowestRow + 1][column] = '#';
        chamber[row][column] = EMPTY;
        row++;
        return true;
    }
    @Override
    public int getRow() {
        return row;
    }
}
