package it.giara.gui.section;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.Log;

public class DownloadAllEpisode extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	private ImageButton ArrowUp, ArrowDown;
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
		
		int line = nEpisode / ((FRAME_WIDTH * 7 / 9)/20)+1;
		Log.log(Log.DEBUG, line);
		for (Entry<String, HashMap<Integer, String>> Tag : EpMap.entrySet())
		{
			JLabel tagName = new JLabel();
			tagName.setBounds(FRAME_WIDTH / 9, 45 +num * (25+10+20*line), FRAME_WIDTH * 7 / 9, 25);
			tagName.setText("<html><h3>" + Tag.getKey() + "</html>");
			tagName.setHorizontalAlignment(JLabel.CENTER);
			tagName.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
			this.add(tagName);
			
			for (int x = 1; x <= nEpisode; x++)
			{
				JLabel icon = new JLabel();
				icon.setBounds(FRAME_WIDTH / 9 +(x-1)%((FRAME_WIDTH * 7 / 9)/20)*20, 78 + num * (25+10+20*line) +20*((x-1)/((FRAME_WIDTH * 7 / 9)/20)), 17, 17);
				icon.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
				icon.setHorizontalAlignment(JLabel.CENTER);
				icon.setText(""+x);
				if(EpMap.get(Tag.getKey()).containsKey(x))
				{
					icon.setForeground(Color.GREEN);
				}
				else
				{
					icon.setForeground(Color.RED);
				}
				this.add(icon);
			}
			
			num++;
		}
		
		ArrowUp.setBounds(this.getWidth() - 57, 45, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 57, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible(true);
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
			offset -= 2;
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
			offset += 2;
			loadComponent();
			repaint();
		}
	};
}
