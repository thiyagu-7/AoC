package com.thiyagu_7.adventofcode.year2024.day9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SolutionDay9 {
    public long part1(String input) {
        List<Integer> diskMapWithIndividualBlocks = constructDiskMapWithIndividualBlocks(input);
        compact(diskMapWithIndividualBlocks);
        return calculateChecksum(diskMapWithIndividualBlocks);
    }

    private List<Integer> constructDiskMapWithIndividualBlocks(String input) {
        List<Integer> diskMapWithIndividualBlocks = new ArrayList<>();
        boolean isFile = true;
        int fileNum = 0;
        for (char c : input.toCharArray()) {
            int n = Integer.parseInt(c + "");
            if (isFile) {
                for (int i = 0; i < n; i++) {
                    diskMapWithIndividualBlocks.add(fileNum);
                }
                fileNum++;
            } else { //free space
                for (int i = 0; i < n; i++) {
                    diskMapWithIndividualBlocks.add(-1);
                }
            }
            isFile = !isFile;
        }
        return diskMapWithIndividualBlocks;
    }

    private void compact(List<Integer> diskMapWithIndividualBlocks) {
        int i = 0;
        int j = diskMapWithIndividualBlocks.size() - 1;

        while (i < j) {
            while (i < diskMapWithIndividualBlocks.size() && diskMapWithIndividualBlocks.get(i) != -1) {
                i++;
            }
            while (j >= 0 && diskMapWithIndividualBlocks.get(j) == -1) {
                j--;
            }
            if (i == diskMapWithIndividualBlocks.size() || j == -1 || i > j) {
                break;
            }
            int fileNum = diskMapWithIndividualBlocks.get(j);
            diskMapWithIndividualBlocks.set(i, fileNum);
            diskMapWithIndividualBlocks.set(j, -1);
            i++;
            j--;
        }
    }

    private long calculateChecksum(List<Integer> diskMapWithIndividualBlocks) {
        long result = 0;
        for (int i = 0; i < diskMapWithIndividualBlocks.size(); i++) {
            if (diskMapWithIndividualBlocks.get(i) == -1) {
                continue; //for part-1 this can be 'break' as there won't be any file block after the first space block
            }
            result += (long) i * diskMapWithIndividualBlocks.get(i);
        }
        return result;
    }

    public long part2(String input) {
        List<Integer> diskMapWithIndividualBlocks = new ArrayList<>();
        // Index to File number and length
        Map<Integer, FileDetail> fileDetails = new TreeMap<>(Comparator.reverseOrder());
        // Index to Free space length
        Map<Integer, Integer> freeSpaceDetails = new TreeMap<>();

        boolean isFile = true;
        int fileNum = 0;
        for (char c : input.toCharArray()) {
            int n = Integer.parseInt(c + "");
            if (isFile) {
                int idx = diskMapWithIndividualBlocks.size();
                fileDetails.put(idx, new FileDetail(fileNum, n));

                for (int i = 0; i < n; i++) {
                    diskMapWithIndividualBlocks.add(fileNum);
                }
                fileNum++;
            } else { //free space
                int idx = diskMapWithIndividualBlocks.size();
                freeSpaceDetails.put(idx, n);

                for (int i = 0; i < n; i++) {
                    diskMapWithIndividualBlocks.add(-1);
                }
            }
            isFile = !isFile;
        }

        compactPart2(diskMapWithIndividualBlocks, fileDetails, freeSpaceDetails);

        return calculateChecksum(diskMapWithIndividualBlocks);
    }

    record FileDetail(int fileNum, int length) {

    }

    private void compactPart2(List<Integer> diskMapWithIndividualBlocks,
                              Map<Integer, FileDetail> fileDetails,
                              Map<Integer, Integer> freeSpaceDetails) {
        for (Map.Entry<Integer, FileDetail> fileEntry : fileDetails.entrySet()) {
            int fileIndex = fileEntry.getKey();
            int fileLength = fileEntry.getValue().length;
            int fileNum = fileEntry.getValue().fileNum;

            int remainingFreeSpace = 0;
            int newFreeSpaceIndex = 0;
            int processedFreeSpaceIndex = -1;

            for (Map.Entry<Integer, Integer> freeSpaceEntry : freeSpaceDetails.entrySet()) {
                int freeSpaceIndex = freeSpaceEntry.getKey();
                int freeSpaceLength = freeSpaceEntry.getValue();

                if (freeSpaceIndex >= fileIndex) { //if first available free space to the right of file
                    break;
                }
                if (freeSpaceLength >= fileLength) {
                    for (int j = freeSpaceIndex; j < freeSpaceIndex + fileLength; j++) {
                        diskMapWithIndividualBlocks.set(j, fileNum);
                    }
                    for (int j = fileIndex; j < fileIndex + fileLength; j++) {
                        diskMapWithIndividualBlocks.set(j, -1);
                    }
                    remainingFreeSpace = freeSpaceLength - fileLength;
                    if (remainingFreeSpace > 0) {
                        newFreeSpaceIndex = freeSpaceIndex + fileLength;
                    }
                    processedFreeSpaceIndex = freeSpaceIndex;
                    break;
                }
            }
            //remove the modified free space and update it if remainingFreeSpace > 0
            if (processedFreeSpaceIndex != -1) {
                freeSpaceDetails.remove(processedFreeSpaceIndex);
            }
            if (remainingFreeSpace > 0) {
                freeSpaceDetails.put(newFreeSpaceIndex, remainingFreeSpace);
            }
        }
    }
}
