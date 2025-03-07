package mb.company.exceptions;

public class ComputerNotFoundInStationException extends Exception{

    public ComputerNotFoundInStationException(int stationId) {
        super("There are no computers in station with ID " + stationId + ".");
    }

}
