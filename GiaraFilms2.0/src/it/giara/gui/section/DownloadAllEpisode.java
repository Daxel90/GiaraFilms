package it.giara.gui.section;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.download.DownloadManager;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class DownloadAllEpisode extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	private ImageButton ArrowUp, ArrowDown;
	private BufferedImage download, download_over;
	int offset = 0;
	int serie;
	int nEpisode;
	HashMap<String, HashMap<Integer, String>> EpMap;
	
	public DownloadAllEpisode(DefaultGui gui, int Serie, int nEP, HashMap<String, HashMap<Integer, String>> map)
	{
		super();
		back = gui;
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
		serie = Serie;
		EpMap = map;
		nEpisode = nEP;
		download = ImageUtils.getImage("gui/download.png");
		download_over = ImageUtils.getImage("gui/download_over.png");
	}
	
	public void loadComponent()
	{
		this.removeAll();
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Download Serie " + serie + "</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		
		int num = 0;
		int itt = 0;
		int line = nEpisode / ((FRAME_WIDTH * 7 / 9) / 20) + 1;
		for (final Entry<String, HashMap<Integer, String>> Tag : EpMap.entrySet())
		{
			itt++;
			if (itt <= offset)
				continue;
				
			JLabel tagName = new JLabel();
			tagName.setBounds(FRAME_WIDTH / 9, 45 + num * (25 + 10 + 20 * line), FRAME_WIDTH * 7 / 9, 25);
			tagName.setText("<html><h3>" + Tag.getKey() + "</html>");
			tagName.setHorizontalAlignment(JLabel.CENTER);
			tagName.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
			this.add(tagName);
			
			for (int x = 1; x <= nEpisode; x++)
			{
				JLabel icon = new JLabel();
				icon.setBounds(FRAME_WIDTH / 9 + (x - 1) % ((FRAME_WIDTH * 7 / 9) / 20) * 20,
						78 + num * (25 + 10 + 20 * line) + 20 * ((x - 1) / ((FRAME_WIDTH * 7 / 9) / 20)), 17, 17);
				icon.setBorder(BorderFactory.createEtchedBorder());
				icon.setHorizontalAlignment(JLabel.CENTER);
				icon.setText("" + x);
				if (EpMap.get(Tag.getKey()).containsKey(x))
				{
					icon.setForeground(Color.GREEN);
				}
				else
				{
					icon.setForeground(Color.RED);
				}
				this.add(icon);
			}
			
			ImageButton downloads = new ImageButton(download, download_over, download_over, new Runnable()
			{
				@Override
				public void run()
				{
					DownloadManager.downloadCollection(EpMap.get(Tag.getKey()));
					MainFrame.getInstance().setInternalPane(new Download(back));
				}
			});
			downloads.setBounds(FRAME_WIDTH * 8 / 9 - 25, 45 + num * (25 + 10 + 20 * line) + 2, 21, 21);
			this.add(downloads);
			
			num++;
		}
		
		ArrowUp.setBounds(this.getWidth() - 57, 45, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 57, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible(offset < (EpMap.size() - 1));
		this.add(ArrowDown);
	}
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(back);
		}
	};
	
	Runnable RunUp = new Runnable()
	{
		@Override
		public void run()
		{
			offset--;
			if (offset < 0)
				offset = 0;
			loadComponent();
			repaint();
		}
	};
	
	Runnable RunDown = new Runnable()
	{
		@Override
		public void run()
		{
			offset++;
			loadComponent();
			repaint();
		}
	};
}
