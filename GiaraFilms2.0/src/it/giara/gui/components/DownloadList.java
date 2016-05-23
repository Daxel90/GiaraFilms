package it.giara.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.QualityAudio;
import it.giara.analyze.enums.QualityVideo;
import it.giara.download.DownloadManager;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.section.Download;
import it.giara.gui.section.DownloadAllEpisode;
import it.giara.gui.section.Search;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.DownloadStrategy;
import it.giara.utils.ThreadManager;

public class DownloadList extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	ArrayList<String[]> fileList;
	TreeMap<Integer, TreeMap<Integer, ArrayList<String[]>>> SerieList;
	// HashMap<Integer,[boolean,HashMap<Integer, boolean>]>
	HashMap<Integer, Object[]> SerieTree = new HashMap<Integer, Object[]>();
	int offset = 0;
	private BufferedImage download, download_over, plus, plus_over, minus, minus_over;
	private DefaultGui gui;
	boolean serie = false;
	
	public DownloadList(HashMap<Integer, TreeMap<Integer, ArrayList<String[]>>> list, DefaultGui g)
	{
		this(g);
		SerieList = new TreeMap<Integer, TreeMap<Integer, ArrayList<String[]>>>(list);
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
		this.setLayout(null);
		this.setOpaque(true);
		this.setBackground(ColorUtils.Back);
		
		download = ImageUtils.getImage("gui/icon32px/download.png");
		download_over = ImageUtils.getImage("gui/icon32px/download_over.png");
		plus = ImageUtils.getImage("gui/icon32px/plus.png");
		plus_over = ImageUtils.getImage("gui/icon32px/plus_over.png");
		minus = ImageUtils.getImage("gui/icon32px/minus.png");
		minus_over = ImageUtils.getImage("gui/icon32px/minus_over.png");
	}
	
	@SuppressWarnings("unchecked")
	public void init()
	{
		this.removeAll();
		boolean searchList = gui instanceof Search;
		int n = 0;
		
		if (!searchList)
		{
			JLabel V = new JLabel("<html><h3>V</html>");
			V.setBounds(15 + this.getWidth() - 80-25, 5, 20, 20);
			V.setToolTipText("Qualità Video");
			V.setHorizontalAlignment(JLabel.CENTER);
			this.add(V);
			
			JLabel A = new JLabel("<html><h3>A</html>");
			A.setBounds(15 + this.getWidth() - 55-25, 5, 20, 20);
			A.setToolTipText("Qualità Audio");
			A.setHorizontalAlignment(JLabel.CENTER);
			this.add(A);
			
			JLabel L = new JLabel("<html><h3>L</html>");
			L.setBounds(15 + this.getWidth() - 30-25, 5, 20, 20);
			L.setToolTipText("Qualità Audio");
			L.setHorizontalAlignment(JLabel.CENTER);
			this.add(L);
		}
		
		if (!serie)
		{
			
			for (int l = 0; l < fileList.size(); l++)
			{
				final String file = fileList.get(l)[0];
				final String size = fileList.get(l)[1];
				
				FileInfo finfo = new FileInfo(file + "", false);
				finfo.parseTags();
				
				JLabel name = new JLabel();
				name.setText("<html><h4>" + size + "&nbsp;&nbsp;&nbsp;&nbsp;" + file + "</html>");
				name.setBounds(10, 30 + (n - offset) * 40, this.getWidth() - 80-25, 30);
				name.setBorder(BorderFactory.createEtchedBorder());
				this.add(name);
				
				if (finfo.video != QualityVideo.NULL && !searchList)
				{
					JLabel Vquality = new JLabel();
					Vquality.setText("" + (int) finfo.video.qualita);
					Vquality.setBounds(15 + this.getWidth() - 80-25, 35 + (n - offset) * 40, 20, 20);
					Vquality.setBackground(ColorUtils.getColorQuality(finfo.video.qualita));
					Vquality.setToolTipText("Video: " + finfo.video.name() + " " + finfo.video.descrizione);
					Vquality.setHorizontalAlignment(JLabel.CENTER);
					Vquality.setOpaque(true);
					Vquality.setForeground(ColorUtils.Back);
					this.add(Vquality);
				}
				
				if (finfo.audio != QualityAudio.NULL && !searchList)
				{
					JLabel Aquality = new JLabel();
					Aquality.setText("" + (int) finfo.audio.qualita);
					Aquality.setBounds(15 + this.getWidth() - 55-25, 35 + (n - offset) * 40, 20, 20);
					Aquality.setBackground(ColorUtils.getColorQuality(finfo.audio.qualita));
					Aquality.setToolTipText("Audio: " + finfo.audio.name() + " " + finfo.audio.descrizione);
					Aquality.setHorizontalAlignment(JLabel.CENTER);
					Aquality.setOpaque(true);
					Aquality.setForeground(ColorUtils.Back);
					this.add(Aquality);
				}
				
				if ((finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH)
						|| finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN)
						|| finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed))
						&& !searchList)
				{
					String text = "";
					String textTip = "";
					Color c = ColorUtils.Back;
					if (finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH))
					{
						text = "EN";
						c = new Color(0, 128, 255);
						textTip += "Inglese ";
					}
					if (finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN))
					{
						text = "IT";
						c = new Color(0, 204, 0);
						textTip += "Italiano ";
					}
					if (finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed))
					{
						text = "EN";
						c = new Color(0, 128, 255);
						textTip += "Sottotitolato ";
					}
					
					JLabel Tags = new JLabel();
					Tags.setText(text);
					Tags.setBounds(15 + this.getWidth() - 30-25, 35 + (n - offset) * 40, 20, 20);
					Tags.setBackground(c);
					Tags.setToolTipText("Lingua: " + textTip);
					Tags.setHorizontalAlignment(JLabel.CENTER);
					Tags.setOpaque(true);
					Tags.setForeground(ColorUtils.Back);
					this.add(Tags);
				}
				
				ImageButton downloads = new ImageButton(download, download_over, download_over, new Runnable()
				{
					@Override
					public void run()
					{
						Runnable task = new Runnable()
						{
							public void run()
							{
								DownloadManager.downloadFile(file, false);
							}
						};
						ThreadManager.submitCacheTask(task);
						MainFrame.getInstance().setInternalPane(new Download(gui.guiInstance));
					}
				});
				downloads.setBounds(this.getWidth() - 110-25, 31 + (n - offset) * 40, 28, 28);
				downloads.setToolTipText("Scarica File");
				this.add(downloads);
				n++;
			}
		}
		else
		{
			for (final Entry<Integer, TreeMap<Integer, ArrayList<String[]>>> serie : SerieList.entrySet())
			{
				final int serieN = serie.getKey();
				
				JLabel serieLabel = new JLabel();
				serieLabel.setText("<html><h2>Serie " + serieN + "</html>");
				serieLabel.setBounds(10, 35 + (n - offset) * 40, this.getWidth() - 50-55, 30);
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
				downloadSerie.setBounds(this.getWidth() - 80-55, 36 + (n - 1 - offset) * 40, 28, 28);
				downloadSerie.setToolTipText("Scarica Stagione");
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
					minusSerie.setBounds(15, 36 + (n - 1 - offset) * 40, 28, 28);
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
					plusSerie.setBounds(15, 36 + (n - 1 - offset) * 40, 28, 28);
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
					episodeLabel.setBounds(30, 35 + (n - offset) * 40, this.getWidth() - 70-55, 30);
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
						minusSerie.setBounds(35, 36 + (n - 1 - offset) * 40, 28, 28);
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
						plusSerie.setBounds(35, 36 + (n - 1 - offset) * 40, 28, 28);
						this.add(plusSerie);
						continue;
					}
					
					for (String[] Arrfile : episode.getValue())
					{
						final String file = Arrfile[0];
						final String size = Arrfile[1];
						
						JLabel name = new JLabel();
						name.setText("<html><h3>" + size + "&nbsp;&nbsp;&nbsp;&nbsp;" + file + "</html>");
						name.setBounds(50, 35 + (n - offset) * 40, this.getWidth() - 120-25, 30);
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
										DownloadManager.downloadFile(file, false);
									}
								};
								ThreadManager.submitCacheTask(task);
								MainFrame.getInstance().setInternalPane(new Download(gui.guiInstance));
							}
						});
						downloads.setToolTipText("Scarica File");
						downloads.setBounds(this.getWidth() - 110-25, 36 + (n - offset) * 40, 28, 28);
						this.add(downloads);
						
						FileInfo finfo = new FileInfo(file + "", false);
						finfo.parseTags();
						
						if (finfo.video != QualityVideo.NULL && !searchList)
						{
							JLabel Vquality = new JLabel();
							Vquality.setText("" + (int) finfo.video.qualita);
							Vquality.setBounds(15 + this.getWidth() - 80-25, 35 + (n - offset) * 40, 20, 20);
							Vquality.setBackground(ColorUtils.getColorQuality(finfo.video.qualita));
							Vquality.setToolTipText("Video: " + finfo.video.name() + " " + finfo.video.descrizione);
							Vquality.setHorizontalAlignment(JLabel.CENTER);
							Vquality.setOpaque(true);
							Vquality.setForeground(ColorUtils.Back);
							this.add(Vquality);
						}
						
						if (finfo.audio != QualityAudio.NULL && !searchList)
						{
							JLabel Aquality = new JLabel();
							Aquality.setText("" + (int) finfo.audio.qualita);
							Aquality.setBounds(15 + this.getWidth() - 55-25, 35 + (n - offset) * 40, 20, 20);
							Aquality.setBackground(ColorUtils.getColorQuality(finfo.audio.qualita));
							Aquality.setToolTipText("Audio: " + finfo.audio.name() + " " + finfo.audio.descrizione);
							Aquality.setHorizontalAlignment(JLabel.CENTER);
							Aquality.setOpaque(true);
							Aquality.setForeground(ColorUtils.Back);
							this.add(Aquality);
						}
						
						if ((finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH)
								|| finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN)
								|| finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed))
								&& !searchList)
						{
							String text = "";
							String textTip = "";
							Color c = ColorUtils.Back;
							if (finfo.tags.contains(AddictionalTags.ENG) || finfo.tags.contains(AddictionalTags.ENGLiSH))
							{
								text = "EN";
								c = new Color(0, 128, 255);
								textTip += "Inglese ";
							}
							if (finfo.tags.contains(AddictionalTags.ITA) || finfo.tags.contains(AddictionalTags.iTALiAN))
							{
								text = "IT";
								c = new Color(0, 204, 0);
								textTip += "Italiano ";
							}
							if (finfo.tags.contains(AddictionalTags.SUB) || finfo.tags.contains(AddictionalTags.Subbed))
							{
								text = "EN";
								c = new Color(0, 128, 255);
								textTip += "Sottotitolato ";
							}
							
							JLabel Tags = new JLabel();
							Tags.setText(text);
							Tags.setBounds(15 + this.getWidth() - 30-25, 35 + (n - offset) * 40, 20, 20);
							Tags.setBackground(c);
							Tags.setToolTipText("Lingua: " + textTip);
							Tags.setHorizontalAlignment(JLabel.CENTER);
							Tags.setOpaque(true);
							Tags.setForeground(ColorUtils.Back);
							this.add(Tags);
						}
						

						n++;
						
					}
				}
			}
		}
		
		Dimension dim = new Dimension(this.getWidth(), n * 40 + 35);
		
		this.setPreferredSize(dim);
		this.setSize(dim);
		
	}
	
}
