package mb.company;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class PrimaryController implements Initializable {

    @FXML
    private Label helloLabel;

    @FXML
    public void buttonClicked() {
        helloLabel.setText("Hello world!!!");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
