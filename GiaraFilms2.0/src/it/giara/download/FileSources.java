package it.giara.download;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.jibble.pircbot.DccFileTransfer;

import it.giara.gui.MainFrame;
import it.giara.gui.section.Download;
import it.giara.http.HTTPFileSources;
import it.giara.irc.IrcConnection;
import it.giara.phases.Settings;
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
	
	public boolean onWaitingList = false;
	public boolean endAskFile = false;
	
	public boolean AtLeastOneBotOccupated = false;
	
	public ArrayList<SourceChan> alreadyAskChannel = new ArrayList<SourceChan>();
	
	ConcurrentHashMap<SourceChan, ArrayList<BotPackage>> sourcesBot = new ConcurrentHashMap<SourceChan, ArrayList<BotPackage>>();
	
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
		
		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				for (int x = 0; x < ListLoader.sources.size(); x++)
				{
					try
					{
						SourceChan chan = ListLoader.sources.get(x);
						new HTTPFileSources(chan, getInstance());
					} finally
					{
						loadingBotList--;
					}
				}
			}
		};
		
		ThreadManager.submitCacheTask(run);
	}
	
	public void waitUntilAllBotListAreLoaded()
	{
		while (loadingBotList > 0)
		{
			Log.log(Log.DEBUG, "Bot: " + totalBot + "  " + filename);
			FunctionsUtils.sleep(1000);
		}
	}
	
	public void waitUntilthereAreAtLeastOneBot()
	{
		while (totalBot <= 0 && loadingBotList > 0)
		{
//			Log.log(Log.DEBUG, "Bot: " + totalBot + "  " + filename);
			FunctionsUtils.sleep(1000);
			if (paused)
				return;
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
			saveFile = new File(DirUtils.getDownloadDirectory(),
					filename.replace(":", " ").replace("*", "").replace("?", "").replace("<", "").replace(">", ""));
					
		SQLQuerySettings.addDownload(filename, saveFile.getAbsolutePath());
		
		waitUntilthereAreAtLeastOneBot();
		
		if (paused)
			return;
			
		if (totalBot <= 0)
		{
			ErrorHandler.fileNotAvailable(filename);
			endAskFile = true;
			if (botResponse < 1 && endAskFile && !downloading && xdcc == null)
			{
				ThreadManager.submitScheduleTask(restartDownload, 120);
			}
			return;
		}
		int LastloadingBotList;

		do
		{
			LastloadingBotList = loadingBotList;
			
			Iterator<Entry<SourceChan, ArrayList<BotPackage>>> it = sourcesBot.entrySet().iterator();
			
			while (it.hasNext())
			{
				botResponse = -1;
				Entry<SourceChan, ArrayList<BotPackage>> data = it.next();
				
				SourceChan chan = data.getKey();
				ArrayList<BotPackage> bots = data.getValue();
				
				if (!alreadyAskChannel.contains(chan))
				{
					alreadyAskChannel.add(chan);
				}
				else
				{
					continue;
				}
				
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
					if (DownloadManager.BotRequest.containsKey(bot.bot)
							&& !DownloadManager.BotRequest.get(bot.bot).filename.equals(this.filename))
					{
						AtLeastOneBotOccupated = true;
						continue;
					}
					DownloadManager.BotRequest.put(bot.bot, this);
					DownloadManager.servers.get(chan.server).requestFile(bot.bot, bot.packetID);
					int retry = 0;
					while (botResponse == -1)
					{
						FunctionsUtils.sleep(2000);
						retry++;
						if (retry > 3)
							break;
					}
					
					if (botResponse == 1)
					{
						FunctionsUtils.sleep(10000);
					}
					
					if (botResponse == 2)
					{
						RemoveFileFromWaitingList();
						return;
					}
				}
			}
			FunctionsUtils.sleep(200);
		} while (LastloadingBotList != loadingBotList || loadingBotList > 0);
		endAskFile = true;
		
		if (botResponse < 1 && endAskFile && !downloading && xdcc == null)
		{
			ThreadManager.submitScheduleTask(restartDownload, 120);
		}
	}
	
	public void RemoveFileFromWaitingList()
	{
		onWaitingList = false;
		Iterator<Entry<String, FileSources>> it = DownloadManager.BotRequest.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, FileSources> entry = it.next();
			FileSources s = entry.getValue();
			if (s.filename.equals(filename))
			{
				DownloadManager.BotRequest.remove(entry.getKey());
			}
		}
	}
	
	public void startDownloadXDCC(DccFileTransfer transfer)
	{
		// for reducing Heap Size
		sourcesBot.clear();
		totalBot = 0;
		RemoveFileFromWaitingList();
		transfer.filesources = this;
		
		if (paused)
		{
			Log.log(Log.IRC, "IN PAUSA:\t" + transfer.getFile().toString() + " " + transfer.getSize() + " bytes");
			transfer.close();
			return;
		}
		
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
		
		if (saveFile.exists() && (xdcc.getSize() <= saveFile.length()))
		{
			Log.log(Log.IRC, "EXISTS:\t try to close connection");
			xdcc.close();
			endXDCCTransfer(transfer, null);
		}
		else
		{
			Log.log(Log.IRC, "SAVING TO:\t" + saveFile.toString());
			xdcc.receive(saveFile, true);
		}
	}
	
	public void endXDCCTransfer(DccFileTransfer transfer, Exception ex)
	{
		if (ex != null && (xdcc.getSize() > saveFile.length()))
		{
			xdcc = null;
			downloading = false;
			FunctionsUtils.sleep(4000);
			if (!paused)
				restart();
			return;
		}
		
		fileEnd = true;
		
		SQLQuerySettings.removeDownload(filename);
		
		if (Settings.getParameter("removecompleted").equals("1"))
		{
			DownloadManager.AllFile.remove(filename);
			if (MainFrame.getInstance().internalPane instanceof Download)
			{
				MainFrame.getInstance().internalPane.loadComponent();
				MainFrame.getInstance().internalPane.repaint();
			}
		}
		
		Log.log(Log.IRC, "Trasferimento Completato: " + transfer.getFile().getName());
	}
	
	public void resetData()
	{
		totalBot = 0;
		loadingBotList = 0;
		botResponse = -1;
		downloading = false;
		if (xdcc != null)
		{
			xdcc.close();
			xdcc = null;
		}
		fileEnd = false;
		sourcesBot.clear();
		totalBot = 0;
		onWaitingList = false;
		RemoveFileFromWaitingList();
		endAskFile = false;
		AtLeastOneBotOccupated = false;
		alreadyAskChannel.clear();
		sourcesBot.clear();
	}
	
	public void stop()
	{
		paused = true;
		resetData();
		SQLQuerySettings.setStatus(filename, 1);
	}
	
	public void restart()
	{
		paused = false;
		resetData();
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
	
	public Runnable restartDownload = new Runnable()
	{
		public void run()
		{
			if (paused)
				return;
			if (downloading)
				return;
			if (xdcc != null)
				return;
			restart();
		}
	};
	
	public FileSources getInstance()
	{
		return this;
	}
}
