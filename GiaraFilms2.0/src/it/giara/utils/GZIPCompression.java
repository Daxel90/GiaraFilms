package it.giara.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
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
	
	public static BufferedReader decompressConnection(URLConnection conn) throws IOException
	{
		boolean gzipped = false;
		
		if(conn.getHeaderFields().containsKey("Content-Encoding"))
		{
			if(conn.getHeaderFields().get("Content-Encoding").contains("gzip"))
			{
				gzipped = true;
			}
		}
		InputStream is = conn.getInputStream();
		BufferedReader rd = null;
		
		if(gzipped)
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();

			byte[] compressedData = buffer.toByteArray();
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData));
			rd = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		}
		else
		{
			rd = new BufferedReader(new InputStreamReader(is));
		}
		
		return rd;
	}
}