package MyPackage;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Assignment {

    public static void main(String args[]) throws IOException, XMLStreamException {
        Pars pars = new Pars();
        String[] arg = pars.parseArgs(args);
        String fileName = arg[0];
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(fileName));
        Predicate<String> predicate = Objects::isNull;
        if (arg[2].equals("-S")) {
             predicate = x -> Pattern.compile(arg[1]).matcher(x).matches();
        }
        if (arg[2].equals("-s")) {
            predicate = x -> x.contains(arg[1]);
        }
        List<File> fileList = pars.parseXMLFile(parser, predicate);
        for (File file : fileList) {
            for (String dir : file.getDirectory()) {
                if (dir.equals("/")) {
                    System.out.print(dir);
                } else {
                    System.out.print(dir + "/");
                }
            }
            System.out.println(file.getFile());
        }
    }
}
