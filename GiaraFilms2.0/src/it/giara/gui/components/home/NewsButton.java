package it.giara.gui.components.home;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.MainFrame;
import it.giara.gui.section.FilmInfoSchede;
import it.giara.gui.section.TVSerieInfoSchede;
import it.giara.gui.utils.ImageUtils;
import it.giara.tmdb.schede.TMDBScheda;

public class NewsButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	public BufferedImage Image;
	public boolean isOver = false;
	TMDBScheda film;
	JLabel text = new JLabel();
	JLabel back = new JLabel();
	int Type = 0;
	
	public NewsButton(TMDBScheda f, int type) // type 1 w185 type 2 w500
	{
		super();
		film = f;
		Type = type;
		this.setOpaque(false);
		this.setLayout(null);
		this.addMouseListener(this);
		
		if (Type == 1)
			back.setIcon(ImageUtils.getIcon(film.initHome_w185(this)));
		else if (Type == 2)
			back.setIcon(ImageUtils.getIcon(film.initHome_w500(this)));
			
		String Stext = "<html><h2>" + film.title + "</html>";
		
		if (film.title.length() > 8)
			Stext = "<html><h4>" + film.title + "</html>";
			
		if (film.title.length() > 16)
			Stext = "<html><h4>" + film.title + "</html>";
			
		if (film.title.length() > 24)
		{
			String[] p = film.title.split(" ");
			String newTitle = "";
			for (int j = 0; j < p.length; j++)
			{
				if (j == (p.length / 2) + 1)
					newTitle += "<br>";
				newTitle += p[j] + " ";
			}
			newTitle = newTitle.trim();
			Stext = "<html><h4>" + newTitle + "</html>";
		}
		
		text = new JLabel(Stext);
		text.setBounds(0, 0, this.getWidth(), this.getHeight());
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setOpaque(false);
		text.setVisible(false);
		this.add(text);
		
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(back);
	}
	
	public void init()
	{
		this.removeAll();
		
		text.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(text);
		
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(back);
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (film.type == MainType.Film)
			MainFrame.getInstance().setInternalPane(new FilmInfoSchede(MainFrame.getInstance().internalPane, film));
		else if (film.type == MainType.SerieTV)
			MainFrame.getInstance().setInternalPane(new TVSerieInfoSchede(MainFrame.getInstance().internalPane, film));
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		isOver = true;
		text.setVisible(true);
		
		if (Type == 1)
			back.setIcon(ImageUtils.getIcon(ImageUtils.FilterRescaleOp(film.initHome_w185(this), 0.4f)));
		else if (Type == 2)
			back.setIcon(ImageUtils.getIcon(ImageUtils.FilterRescaleOp(film.initHome_w500(this), 0.4f)));
		repaint();
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		isOver = false;
		
		text.setVisible(false);
		
		if (Type == 1)
			back.setIcon(ImageUtils.getIcon(film.initHome_w185(this)));
		else if (Type == 2)
			back.setIcon(ImageUtils.getIcon(film.initHome_w500(this)));
		
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		repaint();
	}
	
	public void updateImage(BufferedImage i1)
	{
		back.setIcon(ImageUtils.getIcon(i1));
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
}