package it.giara.tmdb;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.FilmListPanel;
import it.giara.gui.components.home.HomeListPanel;
import it.giara.gui.components.home.NewsButton;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;
import it.giara.utils.MD5;
import it.giara.utils.ThreadManager;

public class TMDBScheda
{
	public int ID;
	public String title;
	public String release;
	public String poster;
	public String back;
	public String desc;
	public int[] genre_ids;
	public double vote;
	public MainType type = MainType.NULL;
	public int fallback_desc = 0;
	
	public BufferedImage poster_w140 = null;
	public BufferedImage poster_original = null;
	public BufferedImage back_w1920 = null;
	
	public BufferedImage home_w185 = null;
	public BufferedImage home_w500 = null;
	
	public BufferedImage initPoster_w140(final JPanel filmListPanel)
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
							if(filmListPanel instanceof FilmListPanel)
							{
								((FilmListPanel)filmListPanel).init();
							}
							else if(filmListPanel instanceof HomeListPanel)
							{
								((HomeListPanel)filmListPanel).init();
							}
							filmListPanel.repaint();
							
						}
					}
				};
				
				ThreadManager.submitSystemTask(task);
				poster_w140 = ImageUtils.getImage("loading.png");
			}
		}
		return poster_w140;
	}
	
	public BufferedImage initPoster_w500(final JLabel label)
	{
		final String link = "http://image.tmdb.org/t/p/w500";
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
				
				ThreadManager.submitSystemTask(task);
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
					MD5.generatemd5(link + back) + ".jpg");
					
			if (f.exists())
			{
				back_w1920 = ImageUtils.getImage(f);
				back_w1920 = ImageUtils.FilterRescaleOp(back_w1920, 0.4f);
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
									back_w1920 = ImageUtils.FilterRescaleOp(back_w1920, 0.4f);
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
				
				ThreadManager.submitSystemTask(task);
				back_w1920 = null;
			}
		}
		return back_w1920;
	}
	
	public BufferedImage initHome_w185(final NewsButton news)
	{
		final String link = "http://image.tmdb.org/t/p/w185";
		if (home_w185 == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link + poster) + ".jpg");
					
			if (f.exists())
			{
				home_w185 = ImageUtils.getImage(f);
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
							home_w185 = ImageUtils.getHttpBufferedImage(link + poster);
							
							if (home_w185 != null)
							{
								home_w185 = ImageUtils.scaleImage(home_w185, 185, 280);
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(home_w185, "JPG", f);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								home_w185 = ImageUtils.getImage("notFound.png");
							}
						} finally
						{
							news.updateImage(home_w185);
						}
					}
				};
				
				ThreadManager.submitSystemTask(task);
				home_w185 = ImageUtils.getImage("loading.png");
			}
		}
		return home_w185;
	}
	
	public BufferedImage initHome_w500(final NewsButton news)
	{
		final String link = "http://image.tmdb.org/t/p/w500";
		if (home_w500 == null)
		{
			final File f = new File(DirUtils.getCacheDir() + File.separator + "image",
					MD5.generatemd5(link + back) + ".jpg");
					
			if (f.exists())
			{
				home_w500 = ImageUtils.getImage(f);
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
							home_w500 = ImageUtils.getHttpBufferedImage(link + back);
							
							if (home_w500 != null)
							{
								home_w500 = ImageUtils.scaleImage(home_w500, 500, 280);
								try
								{
									if (!f.getParentFile().exists())
										f.getParentFile().mkdirs();
										
									ImageIO.write(home_w500, "JPG", f);
								} catch (Exception e)
								{
									Log.stack(Log.IMAGE, e);
								}
							}
							else
							{
								home_w500 = ImageUtils.getImage("notFound.png");
							}
						} finally
						{
							news.updateImage(home_w500);
						}
					}
				};
				
				ThreadManager.submitSystemTask(task);
				home_w500 = ImageUtils.getImage("loading.png");
			}
		}
		return home_w500;
	}
	
	public String getGeneriIDs()
	{
		String res = "";
		boolean first = true;
		if (genre_ids != null)
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
		if (d.equals(""))
		{
			genre_ids = new int[0];
			return;
		}
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
			json.put("scheda_id", ID);
			json.put("title", title);
			json.put("release_date", release);
			json.put("poster", poster);
			json.put("background", back);
			json.put("description", desc);
			json.put("genre_ids", getGeneriIDs());
			json.put("vote", vote);
			json.put("type", type.ID);
			json.put("fallback", fallback_desc);
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
			ID = json.getInt("scheda_id");
			title = json.getString("title");
			release = json.getString("release_date");
			poster = json.getString("poster");
			back = json.getString("background");
			desc = json.getString("description");
			this.setGeneriIDs(json.getString("genre_ids"));
			vote = json.getDouble("vote");
			type = MainType.getMainTypeByID(json.getInt("type"));
			fallback_desc = json.getInt("fallback");
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
	
	@Override
	public String toString()
	{
		String result = "ID: " + ID + " title: " + title + " release: " + release + " poster: " + poster + " back: "
				+ back + " desc: " + desc + " vote: " + vote + " genre_ids: ";
				
		for (int a : genre_ids)
		{
			result += a + ",";
		}
		return result;
	}
}
