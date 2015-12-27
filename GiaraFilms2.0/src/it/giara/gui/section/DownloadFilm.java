package it.giara.gui.section;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadList;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.schede.TMDBScheda;

public class DownloadFilm extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	TMDBScheda scheda;
	ArrayList<String[]> lista = new ArrayList<String[]>();
	DownloadList panel;
	
	public DownloadFilm(DefaultGui gui, TMDBScheda s)
	{
		super();
		back = gui;
		scheda = s;
		for (int x : SQLQuery.readFileListBySchedeId(scheda.ID, MainType.Film))
		{
			lista.add(SQLQuery.getFileNameAndSize(x));
		}
		panel = new DownloadList(lista, this);
		
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		backbt.setBounds(5, 5, 32, 32);
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		this.add(homePage);
		
		panel.setBounds(20 - 8, 80, FRAME_WIDTH - 40, FRAME_HEIGHT - 100);
		panel.init();
		
		this.add(panel);
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
			Search.searchService.StopService();
			MainFrame.getInstance().setInternalPane(new HomePage());
		}
	};
	
}