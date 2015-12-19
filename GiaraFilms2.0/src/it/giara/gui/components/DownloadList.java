package it.giara.gui.components;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.download.DownloadManager;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.section.Download;
import it.giara.gui.section.DownloadAllEpisode;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.DownloadStrategy;
import it.giara.utils.ThreadManager;

public class DownloadList extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	ArrayList<String[]> fileList;
	HashMap<Integer, HashMap<Integer, ArrayList<String[]>>> SerieList;
	// HashMap<Integer,[boolean,HashMap<Integer, boolean>]>
	HashMap<Integer, Object[]> SerieTree = new HashMap<Integer, Object[]>();
	int offset = 0;
	private ImageButton ArrowUp, ArrowDown;
	private BufferedImage download, download_over, plus, plus_over, minus, minus_over;
	private DefaultGui gui;
	boolean serie = false;
	
	public DownloadList(HashMap<Integer, HashMap<Integer, ArrayList<String[]>>> list, DefaultGui g)
	{
		this(g);
		SerieList = list;
		serie = true;
	}
	
	public DownloadList(ArrayList<String[]> list, DefaultGui g)
	{
		this(g);
		fileList = list;
	}
	
	public DownloadList(DefaultGui g)
	{
		gui = g;
		setLayout(null);
		setOpaque(true);
		setBackground(ColorUtils.Back);
		
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
		download = ImageUtils.getImage("gui/download.png");
		download_over = ImageUtils.getImage("gui/download_over.png");
		plus = ImageUtils.getImage("gui/plus.png");
		plus_over = ImageUtils.getImage("gui/plus_over.png");
		minus = ImageUtils.getImage("gui/minus.png");
		minus_over = ImageUtils.getImage("gui/minus_over.png");
	}
	
	@SuppressWarnings("unchecked")
	public void init()
	{
		this.removeAll();
		
		int n = 0;
		
		if (!serie)
		{
			for (int l = 0; l < fileList.size(); l++)
			{
				final String file = fileList.get(l)[0];
				final String size = fileList.get(l)[1];
				if (n < offset || 10 + (n - offset) * 40 > this.getHeight())
				{
					n++;
					continue;
				}
				
				JLabel name = new JLabel();
				name.setText("<html><h3>" + size + "&nbsp;&nbsp;&nbsp;&nbsp;" + file + "</html>");
				name.setBounds(10, 10 + (n - offset) * 40, this.getWidth() - 50, 30);
				name.setBorder(BorderFactory.createEtchedBorder());
				this.add(name);
				
				ImageButton downloads = new ImageButton(download, download_over, download_over, new Runnable()
				{
					@Override
					public void run()
					{
						Runnable task = new Runnable()
						{
							public void run()
							{
								DownloadManager.downloadFile(file);
							}
						};
						ThreadManager.submitCacheTask(task);
						MainFrame.getInstance().setInternalPane(new Download(gui.guiInstance));
					}
				});
				downloads.setBounds(this.getWidth() - 80, 11 + (n - offset) * 40, 28, 28);
				this.add(downloads);
				n++;
			}
		}
		else
		{
			for (final Entry<Integer, HashMap<Integer, ArrayList<String[]>>> serie : SerieList.entrySet())
			{
				final int serieN = serie.getKey();
				
				JLabel serieLabel = new JLabel();
				serieLabel.setText("<html><h2>Serie " + serieN + "</html>");
				serieLabel.setBounds(10, 10 + (n - offset) * 40, this.getWidth() - 50, 30);
				serieLabel.setBorder(BorderFactory.createEtchedBorder());
				serieLabel.setHorizontalAlignment(JLabel.CENTER);
				this.add(serieLabel);
				n++;
				
				if (!SerieTree.containsKey(serieN))
				{
					Object[] obj = { false, new HashMap<Integer, Boolean>() };
					SerieTree.put(serieN, obj);
				}
				
				ImageButton downloadSerie = new ImageButton(download, download_over, download_over, new Runnable()
				{
					@Override
					public void run()
					{
						Runnable task = new Runnable()
						{
							public void run()
							{
								MainFrame.getInstance()
										.setInternalPane(new DownloadAllEpisode(gui.guiInstance, serieN,
												DownloadStrategy.nEpisodesSeries(serie.getValue()),
												DownloadStrategy.downloadSeries(serie.getValue())));
							}
						};
						ThreadManager.submitCacheTask(task);
						// MainFrame.getInstance().setInternalPane(new
						// Download(gui.guiInstance));
					}
				});
				downloadSerie.setBounds(this.getWidth() - 80, 11 + (n - 1 - offset) * 40, 28, 28);
				this.add(downloadSerie);
				
				if ((Boolean) SerieTree.get(serieN)[0])
				{
					ImageButton minusSerie = new ImageButton(minus, minus_over, minus_over, new Runnable()
					{
						@Override
						public void run()
						{
							SerieTree.get(serieN)[0] = false;
							init();
							repaint();
						}
					});
					minusSerie.setBounds(15, 11 + (n - 1 - offset) * 40, 28, 28);
					this.add(minusSerie);
				}
				else
				{
					ImageButton plusSerie = new ImageButton(plus, plus_over, plus_over, new Runnable()
					{
						@Override
						public void run()
						{
							SerieTree.get(serieN)[0] = true;
							init();
							repaint();
						}
					});
					plusSerie.setBounds(15, 11 + (n - 1 - offset) * 40, 28, 28);
					this.add(plusSerie);
					continue;
				}
				
				for (Entry<Integer, ArrayList<String[]>> episode : serie.getValue().entrySet())
				{
					final int episodeN = episode.getKey();
					
					// HashMap<Integer, Boolean> mapTreeSerie =
					// (HashMap<Integer, Boolean>) SerieTree.get(serieN)[1];
					
					JLabel episodeLabel = new JLabel();
					episodeLabel.setText("<html><h2>Episodio " + episodeN + "</html>");
					episodeLabel.setBounds(30, 10 + (n - offset) * 40, this.getWidth() - 70, 30);
					episodeLabel.setBorder(BorderFactory.createEtchedBorder());
					episodeLabel.setHorizontalAlignment(JLabel.CENTER);
					this.add(episodeLabel);
					n++;
					
					if (!((HashMap<Integer, Boolean>) SerieTree.get(serieN)[1]).containsKey(episodeN))
					{
						((HashMap<Integer, Boolean>) SerieTree.get(serieN)[1]).put(episodeN, false);
					}
					
					if (((HashMap<Integer, Boolean>) SerieTree.get(serieN)[1]).get(episodeN))
					{
						ImageButton minusSerie = new ImageButton(minus, minus_over, minus_over, new Runnable()
						{
							@Override
							public void run()
							{
								((HashMap<Integer, Boolean>) SerieTree.get(serieN)[1]).put(episodeN, false);
								init();
								repaint();
							}
						});
						minusSerie.setBounds(35, 11 + (n - 1 - offset) * 40, 28, 28);
						this.add(minusSerie);
					}
					else
					{
						ImageButton plusSerie = new ImageButton(plus, plus_over, plus_over, new Runnable()
						{
							@Override
							public void run()
							{
								((HashMap<Integer, Boolean>) SerieTree.get(serieN)[1]).put(episodeN, true);
								init();
								repaint();
							}
						});
						plusSerie.setBounds(35, 11 + (n - 1 - offset) * 40, 28, 28);
						this.add(plusSerie);
						continue;
					}
					
					for (String[] Arrfile : episode.getValue())
					{
						final String file = Arrfile[0];
						final String size = Arrfile[1];
						if (n < offset || 10 + (n - offset) * 40 > this.getHeight())
						{
							n++;
							continue;
						}
						
						JLabel name = new JLabel();
						name.setText("<html><h3>" + size + "&nbsp;&nbsp;&nbsp;&nbsp;" + file + "</html>");
						name.setBounds(50, 10 + (n - offset) * 40, this.getWidth() - 90, 30);
						name.setBorder(BorderFactory.createEtchedBorder());
						this.add(name);
						
						ImageButton downloads = new ImageButton(download, download_over, download_over, new Runnable()
						{
							@Override
							public void run()
							{
								Runnable task = new Runnable()
								{
									public void run()
									{
										DownloadManager.downloadFile(file);
									}
								};
								ThreadManager.submitCacheTask(task);
								MainFrame.getInstance().setInternalPane(new Download(gui.guiInstance));
							}
						});
						downloads.setBounds(this.getWidth() - 80, 11 + (n - offset) * 40, 28, 28);
						this.add(downloads);
						n++;
						
					}
				}
			}
		}
		ArrowUp.setBounds(this.getWidth() - 37, 10, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 37, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible(n > (this.getHeight() - 10) / 40 + offset);
		this.add(ArrowDown);
		
	}
	
	Runnable RunUp = new Runnable()
	{
		@Override
		public void run()
		{
			offset -= 2;
			if (offset < 0)
				offset = 0;
			init();
			repaint();
		}
		
	};
	
	Runnable RunDown = new Runnable()
	{
		@Override
		public void run()
		{
			offset += 2;
			init();
			repaint();
		}
		
	};
	
}
