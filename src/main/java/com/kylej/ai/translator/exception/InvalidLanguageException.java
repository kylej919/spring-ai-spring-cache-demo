package com.kylej.ai.translator.exception;

/**
 * Exception thrown when an invalid language is provided to the translation service.
 */
public class InvalidLanguageException extends Exception {

    public InvalidLanguageException(String message) {
        super(message);
    }
}
