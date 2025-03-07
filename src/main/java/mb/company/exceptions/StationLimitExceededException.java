package mb.company.exceptions;

public class StationLimitExceededException extends Exception {

    public StationLimitExceededException(int stationLimit) {
        super("Service station limit exceeded: " + stationLimit + ".");
    }
}
