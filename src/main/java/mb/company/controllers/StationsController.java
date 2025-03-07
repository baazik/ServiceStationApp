package mb.company.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mb.company.Main;
import mb.company.dao.StationDAO;
import mb.company.entities.Station;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.util.Callback;

public class StationsController {

    private static final Logger log = LoggerFactory.getLogger(StationsController.class);

    @FXML
    private TableView<Station> stationsTableView;

    @FXML
    private TableColumn<Station, Integer> idColumn;

    @FXML
    private TableColumn<Station, String> nameColumn;

    @FXML
    private TableColumn<Station, String> addressColumn;

    @FXML
    private TableColumn<Station, String> countryColumn;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<Station, Void> actionColumn = new TableColumn<>("Action Buttons");
        actionColumn.setCellFactory(getButtonCellFactory());
        stationsTableView.getColumns().add(actionColumn);

        try {
            StationDAO stationDAO = new StationDAO();
            List<Station> stations = stationDAO.getAllStations();
            ObservableList<Station> stationData = FXCollections.observableArrayList(stations);
            stationsTableView.setItems(stationData);
        } catch (SQLException e) {
            log.error("Error during loading of stations: {}", e.getMessage());
            showAlert("Error during loading of stations.");
        }
    }

    private Callback<TableColumn<Station, Void>, TableCell<Station, Void>> getButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Station, Void> call(final TableColumn<Station, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final Button detailButton = new Button("Detail");

                    {
                        editButton.setOnAction(event -> editStation(getTableView().getItems().get(getIndex())));
                        deleteButton.setOnAction(event -> deleteStation(getTableView().getItems().get(getIndex())));
                        detailButton.setOnAction(event -> {
                            try {
                                showStationDetail(getTableView().getItems().get(getIndex()));
                            } catch (IOException e) {
                                log.error("Error during loading of the station detail: {}", e.getMessage());
                                showAlert("Error during loading of the station detail.");
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(editButton, deleteButton, detailButton);
                            hbox.setSpacing(10);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    private void editStation(Station station) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/add-station-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AddStationController controller = fxmlLoader.getController();
            controller.setStationId(station.getId()); // Předání ID stanice
            Stage stage = (Stage) stationsTableView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            log.error("Error during loading of edit form: {}", e.getMessage());
            showAlert("Error during loading of edit form..");
        }
    }

    private void deleteStation(Station station) {
        try {
            StationDAO stationDAO = new StationDAO();
            stationDAO.deleteStation(station.getId());
            ObservableList<Station> stationData = stationsTableView.getItems();
            stationData.remove(station);
            showAlert("Station was deleted.");
        } catch (SQLException e) {
            log.error("Error during removing of the station: {}", e.getMessage());
            showAlert("Error during removing of the station.");
        }
    }

    private void showStationDetail(Station station) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/station-detail-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        StationDetailController controller = fxmlLoader.getController();
        controller.setStationId(station.getId());
        Stage stage = (Stage) stationsTableView.getScene().getWindow();
        stage.setScene(scene);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) stationsTableView.getScene().getWindow();
        stage.setScene(scene);
    }

}
