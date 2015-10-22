package it.giara.gui.section;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadList;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.schede.PreSchedaFilm;
import it.giara.sql.SQLQuery;

public class DownloadFilm extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	PreSchedaFilm scheda;
	ArrayList<String[]> lista = new ArrayList<String[]>();
	DownloadList panel;
	
	public DownloadFilm(DefaultGui gui, PreSchedaFilm s)
	{
		super();
		back = gui;
		scheda = s;
		for (int x : SQLQuery.readFileInfoList(scheda.IdDb, 1))
		{
			lista.add(SQLQuery.getFileName(x));
		}
		panel = new DownloadList(lista,this);
		
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		
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
	
}