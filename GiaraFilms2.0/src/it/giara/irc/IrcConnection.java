package it.giara.irc;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.utils.ErrorHandler;
import it.giara.utils.Log;

public class IrcConnection extends PircBot
{
	
	public IrcConnection(String server)
	{
		super();
		
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
		this.sendRawLine("JOIN " + channel);
		this.sendMessage(channel, "Ciao a tutti :D");
	}
	
	@Override
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice)
	{
		if (notice.contains("per richiedere questo pack, devi essere un un chan in cui ci sia anche io"))
		{
			DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
			Log.log(Log.IRC, "NON SEI NEL CANALE DEL BOT" + sourceNick);
		}
		else if (notice.contains("pack errato"))
		{
			DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
			Log.log(Log.IRC, "Numero del pack Errato" + sourceNick);
		}
		else if (notice.contains("Tutti gli slots sono occupati"))
		{
			DownloadManager.BotRequest.get(sourceNick).botResponse = 0;
			Log.log(Log.IRC, "Il Bot ti ha messo in lista" + sourceNick);
		}
		else if (notice.contains("Ti sto inviando il pack"))
		{
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
		sources.xdcc = transfer;
		
		sources.startDownloadXDCC();
		
	}
	
	@Override
	protected void onFileTransferFinished(DccFileTransfer transfer, Exception ex)
	{
		FileSources sources = DownloadManager.BotRequest.get(transfer.getNick());
		sources.endXDCCTransfer(transfer, ex);
	}
	
}
