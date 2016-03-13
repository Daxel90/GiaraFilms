package it.giara.gui.components.home;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.MainFrame;
import it.giara.gui.section.FilmInfoSchede;
import it.giara.gui.section.TVSerieInfoSchede;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.tmdb.schede.TMDBScheda;

public class NewsButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	public BufferedImage Image, Star;
	public boolean isOver = false;
	TMDBScheda film;
	JLabel text = new JLabel();
	JLabel back = new JLabel();
	JLabel vote = new JLabel();
	int Type = 0;
	
	public NewsButton(TMDBScheda f, int type) // type 1 w185 type 2 w500
	{
		super();
		film = f;
		Type = type;
		this.setOpaque(false);
		this.setLayout(null);
		this.addMouseListener(this);
		Star = ImageUtils.getImage("star_full_black.png");
		if (Type == 1)
			back.setIcon(ImageUtils.getIcon(film.initHome_w185(this)));
		else if (Type == 2)
			back.setIcon(ImageUtils.getIcon(film.initHome_w500(this)));
			
		String Stext = "<html><h2>" + film.title;
		
		if (film.title.length() > 8)
			Stext = "<html><h4>" + film.title;
			
		if (film.title.length() > 16)
			Stext = "<html><h4>" + film.title;
			
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
			Stext = "<html><h4>" + newTitle;
		}
		
		String[] year = f.relese.split("-");
		if (year.length == 3)
			Stext += "<br> <h3> <center>" + year[0]+"</center>";
		Stext += "</html>";
		
		text = new JLabel(Stext);
		text.setBounds(0, 0, this.getWidth(), this.getHeight() / 2);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setVerticalAlignment(JLabel.CENTER);
		text.setForeground(ColorUtils.DarkText);
		
		vote = new JLabel("<html><h3>"+film.vote+"/10"+"</html>");
		vote.setHorizontalAlignment(JLabel.CENTER);
		vote.setForeground(ColorUtils.DarkText);
		vote.setBounds(0, 0, this.getWidth(), 30);
		
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(back);
	}
	
	public void init()
	{
		this.removeAll();
		
		text.setBounds(0, 0, this.getWidth(), this.getHeight() / 2);
		vote.setBounds(0, 0, this.getWidth(), 30);
		
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
		repaint();
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		isOver = false;
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
	public void paint(Graphics g)
	{
		super.paint(g);
		
		if (isOver)
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setPaint(ColorUtils.AlphaWhite);
			
			g2.fill(new RoundRectangle2D.Float(0, 0, this.getWidth(), this.getHeight(), 6, 6));
			text.paint(g);
			
			g.drawImage(Star, getWidth()/2-10, getHeight()-60, 20, 20, null);
			g.translate(0, getHeight()-40);
			vote.paint(g);
			g.translate(0, -(getHeight()-40));
			
		}
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
}