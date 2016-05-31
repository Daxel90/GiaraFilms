package it.giara.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompression
{
	
	public static byte[] compress(final String str)
	{
		if ((str == null) || (str.length() == 0))
		{
			return null;
		}
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try
		{
			gzip = new GZIPOutputStream(obj);
			gzip.write(str.getBytes("UTF-8"));
			gzip.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return obj.toByteArray();
	}
	
	public static String decompress(final byte[] compressed)
	{
		String outStr = "";
		if ((compressed == null) || (compressed.length == 0))
		{
			return "";
		}
		if (isCompressed(compressed))
		{
			GZIPInputStream gis;
			try
			{
				gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
				
				String line;
				while ((line = bufferedReader.readLine()) != null)
				{
					outStr += line;
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			outStr = new String(compressed);
		}
		return outStr;
	}
	
	public static boolean isCompressed(final byte[] compressed)
	{
		return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
				&& (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
	}
}