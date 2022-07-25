package com.blinov.kafka.service;

import org.json.simple.parser.ParseException;

public interface UserService {
    void consume(String message) throws ParseException;
}
