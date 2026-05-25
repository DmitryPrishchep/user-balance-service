package by.staryhroft.userbalance.exception;

public class PhoneAlreadyTakenException extends RuntimeException {
    public PhoneAlreadyTakenException(String message) {
        super(message);
    }
}