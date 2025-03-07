package mb.company.exceptions;

public class StationNotFoundException extends Exception {

    public StationNotFoundException(int stationId) {
        super("Station with ID " + stationId + " not found.");
    }

}
