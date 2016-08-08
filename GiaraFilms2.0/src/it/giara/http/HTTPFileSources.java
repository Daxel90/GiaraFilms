package it.giara.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import it.giara.download.BotPackage;
import it.giara.download.FileSources;
import it.giara.source.SourceChan;
import it.giara.utils.Log;

public class HTTPFileSources
{
	
	static
	{
		System.setProperty("http.agent", "PoWeR-Script");
	}
	
	FileSources fileSources;
	SourceChan currentChan;
	
	public HTTPFileSources(SourceChan chan, FileSources file)
	{
		currentChan = chan;
		fileSources = file;
		getFileList(chan.link + "?m=1&q=(search)", fileSources.filename.replace(" ", "+"));
		
	}
	
	private void getFileList(String site, String search)
	{
		// Log.log(Log.DOWNLOAD, site.replace("(search)", search));
		try
		{
			URL url = new URL(site.replace("(search)", search));
			final URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "PoWeR-Script");
			conn.setRequestProperty("accept-encoding", "gzip, deflate");
			conn.setConnectTimeout(30000);
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
				
				if (info.length > 4) // correction for file name containing
										// space
					for (int j = 4; j < info.length; j++)
					{
						if (info[j].contains("/"))
							break;
							
						name += " " + info[j];
					}
					
				// remove html body end if present
				name = name.replace("</body>", "").replace("<br/>", "");
				name = name.trim();
				
				int pkid = Integer.parseInt(info[0].replace("#", ""));
				String bot = info[1];
				
				if (fileSources.filename.equals(name))
				{
					fileSources.addBot(currentChan, new BotPackage(bot, pkid));
					
					Log.log(Log.DOWNLOAD, pkid);
					Log.log(Log.DOWNLOAD, bot);
				}
				else
				{
					Log.log(Log.DOWNLOAD, "-" + name + "- != +" + fileSources.filename + "-");
				}
				
			}
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.DOWNLOAD, e);
		}
		
	}
	
}
