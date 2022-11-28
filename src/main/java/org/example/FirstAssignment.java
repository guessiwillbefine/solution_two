package org.example;

import java.io.*;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//todo трабл в том что в одном случае атрибут идет со тегом в другом - без
public class FirstAssignment {
    static final String ROOT_PATH = "src/main/resources/firstAssignmentResources";
    static final String FORMAT = ".xml";
    private static final String NAME_SWAP = "name=\"%s %s\"";
    private static final Pattern PATTERN_LINE = Pattern.compile("<.+>");
    private static final Pattern PATTERN_NAME = Pattern.compile("(name=)(\"([А-Яа-яІЇії]+)\")");
    private static final Pattern PATTERN_SURNAME = Pattern.compile("( surname=)(\"([А-Яа-яІЇії]+)\")");

    private static void rewriteXML(File file) {
        try ( BufferedReader reader = new BufferedReader(new FileReader(file));
               BufferedWriter writer = new BufferedWriter(new FileWriter(ROOT_PATH + "output_" + file.getName()))){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().matches(PATTERN_LINE.pattern())) {
                    Matcher matcherSurname = PATTERN_SURNAME.matcher(line);
                    if (matcherSurname.find()) {
                        String surname = matcherSurname.group(3);
                        String newLine = line.replaceAll(PATTERN_SURNAME.pattern(), "");
                        Matcher matcherName = PATTERN_NAME.matcher(newLine);
                        if (matcherName.find()) {
                            String name = matcherName.group(3);
                            String toWrite = newLine.replaceAll(PATTERN_NAME.pattern(), String.format(NAME_SWAP, name, surname));
                            writer.append(toWrite).append("\n");
                        }
                    } else {
                        writer.append(line).append("\n");
                    }
                } else {
                    StringBuilder tag = new StringBuilder(line + "\n");
                    String temp;
                    while ((temp = reader.readLine()) != null) {
                        tag.append(temp).append("\n");
                        if (temp.matches(".+/>"))
                            break;
                    }
                    Matcher surnameMatcher = PATTERN_SURNAME.matcher(tag);
                    String surname;
                    if (surnameMatcher.find()){
                        surname = surnameMatcher.group(3);
                        String tagWithoutSurname = tag.toString().replaceAll(PATTERN_SURNAME.pattern(), "");
                        Matcher matcherName = PATTERN_NAME.matcher(tagWithoutSurname);
                        if (matcherName.find()) {
                            String name = matcherName.group(3);
                            String toWrite = tagWithoutSurname
                                    .replaceAll(PATTERN_NAME.pattern(), String.format(NAME_SWAP, name, surname))
                                    .replaceAll(" +\n", "");
                            writer.append(toWrite).append("\n");
                        }
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) throw new IllegalArgumentException("haven't received any args, expected file name");
        String path = ROOT_PATH + args[0] + (args[0].contains(".xml") ? "" : FORMAT);
        File file = new File(Path.of(path).toUri());
        if (!file.exists()) throw new FileNotFoundException("file was not found : " + args[0]);
        rewriteXML(file);
    }
}
