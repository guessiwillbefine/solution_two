package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class SecondAssignment {
    private static final String ROOT_PATH = "src/main/resources/secondAssignmentResources/";
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) throw new IllegalArgumentException("haven't received any args, expected file name");
        String path = ROOT_PATH + args[0];
        File file = new File(Path.of(path).toUri());
        if (!file.exists()) throw new FileNotFoundException("file was not found : " + args[0]);
        List<Violation> violations;
        if (file.getName().split("\\.")[1].equals("xml")) {
            violations = parseXML(file);
            objectToXML(convert(violations));
        } else {
            violations = parseJSON(file);
            objectToJson(convert(violations));
        }
    }

    private static List<Violation> parseXML(File file) {
        List<Violation> participantJsonList;
        XmlMapper mapper = new XmlMapper();
        try {
            participantJsonList = mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return participantJsonList;
    }

    private static List<Violation> parseJSON(File file) {
        List<Violation> participantJsonList;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            participantJsonList = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return participantJsonList;
    }

    private static List<ViolationDTO> convert(List<Violation> violations) {
        return violations.stream()
                .collect(Collectors.groupingBy(Violation::getType,
                        Collectors.summarizingDouble(Violation::getFineAmount)))
                .entrySet()
                .stream()
                .map(x -> new ViolationDTO(x.getKey(), x.getValue().getSum()))
                .toList();
    }


    private static void objectToJson(List<ViolationDTO> violations) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writer().writeValue(new File(ROOT_PATH + "output_2.json"), violations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void objectToXML(List<ViolationDTO> violations) {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writer().writeValue(new File(ROOT_PATH + "output_2.xml"), violations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
