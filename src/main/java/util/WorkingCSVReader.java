package util;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WorkingCSVReader {
    private final String separator;
    private final String csvFilename;
    private final boolean ignoreHeaderLine;

    public WorkingCSVReader(String csvFilename, String separator, boolean ignoreHeaderLine) {
        this.csvFilename = csvFilename;
        this.separator = separator;
        this.ignoreHeaderLine = ignoreHeaderLine;
        this.handleFile();
    }
    private static final String sp = System.getProperty("file.separator");
    private String absoluteFilePath = "";


    /** Check if directory and file exist.
     * Create new directories and file if needed.
     */
    private void handleFile() {
        String jarPath = "";
        System.out.println("Reading data...");
        try {
            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            System.out.println(jarPath);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String absoluteWorkingDirectoryPath = jarPath.substring(0, jarPath.lastIndexOf("/"));

        String relativeDirectoryPath = this.csvFilename.substring(0, this.csvFilename.lastIndexOf("/"));
        File absoluteDirectories = new File(absoluteWorkingDirectoryPath + relativeDirectoryPath);

        this.absoluteFilePath = absoluteWorkingDirectoryPath + this.csvFilename;
        File absoluteFile = new File(this.absoluteFilePath);
        try {
            if (!absoluteDirectories.exists() && !absoluteDirectories.mkdirs()) {
                System.out.println("Directory doesn't exist, and creating directory: " + relativeDirectoryPath + " failed.");
            }
            if (!absoluteFile.exists() && !absoluteFile.createNewFile()) {
                System.out.println("File doesn't exist, and creating file with path: " + this.absoluteFilePath + " failed.");

            } else {
                System.out.println("Input data exists, or file with path " + this.absoluteFilePath + " created successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readData() throws IllegalArgumentException {
        try {
            List<String[]> csvData = new ArrayList<>();
            List<String> allLines = Files.readAllLines(Paths.get(absoluteFilePath), StandardCharsets.UTF_8);
            if (this.ignoreHeaderLine) allLines.remove(0);
            allLines.forEach(line -> {
                if (!line.equals("") && line.contains(this.separator)) csvData.add(line.split(this.separator));
            });
            return csvData;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error in reading resource: "+this.absoluteFilePath);
        }
    }
}