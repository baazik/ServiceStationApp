package mb.company.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mb.company.Main;
import mb.company.dao.StationDAO;
import mb.company.entities.Station;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import mb.company.exceptions.StationLimitExceededException;
import mb.company.exceptions.StationNotFoundException;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddStationController {

    private static final Logger log = LoggerFactory.getLogger(AddStationController.class);

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField countryTextField;

    private int stationId;

    public void setStationId(int stationId) {
        this.stationId = stationId;
        if (stationId != 0) {
            try {
                StationDAO stationDAO = new StationDAO();
                Station station = stationDAO.getStationById(stationId);
                nameTextField.setText(station.getName());
                addressTextField.setText(station.getAddress());
                countryTextField.setText(station.getCountry());
            } catch (SQLException | StationNotFoundException e) {
                log.error("Station not found: {}", e.getMessage());
                showAlert("Error during loading of the station.");
            }
        }
    }

    @FXML
    private void saveStation() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String country = countryTextField.getText();

        try {
            StationDAO stationDAO = new StationDAO();
            if (stationId != 0) {
                Station station = new Station(stationId, name, address, country);
                stationDAO.updateStation(station);
                showAlert("The station was updated.");
            } else {
                Station station = new Station(name, address, country);
                stationDAO.createStation(station);
                showAlert("The station was created.");
            }
        } catch (SQLException e) {
            log.error("Error during loading of the station: {}", e.getMessage());
            showAlert("Error during loading of the station.");
        } catch (StationLimitExceededException e) {
            log.error(e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.setScene(scene);
    }


}
