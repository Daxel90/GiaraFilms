package it.giara.irc;

import java.util.ArrayList;
import java.util.List;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.utils.ErrorHandler;
import it.giara.utils.Log;

public class IrcConnection extends PircBot
{
	List<String> channleJoined = new ArrayList<String>();
	
	public IrcConnection(String server)
	{
		super();
		Log.log(Log.IRC, "Mi connetto a: " + server);
		IrcNick nickProvider = new IrcNick();
		this.setLogin(nickProvider.generateRandomNick());
		this.setName(nickProvider.generateRandomNick());
		this.setFinger(nickProvider.generateRandomNick());
		this.setVersion("GiaraFilms");
		this.setAutoNickChange(true);
		this.setVerbose(true);
		
		boolean connect = false;
		int retry = 0;
		
		while (!connect && retry < 10)
		{
			try
			{
				connect(server);
				connect = true;
			} catch (Exception e)
			{
				Log.stack(Log.IRC, e);
				Log.log(Log.IRC, "Impossibile connettersi a: " + server);
				connect = false;
				retry++;
			}
		}
		
		if (!connect)
		{
			ErrorHandler.FailServerConnect("Impossibile connettersi al server " + server);
		}
	}
	
	public void joinChannelAndSayHello(String channel)
	{
		if(!channleJoined.contains(channel))
		{
			this.joinChannel(channel);
			this.sendMessage(channel, "Ciao a tutti :D");
			channleJoined.add(channel);
		}
	}
	
	@Override
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice)
	{
		if (notice.contains("per richiedere questo pack, devi essere un un chan in cui ci sia anche io")
				|| notice.contains("you must be on a known channel to request a pack"))
		{
			if (DownloadManager.BotRequest.containsKey(sourceNick))
				DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
			Log.log(Log.IRC, "NON SEI NEL CANALE DEL BOT" + sourceNick);
		}
		else if (notice.contains("pack errato"))
		{
			if (DownloadManager.BotRequest.containsKey(sourceNick))
				DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
			Log.log(Log.IRC, "Numero del pack Errato" + sourceNick);
		}
		else if (notice.contains("Tutti gli slots sono occupati") || notice.contains("Sei già in coda per questo pack"))
		{
			if (DownloadManager.BotRequest.containsKey(sourceNick))
			{
				DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
				DownloadManager.BotRequest.get(sourceNick).onWaitingList = true;
				if (!DownloadManager.BotRequest.get(sourceNick).waitingList.contains(sourceNick))
					DownloadManager.BotRequest.get(sourceNick).waitingList.add(sourceNick);
			}
			Log.log(Log.IRC, "Il Bot ti ha messo in lista" + sourceNick);
		}
		else if (notice.contains("Ti sto inviando il pack"))
		{
			if (DownloadManager.BotRequest.containsKey(sourceNick))
				DownloadManager.BotRequest.get(sourceNick).botResponse = 1;
		}
	}
	
	@Override
	public void onIncomingFileTransfer(DccFileTransfer transfer)
	{
		
		FileSources sources = DownloadManager.BotRequest.get(transfer.getNick());
		
		if (sources == null)
		{
			ErrorHandler
					.noRequestFileFromThisBot("Non è stato richiesto nessun file da questo bot: " + transfer.getNick());
			transfer.close();
			return;
		}
		else if (!sources.filename.trim().equals(transfer.getFile().getName().trim()))
		{
			ErrorHandler.BotSendWrongFile(sources.filename.trim(), transfer.getFile().getName().trim());
			transfer.close();
			sources.botResponse = 0;
			return;
		}
		if (sources.downloading == true)
		{
			ErrorHandler.alreadyInDownload(transfer.getFile().getName().trim());
			transfer.close();
			return;
		}
		
		sources.botResponse = 2;
		sources.downloading = true;
		
		sources.startDownloadXDCC(transfer);
		
	}
	
	@Override
	protected void onFileTransferFinished(DccFileTransfer transfer, Exception ex)
	{
		if (DownloadManager.BotRequest.containsKey(transfer.getNick()))
		{
			FileSources sources = DownloadManager.BotRequest.get(transfer.getNick());
			sources.endXDCCTransfer(transfer, ex);
		}
	}
	
}
