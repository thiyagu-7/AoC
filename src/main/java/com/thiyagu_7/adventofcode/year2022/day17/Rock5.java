package com.thiyagu_7.adventofcode.year2022.day17;

class Rock5 implements Rock {
    private final char[][] rockStructure;
    private int row;
    private int column = 2;

    Rock5() {
        this.rockStructure = new char[][]{
                {'#', '#'},
                {'#', '#'}
        };
    }

    public int beginRock(char[][] chamber, int highestRock) {
        this.row = highestRock - 4 - 1;
        int tmpRow = row;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                chamber[tmpRow][column + j] = rockStructure[i][j];
            }
            tmpRow++;
        }
        return row;
    }

    public boolean moveRight(char[][] chamber) {
        int rightEndCol = column + 1;
        if (rightEndCol + 1 == chamber[0].length
                || chamber[row][rightEndCol + 1] != EMPTY
                || chamber[row + 1][rightEndCol + 1] != EMPTY) {
            return false;
        }
        for (int i = row; i < row + 2; i++) {
            chamber[i][rightEndCol + 1] = chamber[i][rightEndCol];
        }
        for (int i = row; i < row + 2; i++) {
            chamber[i][column] = EMPTY;
        }
        column++;
        return true;
    }

    public boolean moveLeft(char[][] chamber) {
        int rightMostCol = column + 1;
        if (column - 1 < 0
                || chamber[row][column - 1] != EMPTY
                || chamber[row + 1][column - 1] != EMPTY) {
            return false;
        }
        for (int i = row; i < row + 2; i++) {
            chamber[i][column - 1] = chamber[i][column];
        }
        for (int i = row; i < row + 2; i++) {
            chamber[i][rightMostCol] = EMPTY;
        }
        column--;
        return true;
    }

    public boolean moveDown(char[][] chamber) {
        int lowestRow = row + 1;
        if (lowestRow + 1 == chamber.length
                || chamber[lowestRow + 1][column] != EMPTY
                || chamber[lowestRow + 1][column + 1] != EMPTY) {
            return false;
        }
        for (int j = column; j < column + 2; j++) {
            chamber[lowestRow + 1][j] = chamber[lowestRow][j];
        }

        for (int j = column; j < column + 2; j++) {
            chamber[row][j] = EMPTY;
        }
        row++;
        return true;
    }

    @Override
    public int getRow() {
        return row;
    }
}
