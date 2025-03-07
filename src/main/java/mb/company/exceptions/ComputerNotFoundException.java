package mb.company.exceptions;

public class ComputerNotFoundException extends Exception{

    public ComputerNotFoundException(int computerId) {
        super("Computer with ID " + computerId + " not found.");
    }

}
