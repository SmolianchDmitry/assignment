package myPackage;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Pars {

    public String[] parseArgs(String[] args) throws IOException {
        String[] str = new String[3];
        if (!args[0].equals("-f")) {
            throw  new IOException("Нет обязательного аргумента - ИМЯ ФАЙЛА");
        }
        str[0] = args[1];
        if (args.length > 3) {
            if (args[2].equals("-s")) {
                str[1] = parseString(args[3]);
                str[2] = args[2];
            } else if (args[2].equals("-S")) {
                str[1] = args[3];
                str[2] = args[2];
            }else {
                throw new IOException("Не верный аргумент выбора");
            }

        } else {
            str[1] = "\\S+";
            str[2] = "-S";
        }
        return str;
    }

    private String parseString(String string) {
        if (string.contains("*")) {
            return string.substring(2);
        }
        return string;
    }

    public List<File> parseXMLFile(XMLStreamReader parser, Predicate<String> predicate) throws XMLStreamException {
        boolean flagFile = false;
        boolean flagName = false;
        List<File> fileList = new ArrayList<>();
        List<String> dirList = new ArrayList<>();
        File file = new File();
        int event = parser.next();
        while (true) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if ((parser.getLocalName().equals("child"))) {
                        file = new File();
                        if (parser.getAttributeValue(null, "is-file").equals("true")) {
                            flagFile = true;
                        }
                    } else if (parser.getLocalName().equals("name")) {
                        flagName = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (flagFile) {
                        file.setFile(true);
                        flagFile = false;
                    } else if (flagName) {
                        file.setFile(parser.getText());
                        flagName = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (parser.getLocalName().equals("name") && file.isFile()
                            && predicate.test(file.getFile())) {
                        file.setDirectory(new ArrayList<>(dirList));
                        fileList.add(file);
                    } else if (parser.getLocalName().equals("name") && !file.isFile()) {
                        dirList.add(file.getFile());
                    } else if (parser.getLocalName().equals("children")) {
                        dirList.remove(dirList.size() - 1);
                    }
                    break;
            }
            if (!parser.hasNext())
                break;
            event = parser.next();
        }
        return fileList;
    }
}
