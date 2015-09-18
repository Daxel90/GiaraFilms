package it.giara.phases;

import it.giara.MainClass;
import it.giara.gui.section.LoadScreen;
import it.giara.utils.HttpPost;
import it.giara.utils.MD5;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class UpdateProgram
{
	
	public static void checkUpdate(LoadScreen screen)
	{
		try
		{
			CodeSource codeSource = MainClass.class.getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			if (jarFile.getAbsolutePath().endsWith(".jar"))
			{
				String md5 = MD5.getMD5Checksum(jarFile);
				if (HttpPost.post("http://giaratest.altervista.org/giarafilms/MD5Update.php", "md5", md5)
						.equals("update"))
				{
					startUpdate(screen);
				}
			}
			
		} catch (URISyntaxException e)
		{}
		
	}
	
	public static void startUpdate(LoadScreen screen)
	{
		// TODO
	}
	
}
