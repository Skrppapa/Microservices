package edu.rutmiit.demo.fitapicontract.exception;

public class CodeAlreadyExistsException extends RuntimeException {
    public CodeAlreadyExistsException(String code) {
        super("Subscription with code=" + code + " already exists");
    }
}