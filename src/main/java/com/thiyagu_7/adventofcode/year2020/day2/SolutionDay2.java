package com.thiyagu_7.adventofcode.year2020.day2;

import java.util.List;

public class SolutionDay2 {
    public int part1(List<String> lines) {
        return (int) lines.stream()
                .filter(this::isPasswordValidWhenPolicyIsMinMax)
                .count();
    }
    private boolean isPasswordValidWhenPolicyIsMinMax(String line) {
        PasswordParts passwordParts = PasswordParts.build(line);

        String[] parts = passwordParts.policy.split("-");
        int min = Integer.parseInt(parts[0]);
        int max = Integer.parseInt(parts[1]);

        long count = passwordParts.password.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c == passwordParts.character)
                .count();

        return count >= min && count <= max;
    }

    public int part2(List<String> lines) {
        return (int) lines.stream()
                .filter(this::isPasswordValidWhenPolicyIsPosition)
                .count();
    }

    private boolean isPasswordValidWhenPolicyIsPosition(String line) {
        PasswordParts passwordParts = PasswordParts.build(line);
        String[] parts = passwordParts.policy.split("-");
        int position1 = Integer.parseInt(parts[0]);
        int position2 = Integer.parseInt(parts[1]);

        //Exactly one of these positions must contain the given letter.
        return passwordParts.password.charAt(position1) == passwordParts.character ^
                passwordParts.password.charAt(position2) == passwordParts.character;

    }

    private static class PasswordParts {
        private String policy;
        private char character;
        private String password;

        private PasswordParts(String policy, char character, String password) {
            this.policy = policy;
            this.character = character;
            this.password = password;
        }

        //eg, 1-3 a: abcde
        public static PasswordParts build(String line) {
            String[] parts = line.split(":");
            String password = parts[1];

            parts = parts[0].split(" ");
            char character = parts[1].charAt(0); //guaranteed to be one char

            return new PasswordParts(parts[0], character, password);

        }
    }
}
