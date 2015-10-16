package it.giara.gui.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import it.giara.download.FileSources;
import it.giara.gui.DefaultGui;
import it.giara.gui.utils.ColorUtils;
import it.giara.utils.FunctionsUtils;

public class DownloadBlock extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	FileSources file;
	JProgressBar bar;
	JLabel name;
	int progress = 0;
	
	public DownloadBlock(FileSources fs, DefaultGui g)
	{
		setLayout(null);
		setOpaque(true);
		setBackground(ColorUtils.Back);
		file = fs;
		
		name = new JLabel();
		name.setBounds(g.FRAME_WIDTH / 8, 5, g.FRAME_WIDTH * 3 / 4, 30);
		name.setText("<html> <h3>" + file.filename + "</html>");
		name.setHorizontalAlignment(JLabel.CENTER);
		this.add(name);
		
		bar = new JProgressBar();
		bar.setBounds(g.FRAME_WIDTH / 4, 45, g.FRAME_WIDTH / 2, 30);
		bar.setForeground(Color.WHITE);
		bar.setStringPainted(true);
		updateBarData();
		this.add(bar);
	}
	
	
	public void updateBarData()
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
	
}
