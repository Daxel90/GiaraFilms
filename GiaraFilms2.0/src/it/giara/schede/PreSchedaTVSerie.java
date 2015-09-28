package it.giara.schede;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import it.giara.gui.components.TvSerieButton;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;
import it.giara.utils.MD5;
import it.giara.utils.ThreadManager;

public class PreSchedaTVSerie
{
	public String Titolo;
	public String link;
	public String smallImage;
	public int anno;
	public String nazionalita;
	public String[] Generi;
	
	public BufferedImage img = null;
	
	public String getGeneri()
	{
		String result = "";
		if (Generi != null)
			for (String s : Generi)
			{
				result += s.toUpperCase() + ",";
			}
		if (result.endsWith(","))
			result = result.substring(0, result.length() - 1);
		return result;
	}
	
	public void setGeneri(String d)
	{
		Generi = d.split(",");
	}
	
	public BufferedImage initImage(final TvSerieButton filmButton)
	{
		if (img == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(smallImage) + ".png");
					
			if (f.exists())
			{
				img = ImageUtils.getImage(f);
			}
			else
			{
				Runnable task = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							img = ImageUtils.getHttpBufferedImage(smallImage);
							
							if (img != null)
							{
								img = ImageUtils.scaleImageOld(img, 140, 200);
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(img, "PNG", f);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								img = ImageUtils.getImage("notFound.png");
							}
						} finally
						{
							filmButton.updateImage(img);
						}
					}
				};
				
				ThreadManager.submitPoolTask(task);
				img = ImageUtils.getImage("loading.png");
			}
		}
		return img;
	}
	
}
