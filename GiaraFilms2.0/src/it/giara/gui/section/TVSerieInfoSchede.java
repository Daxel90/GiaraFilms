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
import it.giara.http.HTTPTVSerieInfo;
import it.giara.schede.PreSchedaTVSerie;
import it.giara.schede.SchedaTVSerie;
import it.giara.utils.ThreadManager;

public class TVSerieInfoSchede extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	private DefaultGui back;
	private PreSchedaTVSerie serie;
	private SchedaTVSerie scheda;
	private AnimatedImageButton loading;
	
	JEditorPane text;
	JLabel cover;
	JScrollPane scroll;
	JLabel info;
	
	public TVSerieInfoSchede(DefaultGui gui, PreSchedaTVSerie f)
	{
		super();
		back = gui;
		serie = f;
		final HTTPTVSerieInfo getInfo = new HTTPTVSerieInfo(serie.link);
		scheda = getInfo.serie;
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
		title.setText("<html><h1>" + serie.Titolo + "</html>");
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
		info.setText("<html><h4> " + "Anno: " + serie.anno
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Paese: " + serie.nazionalita
				+ "<br>" + "Genere: " + serie.getGeneri().replace(",", ", ") + "</html>");
		info.setVisible(!scheda.loading);
		this.add(info);
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
				info.setVisible(true);
			}
		}
	};
	
}
