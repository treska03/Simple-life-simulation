package agh.ics.oop;

import agh.ics.oop.presenter.AnimalsList;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.SimulationWindowManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SimulationApp extends Application {

    public void start(Stage primaryStage) throws IOException, InterruptedException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationManager.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationWindowManager manager = loader.getController();
        manager.setApp(this);

        configureStage(primaryStage, viewRoot);


        primaryStage.show();

    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public Stage startNewWindow(SimulationPresenter presenter) throws IOException {

        Stage newWindow = new Stage();
        newWindow.setTitle("Simulation running");
        FXMLLoader loader = new FXMLLoader();
        loader.setController(presenter);
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        newWindow.setScene(new Scene(loader.load()));
        newWindow.show();

        return newWindow;
    }

    public Stage startAnimalsList(AnimalsList presenter) throws IOException {
        Stage newWindow = new Stage();
        newWindow.setTitle("Choose animal");
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setController(presenter);
        loader1.setLocation(getClass().getClassLoader().getResource("animalsWindow.fxml"));
        newWindow.setScene(new Scene(loader1.load()));
        newWindow.show();

        return newWindow;
    }


}
