package it.giara.tmdb.schede;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import org.json.JSONObject;

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
	public BufferedImage back_w1920 = null;
	
	public BufferedImage initPoster_w140(final FilmListPanel filmListPanel)
	{
		final String link = "http://image.tmdb.org/t/p/w148";
		if (poster_w140 == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link + poster) + ".jpg");
					
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
							poster_w140 = ImageUtils.getHttpBufferedImage(link + poster);
							
							if (poster_w140 != null)
							{
								poster_w140 = ImageUtils.scaleImage(poster_w140, 140, 200);
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
					MD5.generatemd5(link + poster) + ".jpg");
					
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
							poster_original = ImageUtils.getHttpBufferedImage(link + poster);
							
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
							label.setIcon(ImageUtils.getIcon(
									ImageUtils.scaleImage(poster_original, label.getWidth(), label.getHeight())));
						}
					}
				};
				
				ThreadManager.submitPoolTask(task);
				poster_original = ImageUtils.getImage("loading.png");
			}
		}
		return poster_original;
	}
	
	public BufferedImage initBack_w1920(final JLabel label)
	{
		final String link = "http://image.tmdb.org/t/p/w1920";
		if (back_w1920 == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link + poster) + ".jpg");
			final RescaleOp op = new RescaleOp(.4f, 0, null);
			
			if (f.exists())
			{
				back_w1920 = ImageUtils.getImage(f);
				back_w1920 = op.filter(back_w1920,null);
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
							back_w1920 = ImageUtils.getHttpBufferedImage(link + back);
							if (back_w1920 != null)
							{
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(back_w1920, "JPG", f);
									back_w1920 = op.filter(back_w1920,null);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								back_w1920 = null;
							}
						} finally
						{
							if (label.getWidth() > label.getHeight())
							{
								BufferedImage img = ImageUtils.scaleWithAspectHeight(back_w1920, label.getHeight());
								if (img.getWidth() < label.getWidth())
									img = ImageUtils.scaleWithAspectWidth(back_w1920, label.getWidth());
								label.setIcon(ImageUtils.getIcon(img));
							}
							else
							{
								BufferedImage img = ImageUtils.scaleWithAspectWidth(back_w1920, label.getWidth());
								if (img.getHeight() < label.getHeight())
									img = ImageUtils.scaleWithAspectHeight(back_w1920, label.getHeight());
								label.setIcon(ImageUtils.getIcon(img));
							}
						}
					}
				};
				
				ThreadManager.submitPoolTask(task);
				back_w1920 = null;
			}
		}
		return back_w1920;
	}
	
	public String getGeneriIDs()
	{
		String res = "";
		boolean first = true;
		if(genre_ids != null)
		for (int s : genre_ids)
		{
			if (!first)
				res += ", ";
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
		for (int x = 0; x < genre_ids.length; x++)
		{
			genre_ids[x] = Integer.parseInt(Generi[x]);
		}
	}
	
	public String getGeneri()
	{
		String res = "";
		boolean first = true;
		for (int s : genre_ids)
		{
			if (!first)
				res += ", ";
			else
				first = false;
			res += GenereType.getGenereByID(s);
		}
		return res;
	}
	
	public JSONObject getJson()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("id", ID);
			json.put("title", title);
			json.put("relese", relese);
			json.put("poster", poster);
			json.put("back", back);
			json.put("desc", desc);
			json.put("genre_ids", getGeneriIDs());
			json.put("vote", vote);
			json.put("type", type.ID);
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		return json;
		
	}
	
	public void setJson(JSONObject json)
	{
		try
		{
			ID = json.getInt("id");
			title = json.getString("title");
			relese = json.getString("relese");
			poster = json.getString("poster");
			back = json.getString("back");
			desc = json.getString("desc");
			this.setGeneriIDs(json.getString("genre_ids"));
			vote = json.getDouble("vote");
			type = MainType.getMainTypeByID(json.getInt("type"));
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		
	}
	
	@Override
	public String toString()
	{
		String result = "ID: " + ID + " title: " + title + " relese: " + relese + " poster: " + poster + " back: "
				+ back + " desc: " + desc + " vote: " + vote + " genre_ids: ";
				
		for (int a : genre_ids)
		{
			result += a + ",";
		}
		return result;
	}
}
