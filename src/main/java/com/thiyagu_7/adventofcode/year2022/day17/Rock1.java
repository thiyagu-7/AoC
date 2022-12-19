package com.thiyagu_7.adventofcode.year2022.day17;

class Rock1 implements Rock {
    private final char[][] rockStructure;
    //row and column are the co-ordinates of top of this rock.
    private int row;
    private int column = 2;
    Rock1() {
        this.rockStructure = new char[][]{
                {'#', '#', '#', '#'}
        };
    }
    public int beginRock(char[][] chamber, int highestRock) {
        this.row = highestRock - 4;
        for (int j = 0; j < 4; j++) {
            chamber[row][column + j] = rockStructure[0][j];
        }
        return row;
    }
    public boolean moveRight(char[][] chamber) {
        int rightEndCol = column + 3;
        if (rightEndCol + 1 == chamber[0].length
                || chamber[row][rightEndCol + 1] != EMPTY) {
            return false;
        }
        chamber[row][rightEndCol + 1] = '#';
        chamber[row][column] = EMPTY;
        column++;
        return true;
    }
    public boolean moveLeft(char[][] chamber) {
        int leftEndCol = column;
        int rightEndCol = column + 3;
        if (leftEndCol - 1 < 0
                || chamber[row][leftEndCol - 1] != EMPTY) {
            return false;
        }
        chamber[row][leftEndCol - 1] = '#';
        chamber[row][rightEndCol] = EMPTY;
        column--;
        return true;
    }
    public boolean moveDown(char[][] chamber) {
        if (row + 1 == chamber.length) {
            return false;
        }
        for (int j = column; j < column + 4; j++) {
            if (chamber[row + 1][j] != EMPTY) {
                return false;
            }
        }
        for (int j = column; j < column + 4; j++) {
            chamber[row][j] = EMPTY;
            chamber[row + 1][j] = '#';
        }
        row++;
        return true;
    }

    @Override
    public int getRow() {
        return row;
    }
}
