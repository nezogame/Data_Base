package org.denys.hudymov.validator;

public class Validator {
    public static void validateTextField(String text, String source) throws IllegalArgumentException {
        if (text.isEmpty() || text.contains("Example:")) {
            throw new IllegalArgumentException("You need to write something in " + source + " field!");
        }
    }

    public static void validateNumberOfSeats(String seatsNumber) throws IllegalArgumentException {
        if (seatsNumber.isEmpty() || Integer.parseInt(seatsNumber) <= 0) {
            throw new IllegalArgumentException("The number of seats must be greater than 0!");
        }
    }

    public static void validateStays(String seatsNumber) throws IllegalArgumentException {
        if (seatsNumber.isEmpty() || Integer.parseInt(seatsNumber) < 0) {
            throw new IllegalArgumentException("The number of seats must be greater or equals than 0!");
        }
    }

    public static void validateComfort(String comfort) throws IllegalArgumentException {
        if (comfort.contains("─")) {
            throw new IllegalArgumentException("You need to chose the room comfort");
        }
    }

    public static void validatePrice(String price) throws IllegalArgumentException {
        if (price.isEmpty() || Integer.parseInt(price) <= 0) {
            throw new IllegalArgumentException("The price must be greater than 0!");
        }
    }

    public static void validateDeleteSelection(String id) throws IllegalArgumentException {
        if (id.contains("Select")) {
            throw new IllegalArgumentException("The price must be greater than 0!");
        }
    }

    public static void validatePositiveNumber(String positiveNumber) throws IllegalArgumentException {
        if (positiveNumber.isEmpty() || Integer.parseInt(positiveNumber) < 0) {
            throw new IllegalArgumentException("The number must be equals or greater than 0!");
        }
    }
}
