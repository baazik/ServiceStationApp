package mb.company;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mb.company.db.DatabaseManager;


public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/view/main-view.fxml"));
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
            log.error("Error during loading from DB: " + e.getMessage());
            e.printStackTrace();
        }
            launch();
        }

    }

