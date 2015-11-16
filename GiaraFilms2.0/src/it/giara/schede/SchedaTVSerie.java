package it.giara.schede;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import it.giara.gui.utils.ImageUtils;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;
import it.giara.utils.MD5;
import it.giara.utils.ThreadManager;
@Deprecated
public class SchedaTVSerie
{
	public String desc = "";
	public String BigImage = "";
	
	public boolean loading = true;
	
	public BufferedImage img = null;
	
	public BufferedImage initImage(final JLabel label)
	{
		if (img == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(BigImage) + ".png");
					
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
							img = ImageUtils.getHttpBufferedImage(BigImage);
							
							if (img != null)
							{
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
							label.setIcon(ImageUtils.getIcon(ImageUtils.scaleImageOld(img, label.getWidth(), label.getHeight())));
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
