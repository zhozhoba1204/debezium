package com.blinov.kafka.utils;

import com.blinov.kafka.model.DocumentEntity;
import com.blinov.kafka.model.UserEntity;
import com.blinov.kafka.repo.DocumentRepository;
import com.blinov.kafka.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomDeserializerUtil {
    private final UserRepository userRepository;

    private final DocumentRepository documentRepository;

    public String parseMessage(String message) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(message);

        JSONObject source = (JSONObject) jsonObject.get("source");
        return (String) source.get("table");
    }

    public void parseFromTableUser(String message) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
        String op = (String) jsonObject.get("op");
        if (op.equals("c") || op.equals("u")) {
            UserEntity user = parseUser(jsonObject);
            userRepository.save(user);
        } else if (op.equals("d")) {
            JSONObject before = (JSONObject) jsonObject.get("before");
            UUID id = UUID.fromString((String) before.get("id"));
            userRepository.deleteById(id);
        }
    }

    public void parseFromTableDocument(String message) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
        String op = (String) jsonObject.get("op");
        if (op.equals("c") || op.equals("u")) {
            DocumentEntity document = parseDocument(jsonObject);
            documentRepository.save(document);
        } else if (op.equals("d")) {
            JSONObject before = (JSONObject) jsonObject.get("before");
            UUID id = UUID.fromString((String) before.get("id"));
            documentRepository.deleteById(id);
        }
    }

    public UserEntity parseUser(JSONObject jsonObject) {

        JSONObject after = (JSONObject) jsonObject.get("after");
        UUID id = UUID.fromString((String) after.get("id"));

        String name = (String) after.get("name");

        String localDate = (String) after.get("birth_date");
        LocalDate birthDate = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'"));

        return new UserEntity(id, name, birthDate);
    }

    public DocumentEntity parseDocument(JSONObject jsonObject) {

        JSONObject after = (JSONObject) jsonObject.get("after");

        UUID id = UUID.fromString((String) after.get("id"));

        String name = (String) after.get("name");

        String stringCreatedDate = (String) after.get("created_date");
        LocalDate createdDate = LocalDate.parse(stringCreatedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'"));

        String stringUpdatedDate = (String) after.get("updated_date");
        LocalDate updatedDate = LocalDate.parse(stringUpdatedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'"));

        return new DocumentEntity(id, name, createdDate, updatedDate);
    }

}
