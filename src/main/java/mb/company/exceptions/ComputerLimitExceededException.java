package mb.company.exceptions;

public class ComputerLimitExceededException extends Exception {

    public ComputerLimitExceededException(int computerLimit) {
        super("The limit of " + computerLimit + " for the station is exceeded");
    }
}
