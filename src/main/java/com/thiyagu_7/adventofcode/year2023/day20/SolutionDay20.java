package com.thiyagu_7.adventofcode.year2023.day20;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SolutionDay20 {
    public int part1(List<String> input) {
        Map<String, Module> moduleMap = parseInput(input);
        Queue<PulseOutput> processQueue = new LinkedList<>();

        int highPulses = 0, lowPulses = 0;
        Module broadcaster = moduleMap.get("broadcaster");

        //1000 button presses
        for (int i = 0; i < 1000; i++) {
            PulseOutput pulseOutput = broadcaster.handlePulse("button", //sender name doesn't matter for broadcaster module
                            Pulse.LOW)
                    .get();
            processQueue.add(pulseOutput);
            lowPulses++;

            while (!processQueue.isEmpty()) {
                pulseOutput = processQueue.poll();

                for (String destinationModuleName : pulseOutput.destinationModules) {
                    Module destinationModule = moduleMap.get(destinationModuleName);

                    if (pulseOutput.pulse == Pulse.HIGH) {
                        highPulses++;
                    } else {
                        lowPulses++;
                    }

                    if (destinationModule != null) {
                        Optional<PulseOutput> maybePulseOutput = destinationModule.handlePulse(
                                pulseOutput.sender,
                                pulseOutput.pulse);
                        maybePulseOutput.ifPresent(processQueue::add);
                    }
                }
            }
        }
        return highPulses * lowPulses;
    }

    private Map<String, Module> parseInput(List<String> input) {
        Map<String, Module> moduleMap = new HashMap<>();
        Map<String, List<String>> allModulesToDestination = new HashMap<>();
        List<String> conjunctionModules = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(" -> ");
            List<String> destinationModules = Arrays.stream(parts[1].split(", "))
                    .toList();

            if (parts[0].equals("broadcaster")) {
                allModulesToDestination.put(parts[0], destinationModules);
                moduleMap.put(parts[0], new BroadcasterModule(parts[0], destinationModules));
            } else {
                String moduleName = parts[0].substring(1);

                allModulesToDestination.put(moduleName, destinationModules);

                if (parts[0].startsWith("%")) { //flip-flop module
                    moduleMap.put(moduleName, new FlipFlopModule(moduleName, destinationModules));
                } else {
                    conjunctionModules.add(moduleName);
                }
            }
        }
        for (String conjunctionModule : conjunctionModules) {
            //modules sending a signal to this conjunction module
            List<String> inputModules = allModulesToDestination.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().contains(conjunctionModule))
                    .map(Map.Entry::getKey)
                    .toList();
            moduleMap.put(conjunctionModule,
                    new ConjunctionModule(conjunctionModule,
                            allModulesToDestination.get(conjunctionModule), inputModules));
        }
        return moduleMap;
    }

    interface Module {
        Optional<PulseOutput> handlePulse(String senderModule, Pulse pulse);
    }

    @Getter
    abstract class AbstractModule implements Module {
        private final String name;
        private final List<String> destinationModules;

        AbstractModule(String name, List<String> destinationModules) {
            this.name = name;
            this.destinationModules = destinationModules;
        }
    }

    class FlipFlopModule extends AbstractModule {
        private State state = State.OFF;

        FlipFlopModule(String name, List<String> destinationModules) {
            super(name, destinationModules);
        }

        @Override
        public Optional<PulseOutput> handlePulse(String senderModule, Pulse pulse) {
            if (pulse == Pulse.LOW) {
                if (state == State.OFF) {
                    state = State.ON;
                    return Optional.of(new PulseOutput(getName(), getDestinationModules(), Pulse.HIGH));
                } else {
                    state = State.OFF;
                    return Optional.of(new PulseOutput(getName(), getDestinationModules(), Pulse.LOW));
                }
            }
            //nothing to do if received a High Pulse
            return Optional.empty();
        }
    }

    class ConjunctionModule extends AbstractModule {
        private final Map<String, Pulse> pulseReceivedFromInputModules;

        ConjunctionModule(String name, List<String> destinationModules,
                          List<String> inputModules) {
            super(name, destinationModules);
            pulseReceivedFromInputModules = inputModules.stream()
                    .collect(Collectors.toMap(Function.identity(), moduleName -> Pulse.LOW));
        }

        @Override
        public Optional<PulseOutput> handlePulse(String senderModule, Pulse pulse) {
            //update memory first
            pulseReceivedFromInputModules.put(senderModule, pulse);
            return Optional.of(new PulseOutput(getName(), getDestinationModules(),
                    highPulseForAllInputs() ? Pulse.LOW : Pulse.HIGH));
        }

        private boolean highPulseForAllInputs() {
            return pulseReceivedFromInputModules.values()
                    .stream()
                    .allMatch(p -> p == Pulse.HIGH);
        }
    }

    class BroadcasterModule extends AbstractModule {
        BroadcasterModule(String name, List<String> destinationModules) {
            super(name, destinationModules);
        }

        @Override
        public Optional<PulseOutput> handlePulse(String senderModule, Pulse pulse) {
            return Optional.of(new PulseOutput(getName(), getDestinationModules(), pulse));
        }
    }

    record PulseOutput(String sender, List<String> destinationModules, Pulse pulse) {

    }

    enum Pulse {
        HIGH,
        LOW;
    }

    enum State {
        ON,
        OFF;
    }
}
