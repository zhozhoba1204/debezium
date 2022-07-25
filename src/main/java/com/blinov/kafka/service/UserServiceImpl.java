package com.blinov.kafka.service;

import com.blinov.kafka.utils.CustomDeserializerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CustomDeserializerUtil customDeserializerUtil;

    @Override
    @KafkaListener(id = "Starship", topics = {"ARCTYPE.public.t_user", "ARCTYPE.public.document"}, containerFactory = "singleFactory")
    public void consume(String message) throws ParseException {
        String table = customDeserializerUtil.parseMessage(message);

        if (table.equals("t_user")) {
            customDeserializerUtil.parseFromTableUser(message);

        } else if (table.equals("document")) {
            customDeserializerUtil.parseFromTableDocument(message);
        }
    }
}