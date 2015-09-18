package it.giara.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5
{
	
	public static String generatemd5(String message)
	{
		String digest = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for (byte b : hash)
			{
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return digest;
	}
	
	public static String getMD5Checksum(File filename)
	{
		byte[] b = null;
		try
		{
			b = createChecksum(filename);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		String result = "";
		for (int i = 0; i < b.length; i++)
		{
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
		
	}
	
	public static byte[] createChecksum(File filename) throws Exception
	{
		final InputStream fis = new FileInputStream(filename);
		final byte[] buffer = new byte[1024];
		final MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do
		{
			numRead = fis.read(buffer);
			if (numRead > 0)
			{
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
}
