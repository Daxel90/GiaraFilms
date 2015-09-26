package it.giara.gui.utils;

import java.util.ArrayList;

import javax.swing.JPanel;

import it.giara.gui.components.FilmListPanel;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;

public class AbstractFilmList
{
	public boolean loading = false;
	public ArrayList<PreSchedaFilm> films = new ArrayList<PreSchedaFilm>();
	public ArrayList<PreSchedaTVSerie> series = new ArrayList<PreSchedaTVSerie>();
	public ArrayList<String> unknowFile = new ArrayList<String>();
	private JPanel panel;
	
	public void setJPanel(JPanel pane)
	{
		panel = pane;
	}
	
	public void addPreSchedaFilm(PreSchedaFilm p)
	{
		if (p == null)
			return;
		for (int k = 0; k < films.size(); k++)
		{
			if (p.Titolo.equals(films.get(k).Titolo))
				return;
		}
		
		films.add(p);
		notifyChange();
	}
	
	public void addPreSchedaTVSerie(PreSchedaTVSerie p)
	{
		if (p == null)
			return;
		for (int k = 0; k < series.size(); k++)
		{
			if (p.Titolo.equals(series.get(k).Titolo))
				return;
		}
		
		series.add(p);
		notifyChange();
	}
	
	public void addUnknowFile(String p)
	{
		if (p == null)
			return;
			
		if (unknowFile.contains(p))
			return;
			
		unknowFile.add(p);
		notifyChange();
	}
	
	private void notifyChange()
	{
		if (panel instanceof FilmListPanel)
			((FilmListPanel) panel).updateFromList();
		else
			panel.repaint();
	}
	
	//remove a lot of memory
	public void clear()
	{
		loading = false;
		films.clear();
		series.clear();
		unknowFile.clear();
		System.gc(); 
	}
	
}
