package com.mediscreen.history.Exception;

import com.mediscreen.history.model.History;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HistoryNotFoundException extends RuntimeException{
    public HistoryNotFoundException(String s) {
        super(s);
    }
}