package mb.company.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mb.company.Main;
import mb.company.dao.ComputerDAO;
import mb.company.dao.StationDAO;
import mb.company.entities.Computer;
import mb.company.entities.Station;
import mb.company.exceptions.ComputerNotFoundInStationException;
import mb.company.exceptions.StationNotFoundException;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


public class StationDetailController {

    private static final Logger log = LoggerFactory.getLogger(StationDetailController.class);

    @FXML
    private Label idLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Computer> computersTableView;

    @FXML
    private TableColumn<Computer, Integer> idColumn;

    @FXML
    private TableColumn<Computer, String> ipAddressColumn;

    @FXML
    private TableColumn<Computer, String> computerTypeColumn;

    private int stationId;

    public void setStationId(int stationId) {
        this.stationId = stationId;
        /*
        this is because of mb.company.controllers.StationsController.showStationDetail method;
        before this, there was an initialization method, that set "0" to stationId automatically, so this is a workaround for the problem
         */
        loadData();
    }


    private void loadData() {
        try {
            StationDAO stationDAO = new StationDAO();
            Station station = stationDAO.getStationById(stationId);
            setStation(station);
            loadComputers();
        } catch (StationNotFoundException | SQLException e) {
            log.error("Error during getting data from DB: {}", e.getMessage());
            nameLabel.setText("Data loading error!");
        }
    }

    private void loadComputers() {
        try {
            ComputerDAO computerDAO = new ComputerDAO();
            List<Computer> computers = computerDAO.getComputersByStationId(stationId);
            ObservableList<Computer> computerData = FXCollections.observableArrayList(computers);
            computersTableView.setItems(computerData);
        } catch (SQLException | ComputerNotFoundInStationException e) {
            log.error("Error loading computers: {}", e.getMessage());
        }
    }

    public void setStation(Station station) {
        idLabel.setText("Id: " + station.getId());
        nameLabel.setText("Name: " + station.getName());
        addressLabel.setText("Address: " + station.getAddress());
        countryLabel.setText("Country: " + station.getCountry());
    }

    @FXML
    private void backToStations() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/stations-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        computerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("computerType"));
        TableColumn<Computer, Void> computerActionColumn = new TableColumn<>("Action Buttons");
        computerActionColumn.setCellFactory(getComputerButtonCellFactory());
        computersTableView.getColumns().add(computerActionColumn);
    }

    private Callback<TableColumn<Computer, Void>, TableCell<Computer, Void>> getComputerButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Computer, Void> call(final TableColumn<Computer, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");

                    {
                        editButton.setOnAction(event -> editComputer(getTableView().getItems().get(getIndex())));
                        deleteButton.setOnAction(event -> deleteComputer(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(editButton, deleteButton);
                            hbox.setSpacing(10);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    private void editComputer(Computer computer) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/add-computer-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AddComputerController controller = fxmlLoader.getController();
            controller.setComputerId(computer.getId()); // Předání ID počítače
            Stage stage = (Stage) computersTableView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            log.error("Error during loading editComputer form: {}", e.getMessage());
            showAlert("Error during loading editComputer form");
        }
    }

    private void deleteComputer(Computer computer) {
        try {
            ComputerDAO computerDAO = new ComputerDAO();
            computerDAO.deleteComputer(computer.getId());
            computersTableView.getItems().remove(computer);
            showAlert("The computer was removed.");
        } catch (SQLException e) {
            log.error("Error during removing of computer: {}", e.getMessage());
            showAlert("Error during removing of computer.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
