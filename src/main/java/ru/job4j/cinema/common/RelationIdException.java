package ru.job4j.cinema.common;

/**
 * An exception is thrown if a related object is not found for one of the object
 * IDs received from the store.
 */
public class RelationIdException extends Exception {

    public RelationIdException() {
    }

    public RelationIdException(String message) {
        super(message);
    }
}
