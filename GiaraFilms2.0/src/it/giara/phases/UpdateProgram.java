package it.giara.phases;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

import it.giara.MainClass;
import it.giara.gui.section.LoadScreen;
import it.giara.utils.DirUtils;
import it.giara.utils.FileUtils;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.HttpPost;
import it.giara.utils.Log;
import it.giara.utils.MD5;

public class UpdateProgram
{
	
	public static void checkUpdate(LoadScreen screen)
	{
		try
		{
			CodeSource codeSource = MainClass.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			if (jarFile.getAbsolutePath().endsWith(".jar"))
			{
				String md5 = MD5.getMD5Checksum(jarFile);
				
				if (HttpPost.post("http://giaratest.altervista.org/giarafilms/MD5Update.php", "md5", md5)
						.contains("update"))
				{
					startUpdate(screen, jarFile);
				}
			}
		} catch (URISyntaxException e)
		{}
		
	}
	
	public static void startUpdate(LoadScreen screen, File jarFile)
	{
		File newFile = new File(jarFile.getAbsolutePath() + ".update");
		if (newFile.exists())
			newFile.delete();
		screen.textProgress.setText("Download Aggiornamento in corso...");
		
		boolean succ = FileUtils.download(newFile, "http://giaratest.altervista.org/giarafilms/GiaraFilms2.0.jar",
				screen);
		if (succ)
		{
			screen.textProgress.setText("Riavvio applicazione in corso...");
			FunctionsUtils.sleep(1500);
			Settings.prop.putBoolean("delUpdate", false);
			try
			{
				Settings.prop.flush();
			} catch (BackingStoreException e)
			{
				e.printStackTrace();
			}
			rebootAndDelete(newFile, jarFile);
		}
	}
	
	public static void rebootAndDelete(File newF, File oldF)
	{
		Log.log(Log.INFO, "Riavvio New:" + newF.getName() + " Old:" + oldF.getName());
		try
		{
			final String javaBin = DirUtils.getJavaDir();
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(newF.getPath());
			
			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch (Exception e)
		{
			Log.stack(Log.ERROR, e);
		}
		
		oldF.deleteOnExit();
		System.exit(0);
	}
	
	public static void checkUpdateSuccess()
	{
		
		try
		{
			CodeSource codeSource = MainClass.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			File oldUpdate = new File(jarFile.getAbsolutePath() + ".update");
			Log.log(Log.INFO, "Avviato con file: " + jarFile.getName());
			Log.log(Log.INFO, "Il file Update Dovrebbe essere: " + oldUpdate.getName());
			
			if (oldUpdate.exists() && Settings.prop.getBoolean("delUpdate", false))
			{
				FunctionsUtils.sleep(1000);
				oldUpdate.delete();
				Log.log(Log.INFO, "Cancellato:" + oldUpdate.getName());
				Settings.prop.putBoolean("delUpdate", false);
				Settings.prop.flush();
			}
			else if (jarFile.getName().endsWith(".update"))
			{
				FunctionsUtils.sleep(1000);
				File newF = new File(jarFile.getAbsolutePath().substring(0, jarFile.getAbsolutePath().length()-7));
				Log.log(Log.INFO, "Rinomino l'update in:" + newF.getName());
				if (newF.exists())
				{
					newF.delete();
					Log.log(Log.INFO, "Cancellato:" + newF.getName());
				}
				FileUtils.copyFile(jarFile, newF);
				Log.log(Log.INFO, "Copiato:" + jarFile.getName() + " in " + newF.getName());
				Settings.prop.putBoolean("delUpdate", true);
				Settings.prop.flush();
				rebootAndDelete(newF, jarFile);
			}
		} catch (Exception e)
		{
			
			Log.stack(Log.ERROR, e);
		}
	}
	
}
