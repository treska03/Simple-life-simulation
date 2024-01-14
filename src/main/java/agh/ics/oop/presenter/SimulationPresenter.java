package agh.ics.oop.presenter;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.ChangeListener;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class SimulationPresenter implements ChangeListener {

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button resumeButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Label numberOfPlantsLabel = new Label();
    @FXML
    private Label numberOfAnimalsLabel = new Label();
    @FXML
    private Label numberOfEmptyFieldsLabel = new Label();
    @FXML
    private Label  averageEnergyLabel = new Label();
    @FXML
    private Label averageDaysOfLivingLabel = new Label();
    @FXML
    private Label averageChildrenNumberLabel = new Label();
    @FXML
    private VBox averageEnergyVBox = new VBox();
    @FXML
    private VBox averageDaysOfLivingVBox = new VBox();
    @FXML
    private VBox averageChildrenNumberVBox = new VBox();
    @FXML
    private Label  genotypesText;
    @FXML
    private Label  genotype0 = new Label();
    @FXML
    private Label  genotype1 = new Label();
    @FXML
    private Label  genotype2 = new Label();
    @FXML
    private Label  genotype3 = new Label();
    @FXML
    private Label  genotype4 = new Label();
    @FXML
    private Label  genotype5 = new Label();
    @FXML
    private Label  genotype6 = new Label();
    @FXML
    private Label  genotype7 = new Label();
    static final int CELL_WIDTH = 30;
    static final int CELL_HEIGHT = 30;
    private Simulation simulation;
    private int simulationId;
    private Stage stage;
    private WorldMap worldMap;
    private Constants constants;
    private Stats stats;
    private SimulationEngine engine;


    public void run() throws InterruptedException {
        engine.runAsync();
    }

    public SimulationPresenter() {}

    public void setUp(int simulationId, Stage stage) {
        this.simulationId = simulationId;
        this.stage = stage;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);

        this.simulation = new Simulation(this.simulationId);
        this.worldMap = simulation.getGameMap();

        this.engine = getSimulationEngine(simulation);
        Platform.runLater(() -> {
            this.genotypesText.setVisible(false);
        });
        resumeButton.setOnAction((e) -> simulation.resumeGame());
        pauseButton.setOnAction((e) -> simulation.pauseGame());

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::exitApplication);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message, Stats stats) {
        if(simulationId == -1) throw new RuntimeException("Didn't set-up UI presenter before calling mapChanged");
        Platform.runLater(() -> {
            changeStats(stats);
            drawMap(worldMap);
        });
    }

    private void changeStats (Stats stats){
        numberOfAnimalsLabel.setText("Liczba zyjacyh zwierzat: " + stats.getNumberOfLiveAnimals());
        numberOfPlantsLabel.setText("Aktualna liczba roslin: " + stats.getNumberOfPlants());
        numberOfEmptyFieldsLabel.setText("Aktualna liczba pustych pol: " + stats.getNumberOfEmptyFields());
        averageEnergyLabel.setText("Sredni poziom energii dla zyjacych zwierzat: " + stats.getAverageEnergy());
        averageEnergyVBox.setVisible(stats.getAverageEnergy() != -1);
        averageEnergyVBox.setManaged(stats.getAverageEnergy() != -1);
        averageDaysOfLivingLabel.setText("Srednia dlugosc zycia dla wszystkich martwych zwierzat: " + stats.getAverageDaysOfLiving());
        averageDaysOfLivingVBox.setVisible(stats.getAverageDaysOfLiving() != -1);
        averageDaysOfLivingVBox.setManaged(stats.getAverageDaysOfLiving() != -1);
        averageChildrenNumberLabel.setText("Srednia liczba dzieci dla zyjacych zwierzat: " + stats.getAverageChildrenNumber());
        averageChildrenNumberVBox.setVisible(stats.getAverageChildrenNumber() != -1);
        averageChildrenNumberVBox.setManaged(stats.getAverageChildrenNumber() != -1);
        genotypesText.setVisible(true);
        genotype0.setText("Genotyp 0: " + stats.getNumberOfEachGenotype()[0]);
        genotype1.setText("Genotyp 1: " + stats.getNumberOfEachGenotype()[1]);
        genotype2.setText("Genotyp 2: " + stats.getNumberOfEachGenotype()[2]);
        genotype3.setText("Genotyp 3: " + stats.getNumberOfEachGenotype()[3]);
        genotype4.setText("Genotyp 4: " + stats.getNumberOfEachGenotype()[4]);
        genotype5.setText("Genotyp 5: " + stats.getNumberOfEachGenotype()[5]);
        genotype6.setText("Genotyp 6: " + stats.getNumberOfEachGenotype()[6]);
        genotype7.setText("Genotyp 7: " + stats.getNumberOfEachGenotype()[7]);
    }

    private Label createGridItem(String content){

        Label item = new Label(String.valueOf(content));
        GridPane.setHalignment(item, HPos.CENTER);
        return item;
    }

    public void drawMap(WorldMap worldMap) {
        clearGrid();

        Boundary bounds = constants.getMapBoundary();
        Vector2d topRight = bounds.upperRight();
        Vector2d bottomLeft = bounds.lowerLeft();
        int gridWidth = topRight.getX() - bottomLeft.getX() + 2;
        int gridHeight = topRight.getY() - bottomLeft.getY() + 2;
        mapGrid.add(createGridItem("y\\x"),0,0);
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        int leftStartingPoint = bottomLeft.getX();
        for(int i = 1; i < gridWidth; i++){
            mapGrid.add(createGridItem(String.valueOf(leftStartingPoint)),i,0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            leftStartingPoint++;
        }
        int upperStartingPoint = topRight.getY();
        for(int i = 1; i < gridHeight; i++){
            mapGrid.add(createGridItem(String.valueOf(upperStartingPoint)),0,i);
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            upperStartingPoint--;
        }
        for(int i = bottomLeft.getY(); i<= topRight.getY(); i++){
            for(int j = bottomLeft.getX(); j <= topRight.getX(); j++){
                Vector2d vec = new Vector2d(j,i);
                if(worldMap.objectAt(vec) != null){
                    mapGrid.add(createGridItem(worldMap.objectAt(vec).toString()),j-bottomLeft.getX()+1,gridHeight-(i-bottomLeft.getY())-1);
                }
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private SimulationEngine getSimulationEngine(Simulation simulation) {
        worldMap.addObserver(this);
        worldMap.addObserver(new ConsoleMapDisplay());

        return new SimulationEngine(List.of(simulation));

    }

    public void exitApplication(WindowEvent event) {
        Platform.exit();
        engine.endSimulation(this.simulation);
    }

}