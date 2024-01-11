package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static agh.ics.oop.World.setUpConstants;

public class SimulationWindowManager {

    // WE WILL NEED SHIT TONS OF @FXML PARAMETERS HERE
    private static AtomicInteger simulationId = new AtomicInteger(1);
    private SimulationApp app;

    public void setApp(SimulationApp app){
        this.app = app;
    }

    public void handleNewWindow() throws IOException, InterruptedException {
        int id = simulationId.getAndIncrement();
        setUpConstants(id);
        Stats stats = new Stats(id);
        StatsList.addToStatsList(id, stats);

        SimulationPresenter windowPresenter = new SimulationPresenter();
        app.startNewWindow(windowPresenter);
        windowPresenter.setUp(id);

        windowPresenter.run();
    }
}
