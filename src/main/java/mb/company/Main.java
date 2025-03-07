package mb.company;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mb.company.dao.ComputerDAO;
import mb.company.dao.StationDAO;
import mb.company.db.DatabaseManager;
import mb.company.entities.Computer;
import mb.company.entities.Station;
import mb.company.enums.ComputerType;

public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/main-view.fxml")); // Nahraďte "main-view.fxml" skutečným názvem vašeho FXML souboru
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        primaryStage.setTitle("Service Stations Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException, SQLException {
        try {
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, address, country FROM station");
            while (rs.next()) {
                log.info(rs.getInt("id") + ", " + rs.getString("name"));
            }
        } catch (SQLException e) {
            log.error("Chyba při načítání dat z databáze: " + e.getMessage());
            e.printStackTrace();
        }
            launch();
        }

    }

