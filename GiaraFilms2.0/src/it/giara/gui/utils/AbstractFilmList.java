package it.giara.gui.utils;

import java.util.ArrayList;

import javax.swing.JPanel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.FilmListPanel;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;

public class AbstractFilmList
{
	public boolean loading = false;
	public ArrayList<PreSchedaFilm> films = new ArrayList<PreSchedaFilm>();
	public ArrayList<PreSchedaTVSerie> series = new ArrayList<PreSchedaTVSerie>();
	public ArrayList<String[]> allFile = new ArrayList<String[]>();
	private JPanel panel;
	
	public void setJPanel(JPanel pane)
	{
		panel = pane;
	}
	
	public void addPreSchedaFilm(PreSchedaFilm p)
	{
		if (p == null)
			return;
		if (p.Titolo == null)
			return;
		if (p.Titolo.equals(""))
			return;
		for (int k = 0; k < films.size(); k++)
		{
			if (p.Titolo.equals(films.get(k).Titolo))
				return;
		}
		
		films.add(p);
		notifyChange(MainType.Film);
	}
	
	public void addPreSchedaTVSerie(PreSchedaTVSerie p)
	{
		if (p == null)
			return;
		if (p.Titolo == null)
			return;
		if (p.Titolo.equals(""))
			return;
		for (int k = 0; k < series.size(); k++)
		{
			if (p.Titolo.equals(series.get(k).Titolo))
				return;
		}
		
		series.add(p);
		notifyChange(MainType.SerieTV);
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
		else
			panel.repaint();
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
