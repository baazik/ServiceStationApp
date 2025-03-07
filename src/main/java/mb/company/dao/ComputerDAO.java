package mb.company.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mb.company.db.DatabaseManager;
import mb.company.entities.Computer;
import mb.company.entities.Station;
import mb.company.enums.ComputerType;
import mb.company.exceptions.ComputerLimitExceededException;
import mb.company.exceptions.ComputerNotFoundException;
import mb.company.exceptions.ComputerNotFoundInStationException;
import mb.company.exceptions.StationNotFoundException;

public class ComputerDAO {

    private static final ResourceBundle properties = ResourceBundle.getBundle("application");
    private static final int COMPUTER_LIMIT = Integer.parseInt(properties.getString("computer.limit"));

    private static final Logger log = LoggerFactory.getLogger(ComputerDAO.class);

    /*
    This method was not used in the final version, because computers are shown only for certain station.
    But it could be used later.
     */
    public List<Computer> getAllComputers() throws SQLException, StationNotFoundException {
        List<Computer> computers = new ArrayList<>();
        String sql = "SELECT id, ip_adress, computer_type, station_id FROM computer";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                computers.add(new Computer(
                        rs.getInt("id"),
                        rs.getString("ip_adress"),
                        ComputerType.fromCode(rs.getString("computer_type")),
                        new StationDAO().getStationById(rs.getInt("station_id")) // Načtení stanice
                ));
            }
        }
        return computers;
    }

    public void createComputer(Computer computer) throws SQLException, ComputerLimitExceededException, ComputerNotFoundInStationException {
        if (getComputersByStationId(computer.getStation().getId()).size() >= COMPUTER_LIMIT) {
            throw new ComputerLimitExceededException(COMPUTER_LIMIT);
        }

        String sql = "INSERT INTO computer (ip_adress, computer_type, station_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, computer.getIpAddress());
            pstmt.setString(2, computer.getComputerType().getCode());
            pstmt.setInt(3, computer.getStation().getId());
            pstmt.executeUpdate();
        }
    }

    public void updateComputer(Computer computer) throws SQLException {
        String sql = "UPDATE computer SET ip_adress = ?, computer_type = ?, station_id = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, computer.getIpAddress());
            pstmt.setString(2, computer.getComputerType().getCode());
            pstmt.setInt(3, computer.getStation().getId());
            pstmt.setInt(4, computer.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteComputer(int computerId) throws SQLException {
        String sql = "DELETE FROM computer WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, computerId);
            pstmt.executeUpdate();
        }
    }

    public Computer getComputerById(int computerId) throws SQLException, ComputerNotFoundException, StationNotFoundException {
        String sql = "SELECT id, ip_adress, computer_type, station_id FROM computer WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, computerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Computer(
                            rs.getInt("id"),
                            rs.getString("ip_adress"),
                            ComputerType.fromCode(rs.getString("computer_type")),
                            new StationDAO().getStationById(rs.getInt("station_id")) // Načtení stanice
                    );
                }
            }
        }

        log.error("Computer with ID {} was not found in DB.", computerId);
        throw new ComputerNotFoundException(computerId);
    }

    public List<Computer> getComputersByStationId(int stationId) throws SQLException, ComputerNotFoundInStationException {
        List<Computer> computers = new ArrayList<>();
        String sql = "SELECT c.id, c.ip_adress, c.computer_type, s.id AS station_id, s.name, s.address, s.country " +
                "FROM computer c " +
                "JOIN station s ON c.station_id = s.id " +
                "WHERE c.station_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Station station = new Station(
                            rs.getInt("station_id"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("country")
                    );
                    Computer computer = new Computer(
                            rs.getInt("id"),
                            rs.getString("ip_adress"),
                            ComputerType.fromCode(rs.getString("computer_type")),
                            station
                    );
                    computers.add(computer);
                }
            }
        }
        return computers;
    }


}
