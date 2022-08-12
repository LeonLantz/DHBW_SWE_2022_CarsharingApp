package util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import de.dhbwka.swe.utils.util.PropertyManager;

public class PManager {

  private final PropertyManager propertyManager;
  private final String relativePropFilePath;
  private String absoluteFilePath;

  public PManager(String relativePropFilePath) throws Exception {
    this.relativePropFilePath = relativePropFilePath;
    this.handleRelativePropFilePath();
    this.propertyManager = new PropertyManager(this.absoluteFilePath, null, null);
  }

  public PropertyManager getPropertyManager() {
    return propertyManager;
  }

  private static final String sp = File.separator;

  /**
   * Used for figuring out the current working directory in order to use PropertyManager
   */
  private void handleRelativePropFilePath() {
    String jarPath = "";
    System.out.println("Reading Propertyfile...");
    try {
      jarPath = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
      System.out.println(jarPath);
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }

    String absoluteWorkingDirectoryPath = jarPath.substring(0, jarPath.lastIndexOf(sp));

    String relativeDirectoryPath = this.relativePropFilePath.substring(0, this.relativePropFilePath.lastIndexOf(sp));
    File absoluteDirectories = new File(absoluteWorkingDirectoryPath + relativeDirectoryPath);

    this.absoluteFilePath = absoluteWorkingDirectoryPath + this.relativePropFilePath;
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
}
