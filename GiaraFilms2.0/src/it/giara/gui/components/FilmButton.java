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
import javax.swing.border.EtchedBorder;

import it.giara.gui.MainFrame;
import it.giara.gui.section.FilmInfoSchede;
import it.giara.schede.PreSchedaFilm;

public class FilmButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 4695042759651824794L;
	public BufferedImage Cover;
	public boolean isOver = false;
	PreSchedaFilm film;
	JLabel text = new JLabel();
	
	public FilmButton(PreSchedaFilm f, int x, int y)
	{
		super();
		film = f;
		if (film == null)
			return;
		if (film.img == null)
			Cover = film.initImage(this);
		else
			Cover = film.img;
			
		this.addMouseListener(this);
		this.setOpaque(false);
		this.setLayout(null);
		this.setBounds(x, y, 140, 240);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		String Stext = "<html><h3>" + film.Titolo + "</html>";
		
		if (film.Titolo.length() > 8)
			Stext = "<html><h4>" + film.Titolo + "</html>";
			
		if (film.Titolo.length() > 16)
			Stext = "<html><h5>" + film.Titolo + "</html>";
			
		if (film.Titolo.length() > 24)
		{
			String[] p = film.Titolo.split(" ");
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
		MainFrame.getInstance().setInternalPane(new FilmInfoSchede(MainFrame.getInstance().internalPane, film));
		// MainFrame.instance.setInternalPane(new FilmInfoPanel(film,
		// MainFrame.instance.internalPane));
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