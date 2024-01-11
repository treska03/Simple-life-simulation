package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.*;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.map.NormalMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.util.List;

public class SimulationPresenter implements MapChangeListener{

    @FXML
    public TextField textField;
    public Button startButton;

    @FXML
    private GridPane mapGrid;
    @FXML
    private Label infoLabel;
    private final int simulationId;
    private final WorldMap worldMap;
    static final int CELL_WIDTH = 30;
    static final int CELL_HEIGHT = 30;
    private final Constants constants;
    private final Stats stats;
    private final SimulationEngine engine;


    public void onSimulationStartClicked() throws InterruptedException {
        engine.runAsync();
    }

    public SimulationPresenter() {
        int simulationId = 1; //TODO: this is temporary id, we need to pass it
                              // so that we can have more than one presenter
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);

        Simulation simulation = new Simulation(this.simulationId);
        this.worldMap = simulation.getGameMap();

        this.engine = getSimulationEngine(simulation);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            infoLabel.setText(message);
        });
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

//        IN ABOVE LINE IN INITIALIZATION OF SIMULATION, 1 IS TEMPORARY.
//        IT REPRESENTS ID OF SIMULATION
    }

}