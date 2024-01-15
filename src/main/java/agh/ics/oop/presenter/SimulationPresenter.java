package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.ChangeListener;
import agh.ics.oop.model.creatures.Animal;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
    static final int CELL_WIDTH = 37;
    static final int CELL_HEIGHT = 37;
    private Simulation simulation;
    private int simulationId;
    private Stage stage;
    private WorldMap worldMap;
    private Constants constants;
    private Stats stats;
    private SimulationEngine engine;
    private SimulationApp app;
    private Animal markedAnimal;
    private final HashMap<Vector2d, Button> buttonMap = new HashMap<>();
    private HashSet<Animal> animalsOnBlue;


    public void run() throws InterruptedException {
        engine.runAsync();
    }

    public SimulationPresenter() {}

    public void setUp(int simulationId, SimulationApp app, Stage stage) {
        this.app = app;

        this.simulationId = simulationId;
        this.stage = stage;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);

        this.simulation = new Simulation(this.simulationId);
        this.worldMap = simulation.getGameMap();

        this.engine = getSimulationEngine(simulation);
        Platform.runLater(() -> {
            changeStats(stats);
            drawMap(worldMap);
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
        genotype0.setText("Genotyp 0: " + stats.getNumberOfEachGenotype()[0]);
        genotype1.setText("Genotyp 1: " + stats.getNumberOfEachGenotype()[1]);
        genotype2.setText("Genotyp 2: " + stats.getNumberOfEachGenotype()[2]);
        genotype3.setText("Genotyp 3: " + stats.getNumberOfEachGenotype()[3]);
        genotype4.setText("Genotyp 4: " + stats.getNumberOfEachGenotype()[4]);
        genotype5.setText("Genotyp 5: " + stats.getNumberOfEachGenotype()[5]);
        genotype6.setText("Genotyp 6: " + stats.getNumberOfEachGenotype()[6]);
        genotype7.setText("Genotyp 7: " + stats.getNumberOfEachGenotype()[7]);
    }

    private Label createGridItem(String content, Vector2d position){
        Label item = new Label(String.valueOf(content));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHalignment(item, HPos.CENTER);
        GridPane.setFillHeight(item, true);
        GridPane.setFillWidth(item, true);
        item.setAlignment(Pos.CENTER);
        if (position != null && constants.getJungleBoundary().insideBoundary(position)){
            item.setStyle("-fx-background-color: green; -fx-text-fill: black;");
        }
        else if (position != null){
            item.setStyle("-fx-text-fill: black; -fx-border-color: grey;");
        }
        else {
            item.setStyle("-fx-background-color: darkgrey; -fx-border-color: grey;");
        }
        return item;
    }

    private Label createJungleField(){
        Label item = new Label();
        item.setStyle("-fx-background-color: green; -fx-border-color: grey;");
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHalignment(item, HPos.CENTER);
        GridPane.setFillHeight(item, true);
        GridPane.setFillWidth(item, true);
        return item;
    }

    private Button createGridButton(String content, List<Animal> listOfAnimals) {
        Button button = new Button(content);
        button.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Vector2d position = listOfAnimals.get(0).getPosition();
        animalsOnBlue = stats.getPopularGenotypeAnimals();
        boolean foundYellow = false;
        boolean foundBlue = false;
        for (Animal animal : listOfAnimals){
            if (markedAnimal != null && markedAnimal.getId() == animal.getId()){
                foundYellow = true;
            }
            if (animalsOnBlue.contains(animal)){
                foundBlue = true;
            }
        }
        if (foundYellow && foundBlue){
            button.setStyle("-fx-background-color: yellow; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (foundYellow) {
            button.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-border-color: grey;");
        }
        else if (foundBlue && constants.getJungleBoundary().insideBoundary(position)) {
            button.setStyle("-fx-background-color: forestgreen; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (foundBlue) {
            button.setStyle("-fx-background-color: lightgrey; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (constants.getJungleBoundary().insideBoundary(position)) {
            button.setStyle("-fx-background-color: forestgreen; -fx-text-fill: black; -fx-border-color: grey;");
        }
        else {
            button.setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-border-color: grey;");
        }
        button.setOnAction(event -> {
            Platform.runLater(() -> {
                try {
                    handleButtonClick(button, content, listOfAnimals.get(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        buttonMap.put(listOfAnimals.get(0).getPosition(), button);
        return button;
    }

    private void handleButtonClick(Button button, String content, Animal animal) throws IOException {
        if (simulation.isPaused()){
            if (content.equals("#")){
                handleNewWindow(animal.getPosition());
            }
            else {
                if (markedAnimal == animal){
                    setMarkedAnimal(null);
                }
                else {
                    setMarkedAnimal(animal);
                }
            }
        }
    }

    public void drawMap(WorldMap worldMap) {
        buttonMap.clear();
        clearGrid();

        Boundary bounds = constants.getMapBoundary();
        Vector2d topRight = bounds.upperRight();
        Vector2d bottomLeft = bounds.lowerLeft();
        int gridWidth = topRight.getX() - bottomLeft.getX() + 2;
        int gridHeight = topRight.getY() - bottomLeft.getY() + 2;
        mapGrid.add(createGridItem("y\\x", null),0,0);
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        int leftStartingPoint = bottomLeft.getX();
        for(int i = 1; i < gridWidth; i++){
            mapGrid.add(createGridItem(String.valueOf(leftStartingPoint), null),i,0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            leftStartingPoint++;
        }
        int upperStartingPoint = topRight.getY();
        for(int i = 1; i < gridHeight; i++){
            mapGrid.add(createGridItem(String.valueOf(upperStartingPoint), null),0,i);
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            upperStartingPoint--;
        }
        for(int i = bottomLeft.getY(); i<= topRight.getY(); i++){
            for(int j = bottomLeft.getX(); j <= topRight.getX(); j++){
                Vector2d vec = new Vector2d(j,i);
                if(worldMap.objectAt(vec) != null && worldMap.objectAt(vec).equals("*")){
                    mapGrid.add(createGridItem(worldMap.objectAt(vec), vec),j-bottomLeft.getX()+1,gridHeight-(i-bottomLeft.getY())-1);
                }
                else if (worldMap.objectAt(vec) != null) {
                    Button button = createGridButton(worldMap.objectAt(vec), worldMap.getAnimalPositions().get(vec));
                    mapGrid.add(button, j - bottomLeft.getX() + 1, gridHeight - (i - bottomLeft.getY()) - 1);
                }
                else if (constants.getJungleBoundary().insideBoundary(vec)){
                    mapGrid.add(createJungleField(),j-bottomLeft.getX()+1,gridHeight-(i-bottomLeft.getY())-1);
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
        engine.endSimulation(this.simulation);
    }

    public void handleNewWindow(Vector2d position) throws IOException {
        AnimalsList animalsList = new AnimalsList();
        Stage stage = app.startAnimalsList(animalsList);
        animalsList.setUp(worldMap.getAnimalPositions().get(position), this, markedAnimal);
    }

    public void setMarkedAnimal(Animal markedAnimal) {
        if (this.markedAnimal != null){
            Button oldButton = buttonMap.get(this.markedAnimal.getPosition());
            boolean inside = constants.getJungleBoundary().insideBoundary(this.markedAnimal.getPosition());
            if (animalsOnBlue.contains(this.markedAnimal) && inside){
                oldButton.setStyle("-fx-background-color: forestgreen; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else if (animalsOnBlue.contains(this.markedAnimal)){
                oldButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else if (inside){
                oldButton.setStyle("-fx-background-color: forestgreen; -fx-text-fill: black; -fx-border-color: grey;");
            }
            else {
                oldButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-border-color: grey;");
            }
        }
        this.markedAnimal = markedAnimal;
        if (this.markedAnimal != null) {
            Button newButton = buttonMap.get(this.markedAnimal.getPosition());
            if (animalsOnBlue.contains(this.markedAnimal)){
                newButton.setStyle("-fx-background-color: yellow; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else {
                newButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-border-color: grey;");
            }
        }
        if (this.markedAnimal == null){
            stats.deleteMark();
        }
        else {
            stats.addMark(this.markedAnimal);
        }
    }
}