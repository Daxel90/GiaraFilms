package it.giara.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class DefaultGui extends JPanel
{
	private static final long serialVersionUID = -1085038245651589565L;
	
	public int FRAME_WIDTH;
	public int FRAME_HEIGHT;
	public DefaultGui guiInstance;
	
	public JLabel backGround;
	public static BufferedImage backGroundImage = null;
	
	public DefaultGui()
	{
		setLayout(null);
		setOpaque(true);
		this.setBackground(ColorUtils.Back);
		guiInstance = this;
		
		
		
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
		if (backGround == null)
		{
			backGround = new JLabel();
		}
		
		if(backGroundImage == null)
		{
			backGroundImage = ImageUtils.getImage("background.jpg");
		}
		
		backGround.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		
		if (backGroundImage != null)
		{
			if (backGround.getWidth() > backGround.getHeight())
			{
				BufferedImage img = ImageUtils.scaleWithAspectHeight(backGroundImage, backGround.getHeight());
				if (img.getWidth() < backGround.getWidth())
					img = ImageUtils.scaleWithAspectWidth(backGroundImage, backGround.getWidth());
				backGround.setIcon(ImageUtils.getIcon(img));
			}
			else
			{
				BufferedImage img = ImageUtils.scaleWithAspectWidth(backGroundImage, backGround.getWidth());
				if (img.getHeight() < backGround.getHeight())
					img = ImageUtils.scaleWithAspectHeight(backGroundImage, backGround.getHeight());
				backGround.setIcon(ImageUtils.getIcon(img));
			}
		}
		this.add(backGround);
	}
	
}
