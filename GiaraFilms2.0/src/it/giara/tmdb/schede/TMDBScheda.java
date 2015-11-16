package it.giara.tmdb.schede;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.FilmListPanel;
import it.giara.gui.utils.ImageUtils;
import it.giara.tmdb.GenereType;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;
import it.giara.utils.MD5;
import it.giara.utils.ThreadManager;

public class TMDBScheda
{
	public int ID;
	public String title;
	public String relese;
	public String poster;
	public String back;
	public String desc;
	public int[] genre_ids;
	public double vote;
	public MainType type = MainType.NULL;
	
	public BufferedImage poster_w140 = null;
	public BufferedImage poster_original = null;
	
	public BufferedImage initPoster_w140(final FilmListPanel filmListPanel)
	{
		final String link = "http://image.tmdb.org/t/p/w148";
		if (poster_w140 == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link+poster) + ".jpg");
					
			if (f.exists())
			{
				poster_w140 = ImageUtils.getImage(f);
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
							poster_w140 = ImageUtils.getHttpBufferedImage(link+poster);
							
							if (poster_w140 != null)
							{
								poster_w140 = ImageUtils.scaleImageOld(poster_w140, 140, 200);
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(poster_w140, "JPG", f);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								poster_w140 = ImageUtils.getImage("notFound.png");
							}
						} finally
						{
							filmListPanel.init();
							filmListPanel.repaint();
						}
					}
				};
				
				ThreadManager.submitPoolTask(task);
				poster_w140 = ImageUtils.getImage("loading.png");
			}
		}
		return poster_w140;
	}
	
	public BufferedImage initPoster_original(final JLabel label)
	{
		final String link = "http://image.tmdb.org/t/p/original";
		if (poster_original == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link+poster) + ".jpg");
					
			if (f.exists())
			{
				poster_original = ImageUtils.getImage(f);
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
							poster_original = ImageUtils.getHttpBufferedImage(link+poster);
							
							if (poster_original != null)
							{
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(poster_original, "JPG", f);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								poster_original = ImageUtils.getImage("notFound.png");
							}
						} finally
						{
							label.setIcon(ImageUtils.getIcon(ImageUtils.scaleImageOld(poster_original, label.getWidth(), label.getHeight())));
						}
					}
				};
				
				ThreadManager.submitPoolTask(task);
				poster_original = ImageUtils.getImage("loading.png");
			}
		}
		return poster_original;
	}
	
	public String getGeneriIDs()
	{
		String res = "";
		boolean first = true;
		for(int s : genre_ids)
		{
			if(!first)
				res +=", ";
			else
				first = false;
			res += s;
		}
		return res;
	}
	
	public void setGeneriIDs(String d)
	{
		String[] Generi = d.split(", ");
		genre_ids = new int[Generi.length];
		for(int x = 0; x< genre_ids.length;x++)
		{
			genre_ids[x] = Integer.parseInt(Generi[x]);
		}
	}
	
	public String getGeneri()
	{
		String res = "";
		boolean first = true;
		for(int s : genre_ids)
		{
			if(!first)
				res +=", ";
			else
				first = false;
			res += GenereType.getGenereByID(s);
		}
		return res;
	}
	
	
	@Override
	public String toString()
	{
		String result = "ID: " + ID + " title: " + title + " relese: " + relese + " poster: " + poster + " back: "
				+ back +" desc: "+desc+ " vote: "+vote+" genre_ids: ";
				
		for(int a : genre_ids)
		{
			result+= a+",";
		}
		return result;
	}
}
