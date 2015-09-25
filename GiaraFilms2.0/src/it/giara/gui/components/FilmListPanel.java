package it.giara.gui.components;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.sql.SQLQuery;

public class FilmListPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private AbstractFilmList list;

	public FilmListPanel(AbstractFilmList l)
	{
		setLayout(null);
		setOpaque(false);
		list =l;
		list.setJPanel(this);
		init();
	}
	
	public void init()
	{
		this.removeAll();
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, this.getWidth(), 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		FilmButton f = new FilmButton(SQLQuery.readPreSchedaFilm(46), 100, 100);
		this.add(f);
		
		FilmButton f2 = new FilmButton(SQLQuery.readPreSchedaFilm(300), 300, 100);
		this.add(f2);
		
	}
	
	@Override
	public void setBounds(int a,int b,int c,int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
}
