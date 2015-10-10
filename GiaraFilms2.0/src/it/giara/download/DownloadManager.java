package it.giara.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import it.giara.http.HTTPFileSources;
import it.giara.irc.IrcConnection;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.utils.ErrorHandler;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class DownloadManager
{
	public static HashMap<String, IrcConnection> servers = new HashMap<String, IrcConnection>();
	public static Random r = new Random();
	
	public static void Download(String filename)
	{
		
		FileSources sources = getFileSources(filename);
		
		if (sources.totalBot <= 0)
		{
			ErrorHandler.fileNotAvailable(filename);
			return;
		}
		
		Entry<SourceChan, ArrayList<BotPackage>> data = sources.sourcesBot.entrySet().iterator().next();
		
		SourceChan chan = data.getKey();
		ArrayList<BotPackage> bot = data.getValue();
		
		if (servers.containsKey(chan.server) && servers.get(chan.server).isConnected())
		{
			servers.get(chan.server).joinChannel(chan.server);
		}
		else if (!servers.containsKey(chan.server))
		{
			IrcConnection conn = new IrcConnection(chan.server);
			conn.joinChannel(chan.chan);
			servers.put(chan.server, conn);
		}
		else if (servers.containsKey(chan.server) && !servers.get(chan.server).isConnected())
		{
			try
			{
				servers.get(chan.server).reconnect();
				servers.get(chan.server).joinChannel(chan.chan);
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		FunctionsUtils.sleep(2000);
		
		int n = r.nextInt(bot.size());
		servers.get(chan.server).sendMessage(bot.get(n).bot, "xdcc send #" + bot.get(n).packetID);
		
	}
	
	public static FileSources getFileSources(String filename)
	{
		FileSources result = new FileSources(filename);
		for (int x = 0; x < ListLoader.sources.size(); x++)
		{
			SourceChan chan = ListLoader.sources.get(x);
			new HTTPFileSources(chan, result);
		}
		
		Log.log(Log.DEBUG, result.totalBot);
		
		return result;
	}
	
}
