package it.giara.gui.section;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.QualityAudio;
import it.giara.analyze.enums.QualityVideo;
import it.giara.download.DownloadManager;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.FunctionsUtils;

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
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/icon32px/arrow_up.png"), ImageUtils.getImage("gui/icon32px/arrow_up_over.png"),
				ImageUtils.getImage("gui/icon32px/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/icon32px/arrow_down.png"),
				ImageUtils.getImage("gui/icon32px/arrow_down_over.png"), ImageUtils.getImage("gui/icon32px/arrow_down_over.png"),
				RunDown);
		serie = Serie;
		EpMap = map;
		nEpisode = nEP;
		download = ImageUtils.getImage("gui/icon32px/download.png");
		download_over = ImageUtils.getImage("gui/icon32px/download_over.png");
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
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/icon32px/arrow_left.png"),
				ImageUtils.getImage("gui/icon32px/arrow_left_over.png"), ImageUtils.getImage("gui/icon32px/arrow_left_over.png"),
				BackGui);
		backbt.setBounds(5, 5, 32, 32);
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/icon32px/home.png"),
				ImageUtils.getImage("gui/icon32px/home_over.png"), ImageUtils.getImage("gui/icon32px/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		homePage.setToolTipText("Home");
		this.add(homePage);
		
		int num = 0;
		int itt = 0;
		int line = nEpisode / ((FRAME_WIDTH * 7 / 9) / 20) + 1;
		for (final Entry<String, HashMap<Integer, String>> Tag : EpMap.entrySet())
		{
			itt++;
			if (itt <= offset)
				continue;
				
			FileInfo finfo = new FileInfo(Tag.getKey()+"",false);
			finfo.parseTags();
			
			
			if (finfo.video != QualityVideo.NULL)
			{
				JLabel Vquality = new JLabel();
				Vquality.setText("" + (int) finfo.video.qualita);
				Vquality.setBounds(FRAME_WIDTH * 8 / 9-75, 45 + num * (25 + 10 + 20 * line)+2, 20, 20);
				Vquality.setBackground(ColorUtils.getColorQuality(finfo.video.qualita));
				Vquality.setToolTipText("Video: " + finfo.video.name() + " " + finfo.video.descrizione);
				Vquality.setHorizontalAlignment(JLabel.CENTER);
				Vquality.setOpaque(true);
				Vquality.setForeground(ColorUtils.Back);
				this.add(Vquality);
			}
			
			if (finfo.audio != QualityAudio.NULL)
			{
				JLabel Aquality = new JLabel();
				Aquality.setText("" + (int) finfo.audio.qualita);
				Aquality.setBounds(FRAME_WIDTH * 8 / 9-50, 45 + num * (25 + 10 + 20 * line)+2, 20, 20);
				Aquality.setBackground(ColorUtils.getColorQuality(finfo.audio.qualita));
				Aquality.setToolTipText("Audio: " + finfo.audio.name() + " " + finfo.audio.descrizione);
				Aquality.setHorizontalAlignment(JLabel.CENTER);
				Aquality.setOpaque(true);
				Aquality.setForeground(ColorUtils.Back);
				this.add(Aquality);
			}
			
			if ((finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH)
					|| finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN)
					|| finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed)))
			{
				String text = "";
				String textTip = "";
				Color c = ColorUtils.Back;
				if (finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH))
				{
					text = "EN";
					c = new Color(0, 128, 255);
					textTip += "Inglese ";
				}
				if (finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN))
				{
					text = "IT";
					c = new Color(0, 204, 0);
					textTip += "Italiano ";
				}
				if (finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed))
				{
					text = "EN";
					c = new Color(0, 128, 255);
					textTip += "Sottotitolato ";
				}
				
				JLabel Tags = new JLabel();
				Tags.setText(text);
				Tags.setBounds(FRAME_WIDTH * 8 / 9-25, 45 + num * (25 + 10 + 20 * line)+2, 20, 20);
				Tags.setBackground(c);
				Tags.setToolTipText("Lingua: " + textTip);
				Tags.setHorizontalAlignment(JLabel.CENTER);
				Tags.setOpaque(true);
				Tags.setForeground(ColorUtils.Back);
				this.add(Tags);
			}
			
			
			
			JLabel tagName = new JLabel();
			tagName.setBounds(FRAME_WIDTH / 9, 45 + num * (25 + 10 + 20 * line), FRAME_WIDTH * 7 / 9-80, 25);
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
					FunctionsUtils.sleep(100);
					MainFrame.getInstance().setInternalPane(new Download(back));
				}
			});
			downloads.setBounds(FRAME_WIDTH * 8 / 9 - 25-80, 45 + num * (25 + 10 + 20 * line) + 2, 21, 21);
			downloads.setToolTipText("Scarica Compilation");
			this.add(downloads);
			
			num++;
		}
		
		ArrowUp.setBounds(this.getWidth() - 57, 45, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 57, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible(offset < (EpMap.size() - 1));
		this.add(ArrowDown);
		
		super.loadComponent();
	}
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(back);
		}
	};
	
	Runnable OpenHomePage = new Runnable()
	{
		@Override
		public void run()
		{
			if (Search.searchService != null)
			Search.searchService.StopService();
			MainFrame.getInstance().setInternalPane(new HomePage());
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
