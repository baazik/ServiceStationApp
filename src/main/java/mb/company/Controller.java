package mb.company;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class Controller {

    @FXML
    private void handleButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Tlačítko bylo kliknuto!");
        alert.showAndWait();
    }

}
