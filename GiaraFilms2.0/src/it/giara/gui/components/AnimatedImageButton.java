package it.giara.gui.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.Timer;

import it.giara.gui.utils.ImageUtils;

public class AnimatedImageButton extends JLabel implements MouseListener
{
	private static final long serialVersionUID = 4695042759651824794L;
	
	public Runnable act;
	public Runnable frameUpdate;
	private BufferedImage[] images = null;
	private int current = 0;
	private int frame = 0;
	public AnimatedImageButton instance;
	public Timer timer;
	
	public AnimatedImageButton(String imageName, int n, Runnable action, Runnable onUpdateFrame)
	{
		this(imageName, n, action);
		frameUpdate = onUpdateFrame;
	}
	
	public AnimatedImageButton(String imageName, int n, Runnable action)
	{
		super();
		instance = this;
		frame = n;
		act = action;
		images = new BufferedImage[n];
		for (int x = 0; x < n; x++)
		{
			images[x] = ImageUtils.getImage("gui/animation/" + imageName.replace("(n)", "" + x) + ".png");
		}
		this.addMouseListener(this);
		
		timer = new Timer(500, updateFrame);
		timer.start();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (act != null)
			act.run();
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{}
	
	@Override
	public void mouseExited(MouseEvent e)
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
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
		
		g.drawImage(images[current], 0, 0, getWidth(), getHeight(), null);
		
		g2d.setColor(old);
		
	}
	
	@Override
	public void setVisible(boolean vis)
	{
		if (!vis && timer != null)
		{
			timer.stop();
			timer = null;
		}
		else if (vis && timer == null)
		{
			timer = new Timer(500, updateFrame);
			timer.start();
		}
		super.setVisible(vis);
	}
	
	public void updateRunnable(Runnable r)
	{
		act = r;
	}
	
	
	ActionListener updateFrame = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			instance.current++;
			if (instance.current >= instance.frame)
			{
				instance.current = 0;
			}
			instance.repaint();
			
			if (frameUpdate != null)
				frameUpdate.run();
		}
	};
	
}