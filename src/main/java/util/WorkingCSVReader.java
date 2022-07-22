package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkingCSVReader {
    private final String separator;
    private final String csvFilename;
    private final boolean ignoreHeaderLine;

    public WorkingCSVReader(String csvFilename, String separator, boolean ignoreHeaderLine) {
        this.csvFilename = csvFilename;
        this.separator = separator;
        this.ignoreHeaderLine = ignoreHeaderLine;
//        this.fileExists(this.csvFilename);
    }

//    private boolean fileExists(String filePath) {
//        File f = new File(filePath);
//        if (f.exists() && !f.isDirectory()) {
//            return true;
//        }
//        throw new IllegalArgumentException(filePath + " does not exist");
//    }

    public List<String[]> readData() throws IllegalArgumentException {
        try {
            InputStream is = this.getClass().getResourceAsStream(this.csvFilename);
            List<String> allLines = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            if (this.ignoreHeaderLine) allLines.remove(0);
            List<String[]> csvData = new ArrayList<>();
            allLines.forEach(line -> csvData.add(line.split(this.separator)));
            return csvData;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error in reading resource: "+this.csvFilename);
        }
    }
}