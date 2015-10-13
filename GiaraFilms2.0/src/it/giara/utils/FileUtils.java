package it.giara.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.giara.gui.section.LoadScreen;

public class FileUtils
{
	public static boolean download(File fileTarget, String link, LoadScreen screen)
	{
		try
		{
			fileTarget.getParentFile().mkdirs();
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			screen.bar.setMaximum(conn.getContentLength());
			BufferedInputStream dis = new BufferedInputStream(conn.getInputStream());
			OutputStream fos = new BufferedOutputStream(new FileOutputStream(fileTarget));
			int l = 0;
			int c;
			while ((c = dis.read()) != -1)
			{
				fos.write(c);
				l++;
				if (l % 1024 == 0)
				{
					screen.bar.setValue(l);
				}
			}
			
			conn.disconnect();
			dis.close();
			fos.close();
			
			return true;
		} catch (final Exception e)
		{
			Log.stack(Log.NET, e);
			Log.log(Log.NET, "errore url download aggiornamento");
		}
		return false;
	}
	
	public static void copyFile(File file1, File file2)
	{
		try
		{
			if (!file2.getParentFile().exists())
				file2.getParentFile().mkdirs();
			file2.createNewFile();
			InputStream inStream = null;
			OutputStream outStream = null;
			inStream = new FileInputStream(file1);
			outStream = new FileOutputStream(file2);
			final byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0)
			{
				outStream.write(buffer, 0, length);
			}
			if (inStream != null)
			{
				inStream.close();
			}
			if (outStream != null)
			{
				outStream.close();
			}
		} catch (final IOException e)
		{
			Log.stack(Log.NET, e);
		}
	}
	
}
