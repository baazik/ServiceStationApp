package mb.company.dao;


import mb.company.db.DatabaseManager;
import mb.company.entities.Station;
import mb.company.exceptions.StationLimitExceededException;
import mb.company.exceptions.StationNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StationDAO {

    private static final ResourceBundle properties = ResourceBundle.getBundle("application");
    private static final int STATION_LIMIT = Integer.parseInt(properties.getString("station.limit"));

    private static final Logger log = LoggerFactory.getLogger(StationDAO.class);

    public List<Station> getAllStations() throws SQLException {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT id, name, address, country FROM station";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stations.add(new Station(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("country")
                ));
            }
        }
        return stations;
    }

    public void createStation(Station station) throws SQLException, StationLimitExceededException {
        if (getAllStations().size() >= STATION_LIMIT) {
            throw new StationLimitExceededException(STATION_LIMIT);
        }

        String sql = "INSERT INTO station (name, address, country) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getAddress());
            pstmt.setString(3, station.getCountry());
            pstmt.executeUpdate();
        }
    }

    public void updateStation(Station station) throws SQLException {
        String sql = "UPDATE station SET name = ?, address = ?, country = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getAddress());
            pstmt.setString(3, station.getCountry());
            pstmt.setInt(4, station.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteStation(int stationId) throws SQLException {
        String sql = "DELETE FROM station WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            pstmt.executeUpdate();
        }
    }

    public Station getStationById(int stationId) throws StationNotFoundException, SQLException {
        String sql = "SELECT id, name, address, country FROM station WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Station(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("country")
                    );
                }
            }
        }
        log.error("Station with ID {} not found in DB.", stationId);
        throw new StationNotFoundException(stationId);
    }


}
