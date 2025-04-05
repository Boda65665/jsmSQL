package org.example.Backend.Exception;

public class NotFoundTable extends RuntimeException{
    public NotFoundTable(){
        super("Not Found Table");
    }
}
