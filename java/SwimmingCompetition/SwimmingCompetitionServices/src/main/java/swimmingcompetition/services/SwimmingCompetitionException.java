package swimmingcompetition.services;


public class SwimmingCompetitionException extends Exception{
    public SwimmingCompetitionException() {
    }

    public SwimmingCompetitionException(String message) {
        super(message);
    }

    public SwimmingCompetitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
