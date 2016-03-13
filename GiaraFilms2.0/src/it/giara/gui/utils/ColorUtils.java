package it.giara.gui.utils;

import java.awt.Color;

public class ColorUtils
{
	public static final Color Trasparent = new Color(0, 0, 0,0);
	public static final Color Back = new Color(30, 30, 30);
	public static final Color DarkText = new Color(5, 5, 5);
	public static Color Separator = new Color(128, 128, 128, 120);
	public static Color AlphaWhite = new Color(255, 255, 255, 180);
	
	public static Color getColorQuality(float f)
	{
		if(f<= 5)
			return new Color(255,0,0);
		if(f<= 5.5)
			return new Color(255,128,0);
		if(f<= 6)
			return new Color(255,153,51);
		if(f<= 7)
			return new Color(255,255,0);
		if(f<= 8)
			return new Color(153,255,51);
		if(f<= 10)
			return new Color(0,204,0);
		
		return new Color(0,0,0,0);
	}
}
