package it.giara.gui.section;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadBlock;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.FunctionsUtils;

public class Download extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	Timer timer;
	int progress = 0;
	
	public Download(DefaultGui gui)
	{
		super();
		back = gui;
		timer = new Timer(1000, update);
		timer.start();
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		int x = -1;
		for (Entry<String, FileSources> data : DownloadManager.AllFile.entrySet())
		{
			x++;
			FileSources file = data.getValue();
			int off = 80 * x;
			
			DownloadBlock downbk = new DownloadBlock(file, this);
			
			downbk.setBounds(5, off + 45, FRAME_WIDTH - 10, 80);
			downbk.setBorder(BorderFactory.createEtchedBorder());
			this.add(downbk);
			
		}
	}
	
	public void updateBarData(JProgressBar bar, FileSources file)
	{
		progress++;
		if (progress > 3)
		{
			progress = 0;
		}
		String loading = "";
		for (int k = 0; k < progress; k++)
		{
			loading += ".";
		}
		
		if (file.loadingBotList > 0)
			bar.setString("Scansione Sorgenti In Corso " + file.loadingBotList + " rimanenti");
		else if (file.botResponse < 1)
			bar.setString("In attesa di connessione" + loading);
		else if (file.botResponse == 1)
			bar.setString("Connessione in corso" + loading);
		else if (file.botResponse == 2 && file.xdcc != null)
			bar.setString("Download in corso: " + FunctionsUtils.arrotondamento(file.xdcc.getProgressPercentage())
					+ "%  " + file.xdcc.getTransferRate() / 1024 + " kb/s");
		if (file.fileEnd)
			bar.setString("Download Completato");
			
		if (file.xdcc != null)
		{
			bar.setEnabled(true);
			bar.setMaximum((int) file.xdcc.getSize() / 1000);
			bar.setValue((int) file.xdcc.getProgress() / 1000);
		}
		else
		{
			bar.setEnabled(false);
		}
		
	}
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
			timer.stop();
			MainFrame.getInstance().setInternalPane(back);
		}
	};
	
	ActionListener update = new ActionListener()
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			for (Component comp : getComponents())
			{
				if (comp instanceof DownloadBlock)
				{
					DownloadBlock dw = (DownloadBlock) comp;
					dw.updateBarData();
				}
			}
		}
		
	};
}
