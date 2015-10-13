package it.giara.gui.components;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.download.DownloadManager;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.section.Download;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.ThreadManager;

public class DownloadList extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	ArrayList<String> fileList;
	int offset = 0;
	private ImageButton ArrowUp, ArrowDown;
	private BufferedImage download, download_over;
	private DefaultGui gui;
	
	public DownloadList(ArrayList<String> list, DefaultGui g)
	{
		fileList = list;
		gui = g;
		setLayout(null);
		setOpaque(true);
		setBackground(ColorUtils.Back);
		
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
		download = ImageUtils.getImage("gui/download.png");
		download_over = ImageUtils.getImage("gui/download_over.png");
	}
	
	public void init()
	{
		this.removeAll();
		int n = 0;
		for (final String file : fileList)
		{
			if (n < offset || 10 + (n - offset) * 40 > this.getHeight())
			{
				n++;
				continue;
			}
			
			JLabel name = new JLabel();
			name.setText("<html><h3>" + file + "</html>");
			name.setBounds(10, 10 + (n - offset) * 40, this.getWidth() - 50, 30);
			name.setBorder(BorderFactory.createEtchedBorder());
			this.add(name);
			
			ImageButton downloads = new ImageButton(download, download_over, download_over, new Runnable()
			{
				@Override
				public void run()
				{
					Runnable task = new Runnable()
					{
						public void run()
						{
							DownloadManager.downloadFile(file);
						}
					};
					ThreadManager.submitCacheTask(task);
					MainFrame.getInstance().setInternalPane(new Download(gui.guiInstance));
				}
			});
			downloads.setBounds(this.getWidth() - 80, 11 + (n - offset) * 40, 28, 28);
			this.add(downloads);
			n++;
		}
		
		ArrowUp.setBounds(this.getWidth() - 37, 10, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 37, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible(fileList.size() > (this.getHeight() - 10) / 40 + offset);
		this.add(ArrowDown);
		
	}
	
	public void addFile(String s)
	{
		fileList.add(s);
		init();
	}
	
	Runnable RunUp = new Runnable()
	{
		@Override
		public void run()
		{
			offset -= 2;
			if (offset < 0)
				offset = 0;
			init();
			repaint();
		}
		
	};
	
	Runnable RunDown = new Runnable()
	{
		@Override
		public void run()
		{
			offset += 2;
			init();
			repaint();
		}
		
	};
	
}
