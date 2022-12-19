package com.thiyagu_7.adventofcode.year2022.day17;

class Rock3 implements Rock {
    private final char[][] rockStructure;
    private int row;
    private int column = 2;

    Rock3() {
        this.rockStructure = new char[][]{
                {'.', '.', '#'},
                {'.', '.', '#'},
                {'#', '#', '#'}
        };
    }
    public int beginRock(char[][] chamber, int highestRock) {
        this.row = highestRock - 4 - 2;
        int tmpRow = row;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                chamber[tmpRow][column + j] = rockStructure[i][j];
            }
            tmpRow++;
        }
        return row;
    }
    public boolean moveRight(char[][] chamber) {
        int rightEndCol = column + 2;
        if (rightEndCol + 1 == chamber[0].length
                || chamber[row][rightEndCol + 1] != EMPTY
                || chamber[row + 1][rightEndCol + 1] != EMPTY
                || chamber[row + 2][rightEndCol + 1] != EMPTY) {
            return false;
        }
        chamber[row][rightEndCol + 1] = '#';
        chamber[row + 1][rightEndCol + 1] = '#';
        chamber[row + 2][rightEndCol + 1] = '#';

        chamber[row][rightEndCol] = EMPTY;
        chamber[row + 1][rightEndCol] = EMPTY;
        chamber[row + 2][column] = EMPTY;
        column++;
        return true;
    }

    public boolean moveLeft(char[][] chamber) {
        int leftEndCol = column;
        int rightEndCol = column + 2;
        if (leftEndCol - 1 < 0
                || chamber[row][column + 1] != EMPTY
                || chamber[row + 1][column + 1] != EMPTY
                || chamber[row + 2][leftEndCol - 1] != EMPTY) {
            return false;
        }

        chamber[row][column + 1] = '#';
        chamber[row + 1][column + 1] = '#';
        chamber[row + 2][leftEndCol - 1] = '#';

        chamber[row][rightEndCol] = EMPTY;
        chamber[row + 1][rightEndCol] = EMPTY;
        chamber[row + 2][rightEndCol] = EMPTY;
        column--;
        return true;
    }

    public boolean moveDown(char[][] chamber) {
        int bottomRow = row + 2;
        int rightEndCol = column + 2;
        if (bottomRow + 1 == chamber.length
                || chamber[bottomRow + 1][column] != EMPTY
                || chamber[bottomRow + 1][column + 1] != EMPTY
                || chamber[bottomRow + 1][column + 2] != EMPTY) {
            return false;
        }

        chamber[bottomRow + 1][column] = '#';
        chamber[bottomRow + 1][column + 1] = '#';
        chamber[bottomRow + 1][column + 2] = '#';

        chamber[row][rightEndCol] = EMPTY;
        chamber[row + 2][column] = EMPTY;
        chamber[row + 2][column + 1] = EMPTY;
        row++;
        return true;
    }
    @Override
    public int getRow() {
        return row;
    }
}
