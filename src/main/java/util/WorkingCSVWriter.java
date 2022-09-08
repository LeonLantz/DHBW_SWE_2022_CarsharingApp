package util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import de.dhbwka.swe.utils.util.FileEncoding;

public class WorkingCSVWriter {

    private final String csvFilename;
    private final String separator;
    private final String headerLine;

    public WorkingCSVWriter(String csvFilename, String separator, String headerLine) {
        this.csvFilename = csvFilename;
        this.separator = separator;
        this.headerLine = headerLine;
        this.handleFile();
    }

    private static final String sp = System.getProperty("file.separator");
    private String absoluteFilePath = "";

    /** Check if directory and file exist.
     * Create new directories and file if needed.
     */
    private void handleFile() {
//        String jarPath = "";
//        System.out.println("Reading data...");
//        try {
//            jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
//            System.out.println(jarPath);
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }

        String absoluteWorkingDirectoryPath = CSHelp.getWorkingDirectory();

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

    public void writeData(List<String[]> data) throws IOException {
//        try {
//
//            // TODO: BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.absoluteFilePath), FileEncoding.UTF_8.getName()));
//            PrintWriter writer = new PrintWriter(this.absoluteFilePath);
//            if (this.headerLine.length() > 0) {
//                writer.write(this.headerLine);
//                writer.println();
//            }
//            data.forEach(line_items -> {
//                String result_line = "";
//                for (int i = 0; i < line_items.length-1; i++) {
//                    result_line += line_items[i] + this.separator;
//                }
//                result_line += line_items[line_items.length-1];
//                writer.write(result_line);
//                writer.println();
//            });
//            writer.flush();
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.absoluteFilePath), FileEncoding.UTF_8.getName()));
        try {
            if (this.headerLine.length() > 0) {
                try {
                    writer.write(this.headerLine);
                    writer.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            data.forEach(line_items -> {
                String result_line = "";
                for (int i = 0; i < line_items.length-1; i++) {
                    result_line += line_items[i] + this.separator;
                }
                result_line += line_items[line_items.length-1];
                try {
                    writer.write(result_line);
                    writer.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
