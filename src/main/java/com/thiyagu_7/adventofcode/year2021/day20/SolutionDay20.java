package com.thiyagu_7.adventofcode.year2021.day20;

import java.util.List;

public class SolutionDay20 {
    private static final int[] xCoor = new int[]{-1, -1, -1, 0, 0, 0, 1, 1, 1};
    private static final int[] yCoor = new int[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};

    public int part1(List<String> input) {
        return processImage(input, 2);
    }

    public int part2(List<String> input) {
        return processImage(input, 50);
    }

    private int processImage(List<String> input, int timesToProcess) {
        char[] imageEnhancementAlgo = input.get(0).toCharArray();
        char[][] image = buildImage(input);
        char infiniteSpaceValue = '.';

        int n, m;
        for (int t = 0; t < timesToProcess; t++) {
            image = copyAndExpand(image, infiniteSpaceValue);
            n = image.length;
            m = image[0].length;

            char[][] newImage = new char[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    StringBuilder builder = new StringBuilder();
                    for (int k = 0; k < 9; k++) {
                        int x = i + xCoor[k];
                        int y = j + yCoor[k];
                        char value;
                        if (x >= 0 && x < n && y >= 0 && y < m) {
                            value = image[x][y];
                        } else {
                            value = infiniteSpaceValue;
                        }
                        builder.append(value == '.' ? "0" : "1");
                    }
                    int decimal = Integer.parseInt(builder.toString(), 2);
                    char lookedUpValue = imageEnhancementAlgo[decimal];
                    newImage[i][j] = lookedUpValue;
                }
            }
            image = newImage;
            //infiniteSpaceValue - can all be the lookup value of 000000000 or 111111111
            infiniteSpaceValue = infiniteSpaceValue == '.' ? imageEnhancementAlgo[0] : imageEnhancementAlgo[511];
        }
        int numLit = 0;
        n = image.length;
        m = image[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (image[i][j] == '#') {
                    numLit++;
                }
            }
        }
        return numLit;
    }

    private char[][] buildImage(List<String> input) {
        List<String> lines = input.subList(2, input.size());
        int n = lines.size();
        int m = lines.get(0).length();
        char[][] image = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                image[i][j] = lines.get(i).charAt(j);
            }
        }
        return image;
    }

    //expand by one row and col on all side
    private char[][] copyAndExpand(char[][] image, char infiniteSpaceValue) {
        int n = image.length;
        int m = image[0].length;
        char[][] expandedImage = new char[n + 2][m + 2];
        //first row
        for (int j = 0; j <= m + 1; j++) {
            expandedImage[0][j] = infiniteSpaceValue;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                expandedImage[i + 1][j + 1] = image[i][j];
            }
            expandedImage[i + 1][0] = infiniteSpaceValue;
            expandedImage[i + 1][m + 1] = infiniteSpaceValue;
        }
        //last row
        for (int j = 0; j <= m + 1; j++) {
            expandedImage[n + 1][j] = infiniteSpaceValue;
        }
        return expandedImage;
    }


}