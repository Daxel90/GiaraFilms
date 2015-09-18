package it.giara.gui.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class FontUtils 
{
	public static FontUIResource defaultFont;
	
	public static void initUIFont()
	{
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) 
	    {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof FontUIResource)
	        UIManager.put (key, getFont());
	    }
	} 
	
	
	public static Font getFrameFont(int size, int type)
	{
		Font font;
		try
		{
			font = ImageUtils.getFrameFont("Kelvetica_Nobis", size);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (final Exception e)
		{
			e.printStackTrace();
			font = new Font("Arial", type, 15);
		}
		return font;
	}
	
	public static FontUIResource getFont()
	{
		if(defaultFont == null)
		{
			defaultFont = new FontUIResource(getFrameFont(15, Font.PLAIN));
		}
		return defaultFont;
	}
	
	
}
