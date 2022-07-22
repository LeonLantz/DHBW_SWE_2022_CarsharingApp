package util;

import jdk.internal.loader.Resource;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

public class WorkingCSVWriter {

    private final String csvFilename;
    private final String separator;
    private final String headerLine;

    public WorkingCSVWriter(String csvFilename, String separator, String headerLine) {
        this.csvFilename = csvFilename;
        this.separator = separator;
        this.headerLine = headerLine;
    }

    public void writeData(List<String[]> data) {
        try {
            URL fullpath = this.getClass().getResource(this.csvFilename);
            if (fullpath == null) {

            }
            PrintWriter writer = new PrintWriter(new File(fullpath.getPath()));
            if (this.headerLine.length() > 0) {
                writer.write(this.headerLine);
                writer.println();
            }
            data.forEach(line_items -> {
                String result_line = "";
                for (int i = 0; i < line_items.length-1; i++) {
                    result_line += line_items[i] + this.separator;
                }
                result_line += line_items[line_items.length-1];
                writer.write(result_line);
                writer.println();
            });
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in writing to resource: "+this.csvFilename);
        }
    }
}
