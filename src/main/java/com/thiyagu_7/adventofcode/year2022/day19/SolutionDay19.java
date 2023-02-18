package com.thiyagu_7.adventofcode.year2022.day19;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO - Re-work to make it faster - Tests disabled as of now
public class SolutionDay19 {
    public int part1(List<String> input) {
        List<Map<RobotType, List<RobotRequirement>>> blueprints = input.stream()
                .map(this::parseBlueprint)
                .toList();
        Map<RobotType, Integer> robots = new HashMap<>();
        robots.put(RobotType.ORE, 1);

        Map<ResourceType, Integer> resources = new HashMap<>();
        int res = 0;
        for (int i = 0; i < blueprints.size(); i++) {
           // System.out.println("Processing " + (i + 1));
            Result result = new Result();
            stateToMin = new HashMap<>();

            recur(0, blueprints.get(i), new State(robots, resources), result,
                    null, new ArrayList<>(), 24);
            res += (i + 1) * result.num;
        }
        return res;
    }


    public int part2(List<String> input) {
        List<Map<RobotType, List<RobotRequirement>>> blueprints = input.stream()
                .map(this::parseBlueprint)
                .toList();
        Map<RobotType, Integer> robots = new HashMap<>();
        robots.put(RobotType.ORE, 1);

        Map<ResourceType, Integer> resources = new HashMap<>();
        int res = 1;
        for (int i = 0; i < Math.min(3, blueprints.size()); i++) {
            // System.out.println("Processing " + (i + 1));
            Result result = new Result();
            stateToMin = new HashMap<>();

            recur(0, blueprints.get(i), new State(robots, resources), result,
                    null, new ArrayList<>(), 32);
            res *= result.num;
        }
        return res;
    }

    Map<State, Integer> stateToMin = new HashMap<>();

    private void recur(int minute, Map<RobotType, List<RobotRequirement>> requirements,
                       State state,
                       Result result,
                       RobotType notBuild,
                       List<RobotType> notBuildList,
                       int totalMinutes) {
        if (minute == totalMinutes) {
            /*if (result.num < state.resources.getOrDefault(ResourceType.GEODE, 0)) {
                System.out.println(state.resources.getOrDefault(ResourceType.GEODE, 0));
            }*/
            result.num = Math.max(result.num, state.resources.getOrDefault(ResourceType.GEODE, 0));
            return;
        }
        if (stateToMin.containsKey(state)) {
            int min = stateToMin.get(state);
            if (minute >= min) {
                return;
            }
        }

        if (result.num > state.resources.getOrDefault(ResourceType.GEODE, 0)) {
            int x = state.resources.getOrDefault(ResourceType.GEODE, 0);
            int r = state.robots.getOrDefault(RobotType.GEODE, 0);
            for (int i = minute; i < totalMinutes; i++) {
                x = x + r++;
            }
            if (x < result.num) {
                return;
            }
        }

        //generate new resources
        Map<ResourceType, Integer> newResources = new HashMap<>();
        for (Map.Entry<RobotType, Integer> robotsEntry : state.robots.entrySet()) {
            ResourceType resourceType = robotsEntry.getKey()
                    .getResourceType();
            newResources.put(resourceType, robotsEntry.getValue());
        }

        boolean geode = false;
        boolean obs = false;
        boolean clay = false;
        boolean ore = false;
        boolean atleastOne = false;
        boolean onlyOreMissing = false;
        //use resources (at this minute) in different ways
        List<RobotType> o = List.of(
                RobotType.GEODE,
                RobotType.OBSIDIAN,
                RobotType.CLAY,
                RobotType.ORE
        );
        for (RobotType rt : o) {
            RobotType robotType = rt;
            boolean available = requirements.get(robotType)
                    .stream()
                    .allMatch(requirement -> state.resources.getOrDefault(requirement.resourceType, 0) >= requirement.quantity);
            if (available) {
                int currR;
                int rem = totalMinutes - minute;
                int currQ;
                if (robotType == RobotType.OBSIDIAN) {
                    currQ = state.resources.getOrDefault(ResourceType.OBSIDIAN, 0);
                    currR = state.robots.getOrDefault(RobotType.OBSIDIAN, 0);
                    //X * T+Y >= T * Z
                    int requiredObs = requirements.get(RobotType.GEODE)
                            .get(1).quantity;
                    if (rem * currR + currQ >= rem * requiredObs) {
                        continue;
                    }

                } else if (robotType == RobotType.CLAY) {
                    currQ = state.resources.getOrDefault(ResourceType.CLAY, 0);
                    currR = state.robots.getOrDefault(RobotType.CLAY, 0);
                    int requiredClay = requirements.get(RobotType.OBSIDIAN)
                            .get(1).quantity;
                    if (rem * currR + currQ >= rem * requiredClay) {
                        continue;
                    }
                } else if (robotType == RobotType.ORE) {
                    currQ = state.resources.getOrDefault(ResourceType.ORE, 0);
                    currR = state.robots.getOrDefault(RobotType.ORE, 0);
                    List<Integer> req = List.of(
                            requirements.get(RobotType.GEODE).get(0).quantity,
                            requirements.get(RobotType.OBSIDIAN).get(0).quantity,
                            requirements.get(RobotType.CLAY).get(0).quantity,
                            requirements.get(RobotType.ORE).get(0).quantity);
                    int requiredOre = req.stream()
                            .max(Comparator.naturalOrder())
                            .get();
                    if (rem * currR + currQ >= rem * requiredOre) {
                        continue;
                    }
                }

                if (onlyOreMissing && (robotType == RobotType.OBSIDIAN || robotType == RobotType.CLAY)) {
                    continue;
                }

                /*if (notBuild != null && notBuild.equals(robotType)) {
                    continue;
                }*/
                if (notBuildList.contains(robotType)) {
                    continue;
                }
                atleastOne = true;

                //generate new robot
                Map<RobotType, Integer> robots = new HashMap<>(state.robots);
                robots.merge(robotType, 1, Integer::sum);

                //reduce resources
                Map<ResourceType, Integer> resources = new HashMap<>(state.resources);
                for (RobotRequirement requirement : requirements.get(robotType)/*entry.getValue()*/) {
                    int existingQuantity = resources.get(requirement.resourceType);
                    resources.put(requirement.resourceType, existingQuantity - requirement.quantity);
                }
                // add resources generated at this minute
                newResources.forEach((k, v) ->
                        resources.merge(k, v, Integer::sum));

                recur(minute + 1, requirements, new State(robots, resources), result,
                        null, new ArrayList<>(), totalMinutes);
                if (robotType == RobotType.GEODE) {
                    geode = true;
                    break;
                }
                if (robotType == RobotType.OBSIDIAN) {
                    obs = true;
                }
                if (robotType == RobotType.CLAY) {
                    clay = true;

                }
                if (robotType == RobotType.ORE) {
                    ore = true;
                }
            } else {
                int ore1, clay1, obsidian1;
                if (robotType == RobotType.GEODE) {
                    ore1 = requirements.get(robotType)
                            .get(0).quantity;
                    obsidian1 = requirements.get(robotType)
                            .get(1).quantity;
                    if (state.resources.getOrDefault(ResourceType.OBSIDIAN, 0) < obsidian1) {
                        onlyOreMissing = false;
                    } else if (state.resources.getOrDefault(ResourceType.ORE, 0) < ore1) {
                        onlyOreMissing = true;
                    }

                } else if (robotType == RobotType.OBSIDIAN && !onlyOreMissing) {
                    ore1 = requirements.get(robotType)
                            .get(0).quantity;
                    clay1 = requirements.get(robotType)
                            .get(1).quantity;
                    if (state.resources.getOrDefault(ResourceType.CLAY, 0) < clay1) {
                        onlyOreMissing = false;
                    } else if (state.resources.getOrDefault(ResourceType.ORE, 0) < ore1) {
                        onlyOreMissing = true;
                    }

                }
            }
        }
        if (!atleastOne) {
            Map<ResourceType, Integer> resources = new HashMap<>(state.resources);
            newResources.forEach((k, v) ->
                    resources.merge(k, v, Integer::sum));
            recur(minute + 1, requirements, new State(state.robots, resources), result,
                    null, notBuildList, totalMinutes);
        } else {
            List<RobotType> no = new ArrayList<>();
            if (obs) {
                no.add(RobotType.OBSIDIAN);
            }
            if (clay) {
                no.add(RobotType.CLAY);
            }
            if (ore) {
                no.add(RobotType.ORE);
            }
            Map<ResourceType, Integer> resources1 = new HashMap<>(state.resources);
            newResources.forEach((k, v) ->
                    resources1.merge(k, v, Integer::sum));
            recur(minute + 1, requirements, new State(state.robots, resources1), result,
                    RobotType.OBSIDIAN, no, totalMinutes);
        }
        stateToMin.put(state, minute);
    }

    private Map<RobotType, List<RobotRequirement>> parseBlueprint(String line) {
        Map<RobotType, List<RobotRequirement>> requirement = new HashMap<>();

        Pattern pattern = Pattern.compile("Each ore robot costs (\\d+) ore");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        requirement.put(RobotType.ORE, List.of(
                new RobotRequirement(ResourceType.ORE, Integer.parseInt(matcher.group(1)))
        ));

        pattern = Pattern.compile("Each clay robot costs (\\d+) ore");
        matcher = pattern.matcher(line);
        matcher.find();
        requirement.put(RobotType.CLAY, List.of(
                new RobotRequirement(ResourceType.ORE, Integer.parseInt(matcher.group(1)))
        ));

        pattern = Pattern.compile("Each obsidian robot costs (\\d+) ore and (\\d+) clay");
        matcher = pattern.matcher(line);
        matcher.find();
        requirement.put(RobotType.OBSIDIAN, List.of(
                new RobotRequirement(ResourceType.ORE, Integer.parseInt(matcher.group(1))),
                new RobotRequirement(ResourceType.CLAY, Integer.parseInt(matcher.group(2)))
        ));

        pattern = Pattern.compile("Each geode robot costs (\\d+) ore and (\\d+) obsidian");
        matcher = pattern.matcher(line);
        matcher.find();
        requirement.put(RobotType.GEODE, List.of(
                new RobotRequirement(ResourceType.ORE, Integer.parseInt(matcher.group(1))),
                new RobotRequirement(ResourceType.OBSIDIAN, Integer.parseInt(matcher.group(2)))
        ));
        return requirement;
    }

    private record State(Map<RobotType, Integer> robots, Map<ResourceType, Integer> resources) {
    }

    private record RobotRequirement(ResourceType resourceType, int quantity) {
    }

    private enum RobotType {
        ORE(ResourceType.ORE),
        CLAY(ResourceType.CLAY),
        OBSIDIAN(ResourceType.OBSIDIAN),
        GEODE(ResourceType.GEODE);

        private ResourceType resourceType;

        RobotType(ResourceType resourceType) {
            this.resourceType = resourceType;
        }

        public ResourceType getResourceType() {
            return resourceType;
        }
    }

    private enum ResourceType {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    private static class Result {
        private int num = Integer.MIN_VALUE;
    }
}
