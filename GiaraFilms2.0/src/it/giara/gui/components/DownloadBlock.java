package it.giara.gui.components;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import it.giara.download.FileSources;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.StringUtils;

public class DownloadBlock extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static BufferedImage stop, stop_over, start, start_over, delete, delete_over;
	
	static
	{
		stop = ImageUtils.getImage("gui/stop.png");
		stop_over = ImageUtils.getImage("gui/stop_over.png");
		start = ImageUtils.getImage("gui/start.png");
		start_over = ImageUtils.getImage("gui/start_over.png");
		delete = ImageUtils.getImage("gui/delete.png");
		delete_over = ImageUtils.getImage("gui/delete_over.png");
	}
	
	FileSources file;
	JProgressBar bar;
	ImageButton startBt;
	ImageButton stopBt;
	JLabel name;
	JLabel size;
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
		this.add(bar);
		
		size = new JLabel();
		size.setBounds(0, 45, g.FRAME_WIDTH / 4, 30);
		size.setHorizontalAlignment(JLabel.CENTER);
		size.setVisible(false);
		this.add(size);
		
		startBt = new ImageButton(start, start_over, start_over, startRun);
		startBt.setBounds(g.FRAME_WIDTH * 3 / 4 + 10, 44, 32, 32);
		this.add(startBt);
		
		stopBt = new ImageButton(stop, stop_over, stop_over, stopRun);
		stopBt.setBounds(g.FRAME_WIDTH * 3 / 4 + 10, 44, 32, 32);
		this.add(stopBt);
		
		startBt.setVisible(file.paused);
		stopBt.setVisible(!file.paused);
		
		ImageButton deleteBt = new ImageButton(delete, delete_over, delete_over, deleteRun);
		deleteBt.setBounds(g.FRAME_WIDTH * 3 / 4 + 52, 44, 32, 32);
		
		this.add(deleteBt);
		
		updateBarData();
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
		
		if (file.loadingBotList > 0 && file.totalBot == 0 && !file.downloading)
			bar.setString("Scansione Sorgenti In Corso " + file.loadingBotList + " rimanenti");
		else if (file.botResponse < 1 && file.endAskFile && file.onWaitingList)
			bar.setString("Sei in Lista d'attesa per il download");
		else if (file.botResponse < 1 && file.endAskFile && file.AtLeastOneBotOccupated)
			bar.setString("Tutti i Bot sono occupati, ritento connessione tra 2min");
		else if (file.botResponse < 1 && file.endAskFile)
			bar.setString("File non disponibile, ritento connessione tra 2min");
		else if (file.botResponse < 1)
			bar.setString("In attesa di connessione" + loading);
		else if (file.botResponse == 1)
			bar.setString("Connessione in corso" + loading);
		else if (file.botResponse == 2 && file.xdcc != null)
			bar.setString("Download in corso: " + FunctionsUtils.arrotondamento(file.xdcc.getProgressPercentage())
					+ "%  " + StringUtils.humanReadableByteCount(file.xdcc.getTransferRate(), 1) + "/s  "+StringUtils.humanReadableSecondLeft((int) ((file.xdcc.getSize()-file.xdcc.getProgress())/(file.xdcc.getTransferRate()+1))));
		if (file.fileEnd)
			bar.setString("Download Completato");
			
		if (file.paused)
			bar.setString("Download In Pausa");
			
		if (file.xdcc != null)
		{
			bar.setEnabled(true);
			bar.setMaximum((int) file.xdcc.getSize() / 1000);
			bar.setValue((int) file.xdcc.getProgress() / 1000);
			size.setVisible(true);
			size.setText("<html> <h4>" + StringUtils.humanReadableByteCount(file.xdcc.getProgress(), 2) + " / "
					+ StringUtils.humanReadableByteCount(file.xdcc.getSize(), 2) + "</html>");
		}
		else
		{
			size.setVisible(false);
			bar.setEnabled(false);
		}
		
		startBt.setVisible(file.paused);
		stopBt.setVisible(!file.paused);
	}
	
	Runnable deleteRun = new Runnable()
	{
		@Override
		public void run()
		{
			int response = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler rimuovere " + file.filename,
					"Conferma di Eliminazione", JOptionPane.YES_NO_OPTION);
					
			if (response == JOptionPane.YES_OPTION)
			{
				file.delete();
				MainFrame.getInstance().internalPane.init(MainFrame.getInstance().FRAME_WIDTH,
						MainFrame.getInstance().FRAME_HEIGHT);
				MainFrame.getInstance().internalPane.repaint();
			}	
		}
	};
	
	Runnable stopRun = new Runnable()
	{
		@Override
		public void run()
		{
			file.stop();
			updateBarData();
		}
		
	};
	
	Runnable startRun = new Runnable()
	{
		
		@Override
		public void run()
		{
			file.restart();
			updateBarData();
		}
		
	};
	
}
