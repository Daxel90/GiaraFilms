package it.giara.gui.section;

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
import it.giara.http.HTTPFilmInfo;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.SchedaFilm;
import it.giara.utils.ThreadManager;

public class FilmInfoSchede extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	private DefaultGui back;
	private PreSchedaFilm film;
	private SchedaFilm scheda;
	private AnimatedImageButton loading;
	
	JEditorPane text;
	JLabel cover;
	JScrollPane scroll;
	JLabel info;
	ImageButton downloads;
	
	public FilmInfoSchede(DefaultGui gui, PreSchedaFilm f)
	{
		super();
		back = gui;
		film = f;
		final HTTPFilmInfo getInfo = new HTTPFilmInfo(film.link);
		scheda = getInfo.film;
		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				getInfo.getInfo();
			}
		};
		ThreadManager.submitCacheTask(run);
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
		title.setText("<html><h1>" + film.Titolo + "</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		
		ImageButton back = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		back.setBounds(5, 5, 32, 32);
		this.add(back);
		
		if (scheda.loading)
		{
			loading = new AnimatedImageButton("SyncBig(n)", 5, null, UpdateStatus);
			loading.setBounds((FRAME_WIDTH - 128) / 2, (FRAME_HEIGHT - 128) / 2, 128, 128);
			add(loading);
		}
		if (cover == null)
			cover = new JLabel();
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			cover.setBounds(20, 80, FRAME_WIDTH / 4, (int) ((FRAME_WIDTH / 4) * 1.5));
		else
			cover.setBounds(20, 80, (int) ((FRAME_HEIGHT / 2) * 0.67), FRAME_HEIGHT / 2);
		cover.setBorder(BorderFactory.createEtchedBorder());
		if (scheda.img != null)
			cover.setIcon(ImageUtils
					.getIcon(ImageUtils.scaleImageOld(scheda.initImage(cover), cover.getWidth(), cover.getHeight())));
		cover.setVisible(!scheda.loading);
		add(cover);
		
		text = new JEditorPane();
		text.setEditable(false);
		text.setHighlighter(null);
		text.setOpaque(false);
		text.setContentType("text/html");
		text.setText("<html><h2>" + scheda.desc + "</html>");
		text.setBorder(BorderFactory.createEmptyBorder());
		text.setBackground(ColorUtils.Back);
		
		scroll = new JScrollPane(text);
		scroll.setFocusable(false);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setBackground(ColorUtils.Back);
		
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			scroll.setBounds(FRAME_WIDTH / 4 + 60, 80, FRAME_WIDTH - (FRAME_WIDTH / 4 + 80),
					(int) ((FRAME_WIDTH / 4) * 1.49) - 35);
		else
			scroll.setBounds((int) ((FRAME_HEIGHT / 2) * 0.67) + 60, 80,
					FRAME_WIDTH - ((int) ((FRAME_HEIGHT / 2) * 0.67) + 80), FRAME_HEIGHT / 2 - 35);
					
		scroll.setVisible(!scheda.loading);
		scroll.setOpaque(false);
		this.add(scroll);
		
		info = new JLabel();
		if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
			info.setBounds(FRAME_WIDTH / 4 + 60, (int) ((FRAME_WIDTH / 4) * 1.49) + 50,
					FRAME_WIDTH - (FRAME_WIDTH / 4 + 80), 30);
		else
			info.setBounds((int) ((FRAME_HEIGHT / 2) * 0.67) + 60, FRAME_HEIGHT / 2 + 50,
					FRAME_WIDTH - ((int) ((FRAME_HEIGHT / 2) * 0.67) + 80), 30);
		info.setText("<html><h4> " + "Anno: " + film.anno
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Paese: " + film.nazionalita
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Regia: " + film.regia + "<br>"
				+ "Genere: " + film.getGeneri().replace(",", ", ") + "</html>");
		info.setVisible(!scheda.loading);
		this.add(info);
		drawRating();
		
		downloads = new ImageButton(ImageUtils.getImage("gui/download.png"),
				ImageUtils.getImage("gui/download_over.png"), ImageUtils.getImage("gui/download_over.png"),
				OpenDownloads);
		downloads.setBounds((FRAME_WIDTH - 64) / 2, FRAME_HEIGHT - 100, 64, 64);
		downloads.setVisible(!scheda.loading);
		this.add(downloads);
		
	}
	
	public void drawRating()
	{
		if (!scheda.loading)
		{
			int x = 0;
			for (x = 0; x < scheda.vote - 1; x++)
			{
				JLabel star = new JLabel();
				if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
					star.setBounds(20 + FRAME_WIDTH / 8 - 50 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
				else
					star.setBounds(20 + (int) ((FRAME_HEIGHT / 2) * 0.67) / 2 - 50 + (20 * x), FRAME_HEIGHT / 2 + 95,
							20, 20);
				star.setIcon(ImageUtils.getIcon("star_full.png"));
				this.add(star);
			}
			if (x + 0.5f >= scheda.vote)
			{
				JLabel star = new JLabel();
				if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
					star.setBounds(20 + FRAME_WIDTH / 8 - 50 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
				else
					star.setBounds(20 + (int) ((FRAME_HEIGHT / 2) * 0.67) / 2 - 50 + (20 * x), FRAME_HEIGHT / 2 + 95,
							20, 20);
				star.setIcon(ImageUtils.getIcon("star_half.png"));
				this.add(star);
				x++;
			}
			for (; x < 5; x++)
			{
				JLabel star = new JLabel();
				if ((int) ((FRAME_WIDTH / 4) * 1.49) < FRAME_HEIGHT / 2)
					star.setBounds(20 + FRAME_WIDTH / 8 - 50 + (20 * x), (int) ((FRAME_WIDTH / 4) * 1.49) + 95, 20, 20);
				else
					star.setBounds(20 + (int) ((FRAME_HEIGHT / 2) * 0.67) / 2 - 50 + (20 * x), FRAME_HEIGHT / 2 + 95,
							20, 20);
				star.setIcon(ImageUtils.getIcon("star_empty.png"));
				this.add(star);
			}
			this.repaint();
		}
	}
	
	Runnable OpenDownloads = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new DownloadFilm(guiInstance, film));
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
	
	Runnable UpdateStatus = new Runnable()
	{
		@Override
		public void run()
		{
			if (!scheda.loading)
			{
				loading.setVisible(false);
				
				cover.setVisible(true);
				cover.setIcon(ImageUtils.getIcon(
						ImageUtils.scaleImageOld(scheda.initImage(cover), cover.getWidth(), cover.getHeight())));
				text.setText("<html><h2>" + scheda.desc + "</html>");
				scroll.setVisible(true);
				info.setVisible(true);
				downloads.setVisible(true);
				drawRating();
			}
		}
	};
	
}
