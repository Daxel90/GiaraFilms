package it.giara.gui.section;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
		cover = new JLabel();
		cover.setBounds(50, 80, FRAME_WIDTH / 4, (int) ((FRAME_WIDTH / 4) * 1.49));
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
		scroll.setBounds(FRAME_WIDTH / 4 + 60, 100, FRAME_WIDTH - (FRAME_WIDTH / 4 + 80),
				(int) ((FRAME_WIDTH / 4) * 1.49) - 20);
		scroll.setVisible(!scheda.loading);
		scroll.setOpaque(false);
		this.add(scroll);
	}
	
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
			}
		}
	};
	
}
