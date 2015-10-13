package it.giara;

import javax.swing.UIManager;

import it.giara.gui.MainFrame;
import it.giara.gui.utils.FontUtils;
import it.giara.phases.UpdateProgram;
import it.giara.utils.Log;

public class MainClass
{
	
	public static void main(String[] args)
	{
		UpdateProgram.checkUpdateSuccess();
		try
		{
			UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel");
			FontUtils.initUIFont();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Log.log(Log.INFO, "GiaraFilms 2.0");
		
		new MainFrame();
		
	}
	
}
