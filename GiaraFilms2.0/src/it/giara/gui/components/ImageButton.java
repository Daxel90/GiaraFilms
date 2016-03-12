package it.giara.gui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class ImageButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 4695042759651824794L;
	public BufferedImage normal;
	public BufferedImage over;
	public BufferedImage clicked;
	public Runnable act;
	public boolean isOver = false;
	
	private BufferedImage current = null;
	private Component comp;
	
	public ImageButton(BufferedImage i1, BufferedImage i2, BufferedImage bufferedImage, Runnable action,
			Component component)
	{
		this(i1, i1, i1, action);
		comp = component;
	}
	
	public ImageButton(BufferedImage i1, Runnable action)
	{
		this(i1, i1, i1, action);
	}
	
	public ImageButton(BufferedImage i1, BufferedImage i2, BufferedImage bufferedImage, Runnable action)
	{
		super();
		normal = i1;
		over = i2;
		clicked = bufferedImage;
		act = action;
		this.addMouseListener(this);
		current = normal;
		
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
		if (over != null)
			current = over;
			
		if (comp != null)
			comp.repaint();
		else
			repaint();
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		isOver = false;
		if (normal != null)
			current = normal;
		if (comp != null)
			comp.repaint();
		else
			repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (clicked != null)
			current = clicked;
		if (comp != null)
			comp.repaint();
		else
			repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (comp != null)
			comp.repaint();
		else
			repaint();
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		Color old = g2d.getColor();
		g2d.setComposite(AlphaComposite.getInstance(3, 1f));
		
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(current, 0, 0, getWidth(), getHeight(), null);
		
		g2d.setColor(old);
		
	}
	
	public void updateRunnable(Runnable r)
	{
		act = r;
	}
	
	public void updateImage(BufferedImage i1)
	{
		normal = i1;
		if (comp != null)
			comp.repaint();
		else
			repaint();
	}
	
}