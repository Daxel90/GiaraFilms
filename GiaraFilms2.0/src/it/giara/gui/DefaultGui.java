package it.giara.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class DefaultGui extends JPanel
{
	private static final long serialVersionUID = -1085038245651589565L;
	
	public int FRAME_WIDTH;
	public int FRAME_HEIGHT;
	
	public DefaultGui()
	{
		setLayout(null);
		setOpaque(false);
	}
	
	public void init(int width, int height)
	{
		FRAME_WIDTH = width;
		FRAME_HEIGHT = height - 30;
		setBounds(0, 0, FRAME_WIDTH / 2, FRAME_HEIGHT);
		setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.removeAll();
		loadComponent();
	}
	
	public void loadComponent()
	{
	
	}
	
}
