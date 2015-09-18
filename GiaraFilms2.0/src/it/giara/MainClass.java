package it.giara;

import it.giara.gui.MainFrame;
import it.giara.gui.utils.FontUtils;
import it.giara.utils.Log;

import java.util.ArrayList;

import javax.swing.UIManager;

public class MainClass 
{
	public static ArrayList<String> sorgenti = new ArrayList<String>();
	
	
	public static void main(String[] args)
	{
		try {
		    UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel");
		    FontUtils.initUIFont();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		Log.log(Log.INFO, "GiaraFilms 2.0");
		
		new MainFrame();
		
	}
	
	
	
}
