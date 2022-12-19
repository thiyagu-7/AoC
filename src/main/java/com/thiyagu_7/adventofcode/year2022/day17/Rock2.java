package com.thiyagu_7.adventofcode.year2022.day17;

class Rock2 implements Rock {
    private final char[][] rockStructure;
    private int row;
    private int column = 2;

    Rock2() {
        this.rockStructure = new char[][]{
                {'.', '#', '.'},
                {'#', '#', '#'},
                {'.', '#', '.'}
        };
    }

    public int beginRock(char[][] chamber, int highestRock) {
        this.row = highestRock - 4 - 2;
        int rowTmp = row;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                chamber[rowTmp][column + j] = rockStructure[i][j];
            }
            rowTmp++;
        }
        return row;
    }

    public boolean moveRight(char[][] chamber) {
        int rightEndCol = column + 2;
        if (rightEndCol + 1 == chamber[0].length
                || chamber[row][rightEndCol] != EMPTY
                || chamber[row + 1][rightEndCol + 1] != EMPTY
                || chamber[row + 2][rightEndCol] != EMPTY) {
            return false;
        }
        chamber[row + 1][rightEndCol + 1] = '#';
        chamber[row][rightEndCol] = '#';
        chamber[row + 2][rightEndCol] = '#';

        chamber[row][column + 1] = EMPTY;
        chamber[row + 1][column] = EMPTY;
        chamber[row + 2][column + 1] = EMPTY;

        column++;
        return true;
    }

    public boolean moveLeft(char[][] chamber) {
        int leftEndCol = column;
        int rightEndCol = column + 2;
        if (leftEndCol - 1 < 0
                || chamber[row][column] != EMPTY
                || chamber[row + 1][leftEndCol - 1] != EMPTY
                || chamber[row + 2][column] != EMPTY) {
            return false;
        }

        chamber[row + 1][leftEndCol - 1] = '#';
        chamber[row][column] = '#';
        chamber[row + 2][column] = '#';

        chamber[row][column + 1] = EMPTY;
        chamber[row + 1][rightEndCol] = EMPTY;
        chamber[row + 2][column + 1] = EMPTY;
        column--;
        return true;
    }

    public boolean moveDown(char[][] chamber) {
        int bottomRow = row + 2;
        if (bottomRow + 1 == chamber.length
                || chamber[bottomRow][column] != EMPTY
                || chamber[bottomRow + 1][column + 1] != EMPTY
                || chamber[bottomRow][column + 2] != EMPTY) {
            return false;
        }

        chamber[bottomRow + 1][column + 1] = '#';
        chamber[bottomRow][column] = '#';
        chamber[bottomRow][column + 2] = '#';

        chamber[row + 1][column] = EMPTY;
        chamber[row + 1][column + 2] = EMPTY;
        chamber[row][column + 1] = EMPTY;
        row++;
        return true;
    }

    @Override
    public int getRow() {
        return row;
    }
}
