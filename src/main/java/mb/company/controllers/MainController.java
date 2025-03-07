package mb.company.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mb.company.Main;

public class MainController {

    @FXML
    private Button listStationsButton;

    @FXML
    private Button addStationButton;

    @FXML
    private Button addComputerButton;

    @FXML
    private void showStationsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/stations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) listStationsButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void showAddStationView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/add-station-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) addStationButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void showAddComputerView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/add-computer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) addComputerButton.getScene().getWindow();
        stage.setScene(scene);
    }

}
