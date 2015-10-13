package it.giara.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jibble.pircbot.DccFileTransfer;

import it.giara.irc.IrcConnection;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;
import it.giara.utils.ErrorHandler;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class FileSources
{
	public String filename;
	public int totalBot = 0;
	public int loadingBotList = 1;
	
	public short botResponse = -1; // -1 wait 0 fail 1 connected 2 file transfer
	public boolean downloading = false;
	public DccFileTransfer xdcc;
	File saveFile = null;
	public boolean fileEnd = false;
	
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
	
	public void requestDownload(File file)
	{
		saveFile = file;
		requestDownload();
	}
	
	public void requestDownload()
	{
		SQLQuerySettings.addDownload(filename, saveFile.getAbsolutePath());
		while (loadingBotList>0)
		{
			Log.log(Log.DEBUG, totalBot);
			FunctionsUtils.sleep(1000);
		}
		
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
			
			if (DownloadManager.servers.containsKey(chan.server)
					&& DownloadManager.servers.get(chan.server).isConnected())
			{
				DownloadManager.servers.get(chan.server).joinChannelAndSayHello(chan.server);
			}
			else if (!DownloadManager.servers.containsKey(chan.server))
			{
				IrcConnection conn = new IrcConnection(chan.server);
				conn.joinChannelAndSayHello(chan.chan);
				DownloadManager.servers.put(chan.server, conn);
			}
			else if (DownloadManager.servers.containsKey(chan.server)
					&& !DownloadManager.servers.get(chan.server).isConnected())
			{
				try
				{
					DownloadManager.servers.get(chan.server).reconnect();
					DownloadManager.servers.get(chan.server).joinChannelAndSayHello(chan.chan);
					
				} catch (Exception e)
				{
					Log.stack(Log.IRC, e);
				}
			}
			
			FunctionsUtils.sleep(1000);
			
			for (BotPackage bot : bots)
			{
				botResponse = -1;
				DownloadManager.BotRequest.put(bot.bot, this);
				DownloadManager.servers.get(chan.server).sendMessage(bot.bot, "xdcc send #" + bot.packetID);
				
				while (botResponse == -1)
				{
					FunctionsUtils.sleep(2000);
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
	
	public void startDownloadXDCC()
	{
		// for reducing Heap Size
		sourcesBot.clear();
		
		if (saveFile == null)
			saveFile = new File(DirUtils.getDownloadDirectory(), xdcc.getFile().getName());
		
		if (!saveFile.getParentFile().exists())
			saveFile.getParentFile().mkdir();
			
		Log.log(Log.IRC, "INCOMING:\t" + xdcc.getFile().toString() + " " + xdcc.getSize() + " bytes");
		
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
			return;
		}
		
		fileEnd = true;
		
		SQLQuerySettings.removeDownload(filename);
		
		Log.log(Log.IRC, "Trasferimento Completato: " + transfer.getFile().getName());
	}
	
}
