package com.example.scoreboard.exception;

public class NegativeScoreException extends RuntimeException{

    public NegativeScoreException(String message) {
        super(message);
    }
}
