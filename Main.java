import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Process {
    String name;
    int processId;
    List<Integer> dependencies;

    public Process(String name, int processId) {
        this.name = name;
        this.processId = processId;
        this.dependencies = new ArrayList<>();
    }
}

class DeadlockDetector {
    Set<Integer> visited;
    Set<Integer> stack;
    List<List<Integer> > deadlocks;
    List<Process> processes; // List of processes

    public DeadlockDetector(List<Process> processes) {
        visited = new HashSet<>();
        stack = new HashSet<>();
        deadlocks = new ArrayList<>();
        this.processes = processes;
    }

    public void detectDeadlocks() {
        for (Process process : processes) {
            if (!visited.contains(process.processId)) {
                detectCycle(process);
            }
        }
    }

    public void detectCycle(Process process) {
        visited.add(process.processId);
        stack.add(process.processId);

        for (int dependencyId : process.dependencies) {
            if (stack.contains(dependencyId)) {
                List<Integer> cycle = new ArrayList<>(stack);
                cycle = cycle.subList(cycle.indexOf(dependencyId), cycle.size());
                deadlocks.add(cycle);
            } else if (!visited.contains(dependencyId)) {
                Process dependency = getProcessById(dependencyId);
                if (dependency != null) {
                    detectCycle(dependency);
                }
            }
        }

        stack.remove(process.processId);
    }

    public Process getProcessById(int processId) {
        for (Process process : processes) {
            if (process.processId == processId) {
                return process;
            }
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        Process process1 = new Process("Process A", 1);
        Process process2 = new Process("Process B", 2);
        Process process3 = new Process("Process C", 3);
        Process process4 = new Process("Process D", 4);

        process1.dependencies.add(2);
        process2.dependencies.add(3);
        process3.dependencies.add(4);
        process4.dependencies.add(1);

        List<Process> processes = new ArrayList<>();
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);
        processes.add(process4);

        DeadlockDetector detector = new DeadlockDetector(processes);
        detector.detectDeadlocks();

        if (detector.deadlocks.isEmpty()) {
            System.out.println("No deadlocks found.");
        } else {
            System.out.println("Deadlocks detected:");
            for (List<Integer> deadlock : detector.deadlocks) {
                System.out.println(deadlock);
            }
        }
    }
}
