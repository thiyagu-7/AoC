package com.thiyagu_7.adventofcode.year2020.day4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolutionDay4 {
    private static final List<String> VALID_EYE_COLORS = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    public int part1(List<String> data) {
        StringBuilder passportDataBuilder = new StringBuilder();
        int numValid = 0;
        for (String line : data) {
            if (line.isEmpty()) {
                if (validatePassportDataPart1(passportDataBuilder.toString())) {
                    numValid++;
                }
                passportDataBuilder = new StringBuilder();
            } else {
                passportDataBuilder.append(line)
                        .append(System.lineSeparator());
            }
        }
        if (validatePassportDataPart1(passportDataBuilder.toString())) {
            numValid++;
        }
        return numValid;
    }

    private boolean validatePassportDataPart1(String passportData) {
       Map<String, String> data = buildPassportData(passportData);
       return hasMandatoryFields(data);
    }

    private boolean hasMandatoryFields(Map<String, String> data) {
        return data.containsKey("byr")
                && data.containsKey("iyr")
                && data.containsKey("eyr")
                && data.containsKey("hgt")
                && data.containsKey("hcl")
                && data.containsKey("ecl")
                && data.containsKey("pid");
    }

    private Map<String, String> buildPassportData(String passportData) {
        String[] lines = passportData.split(System.lineSeparator());

        return Arrays.stream(lines)
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(keyValue -> keyValue.split(":"))
                .collect(Collectors.toMap(keyValue -> keyValue[0], keyValue -> keyValue[1]));
    }

    public int part2(List<String> data) {
        StringBuilder passportDataBuilder = new StringBuilder();
        int numValid = 0;
        for (String line : data) {
            if (line.isEmpty()) {
                if (validatePassportDataPart2(passportDataBuilder.toString())) {
                    numValid++;
                }
                passportDataBuilder = new StringBuilder();
            } else {
                passportDataBuilder.append(line)
                        .append(System.lineSeparator());
            }
        }
        if (validatePassportDataPart2(passportDataBuilder.toString())) {
            numValid++;
        }
        return numValid;
    }

    private boolean validatePassportDataPart2(String passportData) {
        Map<String, String> data = buildPassportData(passportData);

        return hasMandatoryFields(data)
                && isValidBirthYear(data.get("byr"))
                && isValidIssueYear(data.get("iyr"))
                && isValidExpirationYear(data.get("eyr"))
                && isValidHeight(data.get("hgt"))
                && isValidHairColor(data.get("hcl"))
                && isValidEyeColor(data.get("ecl"))
                && isValidPassportId(data.get("pid"));

    }

    private boolean isValidBirthYear(String birthYear) {
        return birthYear.length() == 4
                && Integer.valueOf(birthYear) >= 1920
                && Integer.valueOf(birthYear) <= 2002;
    }

    private boolean isValidIssueYear(String issueYear) {
        return issueYear.length() == 4
                && Integer.valueOf(issueYear) >= 2010
                && Integer.valueOf(issueYear) <= 2020;
    }

    private boolean isValidExpirationYear(String expirationYear) {
        return expirationYear.length() == 4
                && Integer.valueOf(expirationYear) >= 2020
                && Integer.valueOf(expirationYear) <= 2030;
    }

    private boolean isValidHeight(String height) {
        if (height.endsWith("cm")) {
            String val = height.substring(0, height.indexOf("cm"));
            return Integer.valueOf(val) >= 150
                    && Integer.valueOf(val) <= 193;
        } else if (height.endsWith("in")) {
            String val = height.substring(0, height.indexOf("in"));
            return Integer.valueOf(val) >= 59
                    && Integer.valueOf(val) <= 76;
        }
        return false;
    }

    private boolean isValidHairColor(String hairColor) {
        if (!hairColor.startsWith("#")) {
            return false;
        }
        return hairColor.substring(1).matches("[a-f0-9]{6}");
    }

    private boolean isValidEyeColor(String eyeColor) {
        return VALID_EYE_COLORS.contains(eyeColor);
    }

    private boolean isValidPassportId(String passportId) {
        return passportId.matches("^\\d{9}$"); //9 digit number
    }
}
