package com.ssi;

public class FileAlreadyExistException extends Exception {

    public FileAlreadyExistException(String message){
        super(message);
    }
}
