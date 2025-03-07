package mb.company.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mb.company.Main;
import mb.company.dao.ComputerDAO;
import mb.company.dao.StationDAO;
import mb.company.entities.Computer;
import mb.company.enums.ComputerType;
import mb.company.entities.Station;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import mb.company.exceptions.ComputerLimitExceededException;
import mb.company.exceptions.ComputerNotFoundException;
import mb.company.exceptions.ComputerNotFoundInStationException;
import mb.company.exceptions.StationNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddComputerController {

    private static final Logger log = LoggerFactory.getLogger(AddComputerController.class);

    @FXML
    private TextField ipAddressTextField;

    @FXML
    private ComboBox<ComputerType> computerTypeComboBox;

    @FXML
    private ComboBox<Station> stationComboBox;

    private int computerId; // for editing of Computer

    public void setComputerId(int computerId) {
        this.computerId = computerId;
        if (computerId != 0) {
            try {
                ComputerDAO computerDAO = new ComputerDAO();
                Computer computer = computerDAO.getComputerById(computerId);
                ipAddressTextField.setText(computer.getIpAddress());
                computerTypeComboBox.setValue(computer.getComputerType());
                stationComboBox.setValue(computer.getStation());
            } catch (SQLException | ComputerNotFoundException | StationNotFoundException e) {
                log.error("Error during loading of computer: {}", e.getMessage());
                showAlert("Error during loading of computer:");
            }
        }
    }

    @FXML
    private void initialize() {
        computerTypeComboBox.setItems(FXCollections.observableArrayList(ComputerType.values()));
        try {
            StationDAO stationDAO = new StationDAO();
            List<Station> stations = stationDAO.getAllStations();
            stationComboBox.setItems(FXCollections.observableArrayList(stations));
        } catch (SQLException e) {
            log.error("Error during loading of stations: {}", e.getMessage());
            showAlert("Error during loading of stations.");
        }
    }

    @FXML
    private void saveComputer() {
        String ipAddress = ipAddressTextField.getText();
        ComputerType computerType = computerTypeComboBox.getValue();
        Station station = stationComboBox.getValue();

        try {
            ComputerDAO computerDAO = new ComputerDAO();
            if (computerId != 0) {
                Computer computer = new Computer(computerId, ipAddress, computerType, station);
                computerDAO.updateComputer(computer);
                showAlert("Computer successfully updated.");
            } else {
                Computer computer = new Computer(ipAddress, computerType, station);
                computerDAO.createComputer(computer);
                showAlert("Computer successfully created.");
            }

        } catch (SQLException e) {
            log.error("Error during saving of computer: {}", e.getMessage());
            showAlert("Error during saving of computer.");
        } catch (ComputerNotFoundInStationException | ComputerLimitExceededException e) {
            log.error( e.getMessage());
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
        Stage stage = (Stage) ipAddressTextField.getScene().getWindow();
        stage.setScene(scene);
    }

}
