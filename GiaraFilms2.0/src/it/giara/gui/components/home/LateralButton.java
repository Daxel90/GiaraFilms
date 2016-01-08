package it.giara.gui.components.home;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import it.giara.gui.utils.ColorUtils;

public class LateralButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 4695042759651824794L;
	public Runnable act;
	public boolean isOver = false;
	public boolean selected = false;
	
	public LateralButton(Runnable action)
	{
		super();
		act = action;
		this.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (act != null)
			act.run();
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
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (isOver || selected)
		{
			g2d.setPaint(ColorUtils.AlphaWhite);
			g2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		}
		
		g2d.setColor(ColorUtils.Separator);
		g2d.drawLine(0, 0, getWidth(),0);
		g2d.drawLine(0,getHeight()-1, getWidth(), getHeight()-1);
		
		g2d.setColor(old);
		//
		super.paint(g);
		
	}
	
	public void updateRunnable(Runnable r)
	{
		act = r;
	}
	
	public void setSelected(boolean b)
	{
		selected = b;
	}
	
	public boolean getSelected()
	{
		return selected;
	}
}