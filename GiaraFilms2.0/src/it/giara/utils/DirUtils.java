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
		final String userHome = System.getProperty("user.home", "");
		File workingDirectory;
		switch (getPlatform())
		{
			case solaris:
			case linux:
				workingDirectory = new File(userHome, applicationName + '/');
				break;
			case windows:
				final String applicationData = System.getenv("APPDATA");
				if (applicationData != null)
				{
					workingDirectory = new File(applicationData, applicationName + '/');
				}
				else
				{
					workingDirectory = new File(userHome, applicationName + '/');
				}
				break;
			case macos:
				workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
				break;
			default:
				workingDirectory = new File(userHome, applicationName + '/');
		}
		
		if (!workingDirectory.exists())
		{
			;
		}
		workingDirectory.mkdirs();
		
		return workingDirectory;
	}
	
	public static OS getPlatform()
	{
		final String osName = System.getProperty("os.name").toLowerCase();
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
		int pos = 0, count = 0;
		for (; pos < version.length() && count < 2; pos++)
		{
			if (version.charAt(pos) == '.')
			{
				count++;
			}
		}
		--pos;
		double dversion = Double.parseDouble(version.substring(0, pos));
		Log.log(Log.INFO, "Java Version:" + dversion);
		return dversion;
	}
	
	public static enum OS
	{
		linux, solaris, windows, macos, unknown;
	}
}
