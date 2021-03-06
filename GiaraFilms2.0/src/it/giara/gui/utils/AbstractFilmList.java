package it.giara.gui.utils;

import java.util.ArrayList;

import javax.swing.JPanel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.FilmListPanel;
import it.giara.gui.components.home.HomeListPanel;
import it.giara.tmdb.TMDBScheda;

public class AbstractFilmList
{
	public boolean loading = false;
	public ArrayList<TMDBScheda> films = new ArrayList<TMDBScheda>();
	public ArrayList<TMDBScheda> series = new ArrayList<TMDBScheda>();
	public ArrayList<String[]> allFile = new ArrayList<String[]>();
	private JPanel panel;
	
	public void setJPanel(JPanel pane)
	{
		panel = pane;
	}
	
	public void addScheda(TMDBScheda p)
	{
		if (p == null)
			return;
		if (p.title == null)
			return;
		if (p.title.equals(""))
			return;
		if (p.type == MainType.SerieTV)
		{
			for (int k = 0; k < series.size(); k++)
			{
				if (p.title.equals(series.get(k).title))
					return;
			}
			
			series.add(p);
			notifyChange(MainType.SerieTV);
		}
		else if (p.type == MainType.Film)
		{
			for (int k = 0; k < films.size(); k++)
			{
				if (p.title.equals(films.get(k).title))
					return;
			}
			
			films.add(p);
			notifyChange(MainType.Film);
		}
	}
	
	public void addFile(String[] p)
	{
		if (p == null)
			return;
		if (p.equals(""))
			return;
			
		if (contains(p[0], allFile))
			return;
			
		allFile.add(p);
		notifyChange(MainType.NULL);
	}
	
	private void notifyChange(MainType type)
	{
		if (panel instanceof FilmListPanel)
			((FilmListPanel) panel).updateFromList(type);
		else if (panel instanceof HomeListPanel)
			((HomeListPanel) panel).updateFromList(type);
		else
			panel.repaint();
	}
	
	public int sizeofList(MainType t)
	{
		switch(t)
		{
			case Film:
				return films.size();
			case SerieTV:
				return series.size();
			case NULL:
				return allFile.size();
			default:
			return 0;
		}
	}
	
	private boolean contains(String data, ArrayList<String[]> list)
	{
		for (String[] s : list)
		{
			if (s[0].equals(data))
				return true;
		}
		return false;
	}
	
	// remove a lot of memory
	public void clear()
	{
		loading = false;
		films.clear();
		series.clear();
		allFile.clear();
		System.gc();
	}
	
}
