package agh.ics.oop;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    List<Simulation> simulations;
    ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
    }

    public void runSync() {
        for(Simulation simulation : simulations) {
            simulation.run();
        }
    }

    public void runAsync() throws InterruptedException {
        executorService = Executors.newFixedThreadPool(4);
        for(Simulation simulation : simulations) {
            executorService.submit(simulation);
        }
    }

    public void runAsyncInThreadPool() throws InterruptedException {
        runAsync();
    }

    private void awaitSimulationsEnd() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        };
    }
}
