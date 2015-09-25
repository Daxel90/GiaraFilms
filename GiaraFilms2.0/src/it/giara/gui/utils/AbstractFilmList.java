package it.giara.gui.utils;

import java.util.ArrayList;

import javax.swing.JPanel;

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
		films.add(p);
		notifyChange();
	}
	
	public void addPreSchedaTVSerie(PreSchedaTVSerie p)
	{
		series.add(p);
		notifyChange();
	}
	
	public void addv(String p)
	{
		unknowFile.add(p);
		notifyChange();
	}
	
	private void notifyChange()
	{
		panel.repaint();
	}
	
}
