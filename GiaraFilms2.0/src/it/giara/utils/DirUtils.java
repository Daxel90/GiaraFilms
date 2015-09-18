package it.giara.utils;

import java.io.File;

public class DirUtils
{
  public static File workDir = null;

  public static File getWorkingDirectory()
  {
    if (workDir == null)
    {
      workDir = getWorkingDirectory("giarafilms");
    }

    return workDir;
  }

  public static File getDownloadDir()
  {
    return new File(getWorkingDirectory(), "download");
  }

  public static File getCacheDir()
  {
    return new File(getWorkingDirectory(), "cache");
  }

  public static File getWorkingDirectory(String applicationName)
  {
    String userHome = System.getProperty("user.home", "");
    File workingDirectory;
    File workingDirectory;
    File workingDirectory;
    File workingDirectory;
    switch ($SWITCH_TABLE$it$giara$utils$DirUtils$OS()[getPlatform().ordinal()])
    {
    case 1:
    case 2:
      workingDirectory = new File(userHome, applicationName + '/');
      break;
    case 3:
      String applicationData = System.getenv("APPDATA");
      File workingDirectory;
      if (applicationData != null)
      {
        workingDirectory = new File(applicationData, applicationName + '/');
      }
      else
      {
        workingDirectory = new File(userHome, applicationName + '/');
      }
      break;
    case 4:
      workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
      break;
    default:
      workingDirectory = new File(userHome, applicationName + '/');
    }

    if (!workingDirectory.exists());
    workingDirectory.mkdirs();

    return workingDirectory;
  }

  public static OS getPlatform()
  {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win"))
    {
      return OS.windows;
    }
    if (osName.contains("mac"))
    {
      return OS.macos;
    }
    if (osName.contains("solaris"))
    {
      return OS.solaris;
    }
    if (osName.contains("sunos"))
    {
      return OS.solaris;
    }
    if (osName.contains("linux"))
    {
      return OS.linux;
    }
    if (osName.contains("unix"))
    {
      return OS.linux;
    }
    return OS.unknown;
  }

  public static double getJavaVersion()
  {
    String version = System.getProperty("java.version");
    Log.log(Log.INFO, "version:" + version);
    int pos = 0; int count = 0;
    for (; (pos < version.length()) && (count < 2); pos++) {
      if (version.charAt(pos) == '.') {
        count++;
      }
    }
    pos--;
    double dversion = Double.parseDouble(version.substring(0, pos));
    Log.log(Log.INFO, "Java Version:" + dversion);
    return dversion;
  }

  public static enum OS
  {
    linux, solaris, windows, macos, unknown;
  }
}