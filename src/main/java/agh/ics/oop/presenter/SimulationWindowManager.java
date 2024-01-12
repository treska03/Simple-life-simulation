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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationWindowManager {

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
    private void setFields(String[] vals) {
        backAndForth.setSelected(Boolean.parseBoolean(vals[0]));
        portalToHell.setSelected(Boolean.parseBoolean(vals[1]));
        numberOfGenes.setValue(Integer.parseInt(vals[2]));
        minMutations.setValue(Integer.parseInt(vals[3]));
        maxMutations.setValue(Integer.parseInt(vals[4]));
        minEnergyForReproduction.setValue(Integer.parseInt(vals[5]));
        energyUsedForReproduction.setValue(Integer.parseInt(vals[6]));
        startingAnimalNumber.setValue(Integer.parseInt(vals[7]));
        newAnimalEnergy.setValue(Integer.parseInt(vals[8]));
        dailyNewGrassNumber.setValue(Integer.parseInt(vals[9]));
        dailyEnergyLoss.setValue(Integer.parseInt(vals[10]));
        energyGainFromPlant.setValue(Integer.parseInt(vals[11]));
        mapWidth.setValue(Integer.parseInt(vals[12]));
        mapHeight.setValue(Integer.parseInt(vals[13]));
    }

    public void fileSelector() {
        Stage newWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(newWindow);
        if(file == null) {
            return;
        }

        String[] lines = new String[15];
        int i = 0;
        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                if(i >= 15) {
                    break;
                }
                String line = scanner.nextLine();
                lines[i] = line;
                i++;
            }
        }
        catch (IOException e) {
            showError("Wrong file format selected. Make sure config is alright");
            return;
        }

        if(i != 14) {
            showError("Wrong file length. File should be 14 lines long!");
            return;
        }

        try {
            setFields(lines);
        } catch (NumberFormatException e) {
            showError("Wrong file format. Some values are not integers");
        }

    }

    public void saveNewConfig() {
        if(!isValidData()) {
            showError("Make sure to provide valid data before saving");
            return;
        }
        File newFile = new File("simulationConfig.txt");
        saveConfigFileContent(newFile);
    }

    private void saveConfigFileContent(File emptyFile) {
        try(FileWriter writer = new FileWriter(emptyFile)) {
            writer.write(String.valueOf(backAndForth.isSelected() + "\n"));
            writer.write(String.valueOf(portalToHell.isSelected()) + "\n");
            writer.write(numberOfGenes.getValue() + "\n");
            writer.write(minMutations.getValue() + "\n");
            writer.write(maxMutations.getValue() + "\n");
            writer.write(minEnergyForReproduction.getValue() + "\n");
            writer.write(energyUsedForReproduction.getValue() + "\n");
            writer.write(startingAnimalNumber.getValue() + "\n");
            writer.write(newAnimalEnergy.getValue() + "\n");
            writer.write(dailyNewGrassNumber.getValue() + "\n");
            writer.write(dailyEnergyLoss.getValue() + "\n");
            writer.write(energyGainFromPlant.getValue() + "\n");
            writer.write(mapWidth.getValue() + "\n");
            writer.write(String.valueOf(mapHeight.getValue()));
        } catch (IOException e) {
            showError("Something went wrong");
            return;
        }
    }
    public void handleNewWindow() throws IOException, InterruptedException {
        if(!isValidData()) return;
        int id = simulationId.getAndIncrement();
        setUpConstants(id);
        Stats stats = new Stats(id);
        StatsList.addToStatsList(id, stats);

        SimulationPresenter windowPresenter = new SimulationPresenter();
        Stage stage = app.startNewWindow(windowPresenter);
        windowPresenter.setUp(id, stage);

        windowPresenter.run();
    }
}
