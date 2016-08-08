package it.giara.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import it.giara.utils.Log;

public class HTTPList
{
	
	static
	{
		System.setProperty("http.agent", "PoWeR-Script");
	}
	
	public ArrayList<String> file = new ArrayList<String>();
	public ArrayList<String> sizeList = new ArrayList<String>();
	
	public HTTPList(String site, String search)
	{
		getFileList(site + "?m=1&q=(search)", search.replace(" ", "+"));
		
	}
	
	private ArrayList<String> getFileList(String site, String search)
	{
		Log.log(Log.NET, site.replace("(search)", search));
		try
		{
			URL url = new URL(site.replace("(search)", search));
			final URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "PoWeR-Script");
			conn.setRequestProperty("accept-encoding", "gzip, deflate");
			conn.connect();
			
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
				Log.log(Log.DEBUG, "Zipped");
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[16384];
				while ((nRead = is.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();

				byte[] compressedData = buffer.toByteArray();
				Log.log(Log.DEBUG,"DataSize: "+compressedData.length);
				GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData));
				rd = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
			}
			else
			{
				Log.log(Log.DEBUG, "Not Zipped");
				rd = new BufferedReader(new InputStreamReader(is));
			}
			
			String line;
			while ((line = rd.readLine()) != null)
			{
				if (!line.startsWith("#"))
					continue;
					
				line = line.trim();
				while (line.contains("  "))
					line = line.replace("  ", " ");
					
				String[] info = line.split(" ");
				if (info.length < 4)
					continue;
				String name = info[3];
				String size = info[2].trim();
				
				if (info.length > 4) // correction for file name containing space
					for (int j = 4; j < info.length; j++)
					{
						if (info[j].contains("/") || info[j].contains("<"))
							break;
							
						name += " " + info[j];
					}
				
				//remove html body end if present
				name = name.replace("</body>", "").replace("<br/>", "");
				name = name.trim();
				
				if(name.contains("<") && name.charAt(name.length()-4) != '.')
				{
					name = name.split("<")[0];
				}
				
				
				if (!file.contains(name))
				{
					file.add(name);
					sizeList.add(size);
					Log.log(Log.NET, name);
					Log.log(Log.NET, file.size());
				}
				
			}
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		
		return file;
		
	}
	
}
