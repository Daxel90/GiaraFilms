package it.giara.http;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;

import it.giara.download.BotPackage;
import it.giara.download.FileSources;
import it.giara.source.SourceChan;
import it.giara.utils.GZIPCompression;
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
			
			BufferedReader rd = GZIPCompression.decompressConnection(conn);
			
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
