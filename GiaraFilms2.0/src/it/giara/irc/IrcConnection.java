package it.giara.irc;

import java.io.File;

import javax.swing.JOptionPane;

import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;

import it.giara.utils.DirUtils;
import it.giara.utils.Log;

public class IrcConnection extends PircBot
{
	private DccFileTransfer curentTransfer;
	
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
				Log.log(Log.IRC, "Impossibile connettersi a: "+server);
				connect = false;
				retry++;
			}
		}
		
		if (!connect)
		{
			JOptionPane.showMessageDialog(null, "Impossibile connettersi al server " + server);
		}
		
		// this.sendMessage("RLD|CINE|002 ","xdcc send #94");
	}
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		if (message.equalsIgnoreCase("time"))
		{
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
	}
	
	@Override
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice)
	{
		if (notice.contains("per richiedere questo pack, devi essere un un chan in cui ci sia anche io"))
		{
			Log.log(Log.IRC, "NON SEI NEL CANALE DEL BOT" + sourceNick);
		}
		
		if (notice.contains("Invalid Pack Number"))
		{
			Log.log(Log.IRC, "DONE LEECHING BOT " + sourceNick);
		}
		
		if (notice.contains("point greater"))
		{
			Log.log(Log.IRC, "EXISTS:\t try to close connection");
			
			curentTransfer.close();
			// this.sendMessage(botName,"XDCC remove");
			
		}
		
		if (notice.contains("Closing Connection: Pack file changed"))
		{
			// TODO do something here (retry?)
			Log.log(Log.IRC, "PACK file changed");
		}
		
	}
	
	@Override
	public void onIncomingFileTransfer(DccFileTransfer transfer)
	{
		curentTransfer = transfer;
		
		File saveFile = new File(DirUtils.getDownloadDir(), transfer.getFile().getName());
		
		if (!saveFile.getParentFile().exists())
			saveFile.getParentFile().mkdir();
			
		Log.log(Log.IRC, "INCOMING:\t" + transfer.getFile().toString() + " " + transfer.getSize() + " bytes");
		
		if (saveFile.exists() && (transfer.getSize() == saveFile.length()))
		{
			Log.log(Log.IRC, "EXISTS:\t try to close connection");
			transfer.close();
			
		}
		else
		{
			Log.log(Log.IRC, "SAVING TO:\t" + saveFile.toString());
			transfer.receive(saveFile, true);
		}
		
		new Thread()
		{
			
			@Override
			public void run()
			{
				long data = 0;
				int speed = 0;
				while (true)
				{
					speed = (int) (curentTransfer.getProgress() - data);
					data = curentTransfer.getProgress();
					System.out.println("Size: " + curentTransfer.getProgress());
					System.out.println("Percent: " + curentTransfer.getProgressPercentage());
					System.out.println("Speed: " + speed);
					
					// DownloadPanel.updateDownload(curentTransfer.getFile().getName(),
					// curentTransfer.getProgressPercentage(), speed);
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
			}
		}.start();
		
	}
	
	@Override
	protected void onFileTransferFinished(DccFileTransfer transfer, Exception ex)
	{
		if (ex != null)
		{
			System.out.println(ex.getClass().getName() + " -> " + ex.getMessage());
		}
		curentTransfer = null;
		
		Log.log(Log.IRC, "FINISHED:\t Transfer finished for " + transfer.getFile().getName());
		
	}
	
	// public void download(Bot b)
	// {
	// this.sendMessage(b.name, "xdcc send " + b.packId);
	// }
	
}
