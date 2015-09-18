package it.giara.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HttpPost
{
	
	public static String post(String pagina, String variabile, String valore)
	{
		String risultato = "error";
		try
		{
			String data = URLEncoder.encode(variabile, "UTF-8") + "=" + URLEncoder.encode(valore, "UTF-8");
			URL url = new URL(pagina);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				risultato = line;
			}
			risultato = removeUTF8BOM(risultato);
			wr.close();
			rd.close();
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		return risultato;
	}
	
	public static String removeUTF8BOM(String s)
	{
		if (s.startsWith("\uFEFF"))
		{
			s = s.substring(1);
		}
		return s;
	}
	
}
