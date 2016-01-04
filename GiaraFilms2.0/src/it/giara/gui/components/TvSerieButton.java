package it.giara.gui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import it.giara.gui.MainFrame;
import it.giara.gui.section.TVSerieInfoSchede;
import it.giara.tmdb.schede.TMDBScheda;

public class TvSerieButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 4695042759651824794L;
	public BufferedImage Cover;
	public boolean isOver = false;
	TMDBScheda film;
	JLabel text = new JLabel();
	
	public TvSerieButton(TMDBScheda f, int x, int y, JPanel panel)
	{
		super();
		film = f;
		if (film == null)
			return;
		if (film.poster_w140 == null)
			Cover = film.initPoster_w140(panel);
		else
			Cover = film.poster_w140;
			
		this.addMouseListener(this);
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(x, y, 140, 240);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		String Stext = "<html><h3>" + film.title + "</html>";
		
		if (film.title.length() > 8)
			Stext = "<html><h4>" + film.title + "</html>";
			
		if (film.title.length() > 16)
			Stext = "<html><h5>" + film.title + "</html>";
			
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
			Stext = "<html><h5>" + newTitle + "</html>";
		}
		
		text = new JLabel(Stext);
		text.setBounds(0, 200, this.getWidth(), 40);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setOpaque(false);
		
		this.add(text);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
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
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		Color old = g2d.getColor();
		g2d.setComposite(AlphaComposite.getInstance(3, 1f));
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(Cover, 0, 0, getWidth(), getHeight() - 40, null);
		
		g2d.setColor(old);
		
		super.paint(g);
	}
	
	public void updateImage(BufferedImage i1)
	{
		Cover = i1;
		if (this.isVisible())
			repaint();
	}
	
}