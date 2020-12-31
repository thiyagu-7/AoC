package com.thiyagu_7.adventofcode.year2020.day20;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeaMonsterFinder {
    public int findRoughness(SolutionDay20Part2.MatchingTileAndAlignment[][] grid, int dimension) {
        int c = 8; //10 -> removed borders/edges -> 8
        int size = dimension * c;
        char[][] image = new char[size][size];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                SolutionDay20Part2.MatchingTileAndAlignment m = grid[i][j];

                int row = c * i;
                for (int a = 1; a < m.tile.length - 1; a++, row++) {
                    int col = c * j;
                    for (int b = 1; b < m.tile[0].length - 1; b++, col++) {
                        image[row][col] = m.tile[a][b];
                    }
                }
            }
        }

        int r = 0;
        Map<Alignment, char[][]> alignments = get8Permutations(image);
        for (char[][] img : alignments.values()) {
            if (markSeaMonsters2(img)) {
                for (int i = 0; i < image.length; i++) {
                    System.out.println(Arrays.toString(img[i]));

                }
                System.out.println("FOUND");
                for (int i = 0; i < image.length; i++) {
                    for (int j = 0; j < image[0].length; j++) {
                        if (img[i][j] == '#') {
                            r++;
                        }
                    }
                }
                //break;
                //return r;
            }
        }
        return r;
    }

    private boolean markSeaMonsters2(char[][] image) {
        boolean found = false;

        for (int i = 1; i < image.length - 1; i++) {
            for (int j = 1; j + 2 < image.length; j++) {
                if (image[i][j] == '#' && image[i][j + 1] == '#' && image[i][j + 2] == '#'
                        && image[i - 1][j + 1] == '#' && image[i + 1][j - 1] == '#') {
                    found = found | findPotentialSeaMonster(image, i, j);
                }
            }

        }
        return found;
    }

    //x, y start of head (###)
    private boolean findPotentialSeaMonster(char[][] image, int x, int y) {
        int n = 0;
        int j = y - 4;

        int bodyPart1Index = 0;
        int bodyPart2Index = 0;
        int headIndex = y;
        int tailIndex;
        for (; j > 1; j--) {
            if (image[x][j] == '#' && image[x][j - 1] == '#'
                    && image[x + 1][j + 1] == '#' && image[x + 1][j - 2] == '#') {
                //found body part 1
                if (n == 0) {
                    bodyPart1Index = j;
                } else {
                    if (headIndex - bodyPart1Index == bodyPart1Index - 1 - j) {
                        bodyPart2Index = j;
                    } else {
                        continue;
                    }
                }
                n++;
                j -= 3;
            }
            if (n == 2) {
                j--;
                break;
            }
        }

        if (n == 2) {
            for (; j >= 0; j--) {
                if (image[x][j] == '#' && image[x + 1][j + 1] == '#') {
                    //found tail
                    tailIndex = j;
                    //check spacings
                    if (headIndex - bodyPart1Index == bodyPart1Index - 1 - bodyPart2Index
                            && bodyPart1Index - 1 - bodyPart2Index == bodyPart2Index - 1 - tailIndex) {
                        //now mark
                        //head
                        image[x][headIndex] = 'X' ;
                        image[x][headIndex + 1] = 'X' ;
                        image[x][headIndex + 2] = 'X' ;
                        image[x - 1][headIndex + 1] = 'X' ;
                        image[x + 1][headIndex - 1] = 'X' ;

                        //body
                        image[x][bodyPart1Index] = 'X' ;
                        image[x][bodyPart1Index - 1] = 'X' ;
                        image[x + 1][bodyPart1Index + 1] = 'X' ;
                        image[x + 1][bodyPart1Index - 2] = 'X' ;

                        image[x][bodyPart2Index] = 'X' ;
                        image[x][bodyPart2Index - 1] = 'X' ;
                        image[x + 1][bodyPart2Index + 1] = 'X' ;
                        image[x + 1][bodyPart2Index - 2] = 'X' ;

                        //tail
                        image[x][tailIndex] = 'X' ;
                        image[x + 1][tailIndex + 1] = 'X' ;

                        return true;
                    }

                }
            }
        }
        return false;
    }

    /*private boolean markSeaMonsters(char[][] image) {
        //a sea monster will occupy 19 cols
        boolean found = false;
        List<List<Integer>> indicesToMatch = Arrays.asList(
                Collections.singletonList(18),
                Arrays.asList(0, 5, 6, 11, 12, 17, 18, 19),
                Arrays.asList(1, 4, 7, 10, 13, 16)
        );
        for (int i = 0; i < image.length - 2; i++) {
            for (int j = 0; j + 19 < image.length; j++) {
                int row = i;
                int col = j;
                if (indicesToMatch.get(0).stream()
                        .allMatch(idx -> image[row][col + idx] == '#')
                        &&
                        indicesToMatch.get(1).stream()
                                .allMatch(idx -> image[row + 1][col + idx] == '#')
                        &&
                        indicesToMatch.get(2).stream()
                                .allMatch(idx -> image[row + 2][col + idx] == '#')) {
                    found = true;
                    indicesToMatch.get(0)
                            .forEach(idx -> image[row][col + idx] = 'X');
                    indicesToMatch.get(1)
                            .forEach(idx -> image[row + 1][col + idx] = 'X');
                    indicesToMatch.get(2)
                            .forEach(idx -> image[row + 2][col + idx] = 'X');
                }
            }
        }
        return found;
    }*/

    //todo
    private Map<Alignment, char[][]> get8Permutations(char[][] tile) {
        int l = tile.length;
        int i, j, k, m;
        //as it is
        char[][] asItIs = new char[l][l];
        for (i = 0; i < l; i++) {
            for (j = 0; j < l; j++) {
                asItIs[i][j] = tile[i][j];
            }
        }

        //reversed
        char[][] reversed = new char[l][l];
        for (i = 0; i < l; i++) {
            for (k = 0, j = l - 1; j >= 0; k++, j--) {
                reversed[i][k] = tile[i][j];
            }
        }

        //flipped
        char[][] flipped = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (j = 0; j < l; j++) {
                flipped[k][j] = tile[i][j];
            }
        }

        //reversed and flipped
        char[][] reversedAndFlipped = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (m = 0, j = l - 1; j >= 0; m++, j--) {
                reversedAndFlipped[k][m] = tile[i][j];
            }
        }
        Map<Alignment, char[][]> res = new HashMap<>();
        res.put(Alignment.AS_IT_IS, asItIs);
        res.put(Alignment.REVERSED, reversed);
        res.put(Alignment.FLIPPED, flipped);
        res.put(Alignment.FLIPPED_AND_REVERSED, reversedAndFlipped);

        char[][] n1 = new char[l][l];
        for (i = 0; i < l; i++) {
            for (j = 0; j < l; j++) {
                n1[i][j] = tile[j][i];
            }
        }

        char[][] n2 = new char[l][l];
        for (i = 0; i < l; i++) {
            for (k = 0, j = l - 1; j >= 0; k++, j--) {
                n2[i][k] = tile[j][i];
            }
        }

        char[][] n3 = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (j = 0; j < l; j++) {
                n3[k][j] = tile[j][i];
            }
        }

        //flipped
        char[][] n4 = new char[l][l];
        for (k = 0, i = l - 1; i >= 0; k++, i--) {
            for (m = 0, j = l - 1; j >= 0; m++, j--) {
                n4[k][m] = tile[j][i];
            }
        }
        res.put(Alignment.N1, n1);
        res.put(Alignment.N2, n2);
        res.put(Alignment.N3, n3);
        res.put(Alignment.N4, n4);
        return res;
    }

    private enum Alignment {
        AS_IT_IS,
        REVERSED,
        FLIPPED,
        FLIPPED_AND_REVERSED,

        N1,
        N2, N3, N4;
    }
}
