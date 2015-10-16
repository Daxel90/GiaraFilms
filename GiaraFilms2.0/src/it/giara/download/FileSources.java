package it.giara.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jibble.pircbot.DccFileTransfer;

import it.giara.http.HTTPFileSources;
import it.giara.irc.IrcConnection;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;
import it.giara.utils.ErrorHandler;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class FileSources
{
	public String filename;
	public int totalBot = 0;
	public int loadingBotList = 0;
	
	public short botResponse = -1; // -1 wait 0 fail 1 connected 2 file transfer
	public boolean downloading = false;
	public DccFileTransfer xdcc;
	File saveFile = null;
	public boolean fileEnd = false;
	
	public boolean paused = false;
	
	HashMap<SourceChan, ArrayList<BotPackage>> sourcesBot = new HashMap<SourceChan, ArrayList<BotPackage>>();
	
	public FileSources(String name)
	{
		filename = name;
		DownloadManager.AllFile.put(filename, this);
	}
	
	public void addBot(SourceChan chan, BotPackage bot)
	{
		totalBot++;
		if (!sourcesBot.containsKey(chan))
		{
			sourcesBot.put(chan, new ArrayList<BotPackage>());
		}
		
		sourcesBot.get(chan).add(bot);
	}
	
	public void loadList()
	{
		loadingBotList = ListLoader.sources.size();
		for (int x = 0; x < ListLoader.sources.size(); x++)
		{
			final int K = x;
			Runnable run = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						SourceChan chan = ListLoader.sources.get(K);
						new HTTPFileSources(chan, getInstance());
					} finally
					{
						loadingBotList--;
					}
				}
			};
			
			ThreadManager.submitCacheTask(run);
		}
	}
	
	public void waitUntilAllBotListAreLoaded()
	{
		while (loadingBotList > 0)
		{
			Log.log(Log.DEBUG, totalBot);
			FunctionsUtils.sleep(1000);
		}
	}
	
	public void requestDownload(File file)
	{
		saveFile = file;
		requestDownload();
	}
	
	public void requestDownload()
	{
		if (paused)
			return;
			
		if (saveFile == null)
			saveFile = new File(DirUtils.getDownloadDirectory(), filename);
			
		SQLQuerySettings.addDownload(filename, saveFile.getAbsolutePath());
		
		waitUntilAllBotListAreLoaded();
		
		if (totalBot <= 0)
		{
			ErrorHandler.fileNotAvailable(filename);
			return;
		}
		Iterator<Entry<SourceChan, ArrayList<BotPackage>>> it = sourcesBot.entrySet().iterator();
		
		while (it.hasNext())
		{
			botResponse = -1;
			Entry<SourceChan, ArrayList<BotPackage>> data = it.next();
			
			SourceChan chan = data.getKey();
			ArrayList<BotPackage> bots = data.getValue();
			
			if (!DownloadManager.servers.containsKey(chan.server))
			{
				IrcConnection conn = new IrcConnection(chan.server);
				DownloadManager.servers.put(chan.server, conn);
			}
			else if (DownloadManager.servers.containsKey(chan.server)
					&& !DownloadManager.servers.get(chan.server).isConnected())
			{
				try
				{
					DownloadManager.servers.get(chan.server).reconnect();
				} catch (Exception e)
				{
					Log.stack(Log.IRC, e);
				}
			}
			
			DownloadManager.servers.get(chan.server).joinChannelAndSayHello(chan.chan);
			
			FunctionsUtils.sleep(1000);
			
			for (BotPackage bot : bots)
			{
				botResponse = -1;
				DownloadManager.BotRequest.put(bot.bot, this);
				DownloadManager.servers.get(chan.server).sendMessage(bot.bot, "xdcc send #" + bot.packetID);
				int retry = 0;
				while (botResponse == -1)
				{
					FunctionsUtils.sleep(2000);
					retry++;
					if (retry > 4)
						break;
				}
				
				if (botResponse == 1)
				{
					FunctionsUtils.sleep(10000);
				}
				
				if (botResponse == 2)
					return;
				else
					DownloadManager.BotRequest.remove(bot.bot);
			}
		}
		
	}
	
	public void startDownloadXDCC(DccFileTransfer transfer)
	{
		// for reducing Heap Size
		sourcesBot.clear();
		
		if (saveFile == null)
			saveFile = new File(DirUtils.getDownloadDirectory(), transfer.getFile().getName());
			
		if (!saveFile.getParentFile().exists())
			saveFile.getParentFile().mkdir();
			
		Log.log(Log.IRC, "INCOMING:\t" + transfer.getFile().toString() + " " + transfer.getSize() + " bytes");
		
		if (xdcc != null)
		{
			transfer.close();
			return;
		}
		xdcc = transfer;
		
		if (saveFile.exists() && (xdcc.getSize() == saveFile.length()))
		{
			Log.log(Log.IRC, "EXISTS:\t try to close connection");
			xdcc.close();
			
		}
		else
		{
			Log.log(Log.IRC, "SAVING TO:\t" + saveFile.toString());
			xdcc.receive(saveFile, true);
		}
	}
	
	public void endXDCCTransfer(DccFileTransfer transfer, Exception ex)
	{
		if (ex != null)
		{
			requestDownload();
			xdcc = null;
			return;
		}
		
		fileEnd = true;
		
		SQLQuerySettings.removeDownload(filename);
		
		Log.log(Log.IRC, "Trasferimento Completato: " + transfer.getFile().getName());
	}
	
	public void stop()
	{
		paused = true;
		if (xdcc != null)
		{
			xdcc.close();
			xdcc = null;
		}
		
		totalBot = 0;
		loadingBotList = 0;
		botResponse = -1;
		downloading = false;
		sourcesBot.clear();
		SQLQuerySettings.setStatus(filename, 1);
	}
	
	public void restart()
	{
		paused = false;
		loadList();
		Runnable run = new Runnable()
		{
			
			@Override
			public void run()
			{
				requestDownload();
			}
		};
		ThreadManager.submitCacheTask(run);
		SQLQuerySettings.setStatus(filename, 0);
	}
	
	public void delete()
	{
		stop();
		DownloadManager.AllFile.remove(filename);
		SQLQuerySettings.removeDownload(filename);
	}
	
	public FileSources getInstance()
	{
		return this;
	}
}
