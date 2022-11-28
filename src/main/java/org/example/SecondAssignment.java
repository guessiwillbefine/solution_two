package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.FirstAssignment.ROOT_PATH;


public class SecondAssignment {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
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
                        Collectors.summarizingInt(Violation::getFineAmount)))
                .entrySet()
                .stream()
                .map(x -> new ViolationDTO(x.getKey(), x.getValue().getSum()))
                .toList();
    }

    private static List<Violation> parseXML(File file) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(
                new StreamSource(file.getName()));
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                QName qName = event.asStartElement().getName();
            }
        }
        return null;
    }

    private static void objectToJson(List<ViolationDTO> violations) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writer().writeValue(new File("output_2.json"), violations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void objectToXML(List<ViolationDTO> violations) {
        XmlMapper mapper = new XmlMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writer().writeValue(new File("output_2.xml"), violations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
