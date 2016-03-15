package it.giara.gui.section;

import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.AnimatedImageButton;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.Settings;
import it.giara.syncdata.ServerQuery;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.ThreadManager;

public class FilmInfoSchede extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	private DefaultGui back;
	private TMDBScheda scheda;
	
	JEditorPane text;
	JLabel cover;
	JLabel backGround;
	JScrollPane scroll;
	JLabel info;
	ImageButton downloads;
	
	public boolean syncRunning = false;
	AnimatedImageButton sync;
	
	public FilmInfoSchede(DefaultGui gui, TMDBScheda f)
	{
		super();
		back = gui;
		scheda = f;
		if(Settings.getParameter("servercollaborate").equals("1"))
		{
			syncRunning = true;
			Runnable r = new Runnable()
			{
				public void run()
				{
					ServerQuery.loadFileOfSchede(scheda);
					syncRunning = false;
					downloads.setVisible(!syncRunning);
					sync.setVisible(syncRunning);
				}
			};
			ThreadManager.submitCacheTask(r);
		}
	}
	
	public void loadComponent()
	{
		this.removeAll();
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 8, 0, FRAME_WIDTH * 3 / 4, 40);
		title.setText("<html><h1>" + scheda.title + "</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		backbt.setBounds(5, 5, 32, 32);
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		this.add(homePage);
		homePage.setToolTipText("Home");
		if (cover == null)
			cover = new JLabel();
			
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			cover.setBounds(20, 80, FRAME_WIDTH / 4, (int) ((FRAME_WIDTH / 4) * 1.5));
		else
			cover.setBounds(20, 80, (int) ((FRAME_HEIGHT / 2) * 0.67), FRAME_HEIGHT / 2);
			
		cover.setBorder(BorderFactory.createEtchedBorder());
		cover.setIcon(ImageUtils.getIcon(
				ImageUtils.scaleImage(scheda.initPoster_original(cover), cover.getWidth(), cover.getHeight())));
				
		add(cover);
		
		text = new JEditorPane();
		text.setEditable(false);
		text.setHighlighter(null);
		text.setOpaque(false);
		text.setContentType("text/html");
		text.setText("<html><h2>" + scheda.desc + "</html>");
		text.setBorder(BorderFactory.createEmptyBorder());
		text.setBackground(ColorUtils.Trasparent);
		
		scroll = new JScrollPane(text);
		scroll.setFocusable(true);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setBackground(ColorUtils.Trasparent);
		
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			scroll.setBounds(FRAME_WIDTH / 4 + 60, 80, FRAME_WIDTH - (FRAME_WIDTH / 4 + 80),
					(int) ((FRAME_WIDTH / 4) * 1.49) - 35);
		else
			scroll.setBounds((int) ((FRAME_HEIGHT / 2) * 0.67) + 60, 80,
					FRAME_WIDTH - ((int) ((FRAME_HEIGHT / 2) * 0.67) + 80), FRAME_HEIGHT / 2 - 35);
					
		scroll.setOpaque(false);
		scroll.setVisible(false);
		this.add(scroll);
		ThreadManager.submitCacheTask(UpdateText);
		
		info = new JLabel();
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			info.setBounds(FRAME_WIDTH / 4 + 60, (int) ((FRAME_WIDTH / 4) * 1.49) + 50,
					FRAME_WIDTH - (FRAME_WIDTH / 4 + 80), 30);
		else
			info.setBounds((int) ((FRAME_HEIGHT / 2) * 0.67) + 60, FRAME_HEIGHT / 2 + 50,
					FRAME_WIDTH - ((int) ((FRAME_HEIGHT / 2) * 0.67) + 80), 30);
					
		info.setText("<html><h4> " + "Data di Uscita: " + scheda.relese + "<br>" + "Generi: " + scheda.getGeneri()
				+ "</html>");
		info.setVisible(true);
		this.add(info);
		drawRating();
		
		downloads = new ImageButton(ImageUtils.getImage("gui/download.png"),
				ImageUtils.getImage("gui/download_over.png"), ImageUtils.getImage("gui/download_over.png"),
				OpenDownloads);
		downloads.setBounds((FRAME_WIDTH - 64) / 2, FRAME_HEIGHT - 100, 64, 64);
		downloads.setToolTipText("Scarica");
		downloads.setVisible(!syncRunning);
		this.add(downloads);
		

			sync = new AnimatedImageButton("SyncBig(n)", 5, null, 500);
		sync.setBounds((FRAME_WIDTH - 64) / 2, FRAME_HEIGHT - 100, 64, 64);
		sync.setToolTipText("Sincronizzazione con il server Attedere qualche secondo");
		sync.setVisible(syncRunning);
		this.add(sync);
		
		
		if (backGround == null)
		{
			backGround = new JLabel();
			scheda.initBack_w1920(backGround);
		}
		
		backGround.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		
		if (scheda.back_w1920 != null)
		{
			if (backGround.getWidth() > backGround.getHeight())
			{
				BufferedImage img = ImageUtils.scaleWithAspectHeight(scheda.back_w1920, backGround.getHeight());
				if (img.getWidth() < backGround.getWidth())
					img = ImageUtils.scaleWithAspectWidth(scheda.back_w1920, backGround.getWidth());
				backGround.setIcon(ImageUtils.getIcon(img));
			}
			else
			{
				BufferedImage img = ImageUtils.scaleWithAspectWidth(scheda.back_w1920, backGround.getWidth());
				if (img.getHeight() < backGround.getHeight())
					img = ImageUtils.scaleWithAspectHeight(scheda.back_w1920, backGround.getHeight());
				backGround.setIcon(ImageUtils.getIcon(img));
			}
		}
		this.add(backGround);
		
		super.loadComponent();
	}
	
	public void drawRating()
	{
		int x = 0;
		for (x = 0; x < scheda.vote - 1; x++)
		{
			JLabel star = new JLabel();
			if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
				star.setBounds(20 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
			else
				star.setBounds(20 + (20 * x), FRAME_HEIGHT / 2 + 95, 20, 20);
				
			star.setIcon(ImageUtils.getIcon("star_full.png"));
			this.add(star);
		}
		if (x + 0.5f >= scheda.vote)
		{
			JLabel star = new JLabel();
			if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
				star.setBounds(20 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
			else
				star.setBounds(20 + (20 * x), FRAME_HEIGHT / 2 + 95, 20, 20);
			star.setIcon(ImageUtils.getIcon("star_half.png"));
			this.add(star);
			x++;
		}
		for (; x < 10; x++)
		{
			JLabel star = new JLabel();
			if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
				star.setBounds(20 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
			else
				star.setBounds(20 + (20 * x), FRAME_HEIGHT / 2 + 95, 20, 20);
			star.setIcon(ImageUtils.getIcon("star_empty.png"));
			this.add(star);
		}
		this.repaint();
	}
	
	Runnable OpenDownloads = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new DownloadFilm(guiInstance, scheda));
		}
	};
	
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
			Search.searchService.StopService();
			MainFrame.getInstance().setInternalPane(new HomePage());
		}
	};
	
	Runnable UpdateText = new Runnable()
	{
		@Override
		public void run()
		{
			FunctionsUtils.sleep(10);
			text.setText("<html><h2>" + scheda.desc + "</html>");
			scroll.setVisible(true);
		}
	};
	
}
