package org.denys.hudymov.model;

import lombok.Data;


public class Validator  {
    public static void validateRoomNumber(String number) throws IllegalArgumentException{
        if(number.isEmpty()|| number.contains("Example:")){
            throw new IllegalArgumentException("You need to indicate the room number!");
        }
    }

    public static void validateNumberOfSeats(String seatsNumber) throws IllegalArgumentException{
        if(seatsNumber.isEmpty()){
            throw new IllegalArgumentException("You need to indicate the number of seats in the room!");
        }
    }

    public static void validateComfort(String comfort) throws IllegalArgumentException{
        if(comfort.contains("─")){
            throw new IllegalArgumentException("You need to chose the room comfort");
        }
    }

    public static void validatePrice(String price) throws IllegalArgumentException{
        if(price.isEmpty()||  Integer.parseInt(price)<=0) {
            throw new IllegalArgumentException("The price must be greater than 0!");
        }
    }
}
