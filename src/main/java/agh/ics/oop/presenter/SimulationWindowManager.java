package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;
import com.sun.javafx.scene.control.IntegerField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationWindowManager {

    // WE WILL NEED SHIT TONS OF @FXML PARAMETERS HERE
    @FXML
    private CheckBox backAndForth;
    @FXML
    private CheckBox portalToHell;
    @FXML
    private IntegerField numberOfGenes;
    @FXML
    private IntegerField minMutations;
    @FXML
    private IntegerField maxMutations;
    @FXML
    private IntegerField minEnergyForReproduction;
    @FXML
    private IntegerField energyUsedForReproduction;
    @FXML
    private IntegerField startingAnimalNumber;
    @FXML
    private IntegerField newAnimalEnergy;
    @FXML
    private IntegerField dailyNewGrassNumber;
    @FXML
    private IntegerField dailyEnergyLoss;
    @FXML
    private IntegerField energyGainFromPlant;
    @FXML
    private IntegerField mapWidth;
    @FXML
    private IntegerField mapHeight;

    private static AtomicInteger simulationId = new AtomicInteger(1);
    private SimulationApp app;

    public void setApp(SimulationApp app){
        this.app = app;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }

    private void setUpConstants(int id) {
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(mapWidth.getValue(), mapHeight.getValue());
        Constants constants = new Constants(
                backAndForth.isSelected(),
                portalToHell.isSelected(),
                numberOfGenes.getValue(),
                minMutations.getValue(),
                maxMutations.getValue(),
                minEnergyForReproduction.getValue(),
                energyUsedForReproduction.getValue(),
                startingAnimalNumber.getValue(),
                newAnimalEnergy.getValue(),
                dailyNewGrassNumber.getValue(),
                dailyEnergyLoss.getValue(),
                energyGainFromPlant.getValue(),
                new Boundary(lowerLeft, upperRight)
        );

        ConstantsList.addToConstantsList(id, constants);
    }
    private boolean isValidData() {

        if(numberOfGenes.getValue() == 0) {
            showError("Too small gene size");
            return false;
        }
        if(minMutations.getValue() > numberOfGenes.getValue()) {
            showError("Min mutation number higher than length of gene");
            return false;
        }
        if(maxMutations.getValue() > numberOfGenes.getValue()) {
            showError("Max mutation number higher than length of gene");
            return false;
        }
        if(maxMutations.getValue() < minMutations.getValue()) {
            showError("Max mutation number lower than min mutation number");
            return false;
        }
        if(minEnergyForReproduction.getValue() < energyUsedForReproduction.getValue()) {
            showError("Energy to be ready for reproduction is lower than used during reproduction");
            return false;
        }
        if(startingAnimalNumber.getValue() < 2) {
            showError("Starting animal number is too little. Need at least 2 animals to survive");
            return false;
        }
        if(energyGainFromPlant.getValue() == 0) {
            showError("Plants should give some energy when eaten");
            return false;
        }
        if(mapWidth.getValue() == 0) {
            showError("Map width has to be at least 1");
            return false;
        }
        if(mapHeight.getValue() == 0) {
            showError("Map height has to be at least 1");
            return false;
        }

//      If nothing goes wrong, then the data is valid!
        return true;
    }
    public void handleNewWindow() throws IOException, InterruptedException {
        if(!isValidData()) return;
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
