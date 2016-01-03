package it.giara.gui.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadList;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.schede.TMDBScheda;

public class DownloadTVSerie extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	TMDBScheda scheda;
	HashMap<Integer, TreeMap<Integer, ArrayList<String[]>>> lista = new HashMap<Integer, TreeMap<Integer, ArrayList<String[]>>>();
	DownloadList panel;
	
	public DownloadTVSerie(DefaultGui gui, TMDBScheda scheda2)
	{
		super();
		back = gui;
		scheda = scheda2;
		
		for (Entry<Integer, HashMap<Integer, ArrayList<Integer>>> serie : SQLQuery.readEpisodeInfoList(scheda.ID)
				.entrySet())
		{
			int serieN = serie.getKey();
			for (Entry<Integer, ArrayList<Integer>> episode : serie.getValue().entrySet())
			{
				int episodeN = episode.getKey();
				for (int file : episode.getValue())
				{
					if (!lista.containsKey(serieN))
						lista.put(serieN, new TreeMap<Integer, ArrayList<String[]>>());
					if (!lista.get(serieN).containsKey(episodeN))
						lista.get(serieN).put(episodeN, new ArrayList<String[]>());
					lista.get(serieN).get(episodeN).add(SQLQuery.getFileNameAndSize(file));
				}
			}
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
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		homePage.setToolTipText("Home");
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