package it.giara.gui.components.home;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.ImageButton;
import it.giara.gui.section.HomePage;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.ListRequest;
import it.giara.tmdb.GenereType;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.ThreadManager;

public class LateralDrag extends JPanel
{
	private static final long serialVersionUID = -5790293229312148746L;
	
	public int progress = 0;
	public boolean out = false;
	public boolean inUse = false;
	
	JScrollPane scroll;
	JPanel scrolJPanel;
	
	ImageButton letOut;
	ImageButton letIn;
	LateralButton news, Films, TVSeries;
	LateralButton[] genreFilm, genreTVSerie;
	JLabel sep3;
	JLabel background;
	HomePage home;
	boolean expandingFilm = true;
	boolean expandindTVSerie = true;
	int offsetFilm = 0;
	int offsetTvSerie = 0;
	
	ListRequest listR = null;
	
	public LateralDrag(HomePage i)
	{
		setLayout(null);
		setOpaque(false);
		setBackground(ColorUtils.Back);
		home = i;
		scrolJPanel = new JPanel();
		scrolJPanel.setOpaque(false);
		scrolJPanel.setLayout(null);
		scrolJPanel.setBackground(ColorUtils.Trasparent);
		
		scroll = new JScrollPane();
		scroll.setFocusable(true);
		scroll.setBackground(ColorUtils.Back);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		
		letOut = new ImageButton(ImageUtils.getImage("gui/drag_right.png"),
				ImageUtils.getImage("gui/drag_right_over.png"), ImageUtils.getImage("gui/drag_right_over.png"),
				RunLetOut);
		letIn = new ImageButton(ImageUtils.getImage("gui/drag_left.png"), ImageUtils.getImage("gui/drag_left_over.png"),
				ImageUtils.getImage("gui/drag_left_over.png"), RunLetIn);
				
		news = new LateralButton(newsAL);
		news.setText("<html> <h3> <font color='white'>News </font></html>");
		news.setHorizontalAlignment(JLabel.CENTER);
		news.setSelected(true);
		
		Films = new LateralButton(allFilmAL);
		Films.setText("<html> <h3> <font color='white'>Film</font></html>");
		Films.setHorizontalAlignment(JLabel.CENTER);
		
		genreFilm = new LateralButton[GenereType.values().length];
		
		for (int k = 0; k < GenereType.values().length; k++)
		{
			final GenereType type = GenereType.values()[k];
			final int indice = k;
			genreFilm[k] = new LateralButton(new Runnable()
			{
				@Override
				public void run()
				{
					AbstractFilmList lista = new AbstractFilmList();
					if (listR != null)
						listR.running.setValue(false);
					listR = new ListRequest(type, MainType.Film, lista);
					home.showOnHomepage(lista, MainType.Film);
					news.setSelected(false);
					Films.setSelected(false);
					TVSeries.setSelected(false);
					news.repaint();
					Films.repaint();
					TVSeries.repaint();
					for (int j = 0; j < genreFilm.length; j++)
					{
						genreFilm[j].setSelected(false);
						genreFilm[j].repaint();
					}
					for (int j = 0; j < genreTVSerie.length; j++)
					{
						genreTVSerie[j].setSelected(false);
						genreTVSerie[j].repaint();
					}
					genreFilm[indice].setSelected(true);
					genreFilm[indice].repaint();
				}
			});
			genreFilm[k]
					.setText("<html> <h4> <font color='white'>&nbsp;&nbsp;&bull;     " + type.Name + "</font></html>");
			genreFilm[k].setHorizontalAlignment(JLabel.LEFT);
			genreFilm[k].setVisible(false);
		}
		
		genreTVSerie = new LateralButton[GenereType.values().length];
		
		for (int k = 0; k < GenereType.values().length; k++)
		{
			final GenereType type = GenereType.values()[k];
			final int indice = k;
			
			genreTVSerie[k] = new LateralButton(new Runnable()
			{
				
				@Override
				public void run()
				{
					AbstractFilmList lista = new AbstractFilmList();
					if (listR != null)
						listR.running.setValue(false);
					listR = new ListRequest(type, MainType.SerieTV, lista);
					home.showOnHomepage(lista, MainType.SerieTV);
					news.setSelected(false);
					Films.setSelected(false);
					TVSeries.setSelected(false);
					news.repaint();
					Films.repaint();
					TVSeries.repaint();
					for (int j = 0; j < genreFilm.length; j++)
					{
						genreFilm[j].setSelected(false);
						genreFilm[j].repaint();
					}
					for (int j = 0; j < genreTVSerie.length; j++)
					{
						genreTVSerie[j].setSelected(false);
						genreTVSerie[j].repaint();
					}
					genreTVSerie[indice].setSelected(true);
					genreTVSerie[indice].repaint();
					
				}
			});
			genreTVSerie[k]
					.setText("<html> <h4> <font color='white'>&nbsp;&nbsp;&bull;      " + type.Name + "</font></html>");
			genreTVSerie[k].setHorizontalAlignment(JLabel.LEFT);
			genreTVSerie[k].setVisible(false);
		}
		
		TVSeries = new LateralButton(allTVSerieAL);
		TVSeries.setText("<html> <h3> <font color='white'>Serie TV</font></html>");
		TVSeries.setHorizontalAlignment(JLabel.CENTER);
		
		sep3 = new JLabel();
		sep3.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		
		background = new JLabel();
		background.setBackground(ColorUtils.Back);
		background.setOpaque(true);
		init();
		
		this.add(letOut);
		this.add(letIn);
		
		this.add(scroll);
		scrolJPanel.add(news);
		scrolJPanel.add(Films);
		for (
		
		int k = 0; k < genreFilm.length; k++)
		
		{
			scrolJPanel.add(genreFilm[k]);
		}
		
		for (
		
		int k = 0; k < genreTVSerie.length; k++)
		
		{
			scrolJPanel.add(genreTVSerie[k]);
		}
		
		scrolJPanel.add(TVSeries);
		
		scrolJPanel.add(sep3);
		scrolJPanel.add(background);
		
	}
	
	public void init()
	{
		offsetFilm = 0;
		offsetTvSerie = 0;
		
		// scrolJPanel.setSize(this.getWidth(),this.getHeight()*2);
		scrolJPanel.setPreferredSize(new Dimension(this.getWidth() - 32 - 16, 1550));
		scroll.setBounds(0, 0, this.getWidth() - 26, this.getHeight());
		scroll.setViewportView(scrolJPanel);
		
		letOut.setBounds(this.getWidth() - 16, this.getHeight() / 2 - 16, 16, 32);
		letIn.setBounds(this.getWidth() - 32, this.getHeight() / 2 - 16, 16, 32);
		
		letIn.setVisible(out);
		letOut.setVisible(!out);
		
		news.setBounds(0, 0, this.getWidth() - 16 - 26, 40);
		
		Films.setBounds(0, 40, this.getWidth() - 16 - 26, 40);
		
		if (expandingFilm)
		{
			for (int k = 0; k < genreFilm.length; k++)
			{
				genreFilm[k].setBounds(0, 80 + offsetFilm, this.getWidth() - 16 - 26, 30);
				genreFilm[k].setVisible(true);
				offsetFilm += 30;
			}
		}
		else
		{
			for (int k = 0; k < genreFilm.length; k++)
			{
				genreFilm[k].setVisible(false);
			}
		}
		
		TVSeries.setBounds(0, 80 + offsetFilm, this.getWidth() - 16 - 26, 40);
		
		if (expandindTVSerie)
		{
			for (int k = 0; k < genreTVSerie.length; k++)
			{
				genreTVSerie[k].setBounds(0, 120 + offsetFilm + offsetTvSerie, this.getWidth() - 16 - 26, 30);
				genreTVSerie[k].setVisible(true);
				offsetTvSerie += 30;
			}
		}
		else
		{
			for (int k = 0; k < genreTVSerie.length; k++)
			{
				genreTVSerie[k].setVisible(false);
			}
		}
		
		sep3.setBounds(this.getWidth() - 16 - 32, 0, 1, this.getHeight());
		
		background.setBounds(0, 0, this.getWidth() - 16, this.getHeight());
		
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
	Runnable RunLetOut = new Runnable()
	{
		@Override
		public void run()
		{
			if (!inUse)
				ThreadManager.submitCacheTask(new Runnable()
				{
					@Override
					public void run()
					{
						inUse = true;
						out = true;
						while (progress < 20)
						{
							progress++;
							FunctionsUtils.sleep(10);
							setBounds(-getWidth() + 16 + (getWidth() - 16) * progress / 20, 40, getWidth(),
									getHeight());
						}
						inUse = false;
						repaint();
					}
				});
		}
	};
	
	Runnable RunLetIn = new Runnable()
	{
		@Override
		public void run()
		{
			if (!inUse)
				ThreadManager.submitCacheTask(new Runnable()
				{
					@Override
					public void run()
					{
						inUse = true;
						out = false;
						while (progress > 0)
						{
							progress--;
							FunctionsUtils.sleep(10);
							setBounds(-getWidth() + 16 + (getWidth() - 16) * progress / 20, 40, getWidth(),
									getHeight());
						}
						inUse = false;
						repaint();
					}
				});
		}
	};
	
	private Runnable allFilmAL = new Runnable()
	{
		@Override
		public void run()
		{
			news.setSelected(false);
			Films.setSelected(true);
			TVSeries.setSelected(false);
			
			for (int j = 0; j < genreFilm.length; j++)
			{
				genreFilm[j].setSelected(false);
				genreFilm[j].repaint();
			}
			for (int j = 0; j < genreTVSerie.length; j++)
			{
				genreTVSerie[j].setSelected(false);
				genreTVSerie[j].repaint();
			}
			
			// expandingFilm = !expandingFilm;
			// if (expandingFilm)
			// expandindTVSerie = false;
			init();
			AbstractFilmList lista = new AbstractFilmList();
			if (listR != null)
				listR.running.setValue(false);
			listR = new ListRequest(null, MainType.Film, lista);
			home.showOnHomepage(lista, MainType.Film);
			repaint();
		}
	};
	
	private Runnable allTVSerieAL = new Runnable()
	{
		@Override
		public void run()
		{
			news.setSelected(false);
			Films.setSelected(false);
			TVSeries.setSelected(true);
			
			for (int j = 0; j < genreFilm.length; j++)
			{
				genreFilm[j].setSelected(false);
				genreFilm[j].repaint();
			}
			for (int j = 0; j < genreTVSerie.length; j++)
			{
				genreTVSerie[j].setSelected(false);
				genreTVSerie[j].repaint();
			}
			
			// expandindTVSerie = !expandindTVSerie;
			// if (expandindTVSerie)
			// expandingFilm = false;
			init();
			AbstractFilmList lista = new AbstractFilmList();
			if (listR != null)
				listR.running.setValue(false);
			listR = new ListRequest(null, MainType.SerieTV, lista);
			home.showOnHomepage(lista, MainType.SerieTV);
			repaint();
		}
	};
	
	private Runnable newsAL = new Runnable()
	{
		@Override
		public void run()
		{
			news.setSelected(true);
			Films.setSelected(false);
			TVSeries.setSelected(false);
			home.showOnHomepage(null, MainType.NULL);
			repaint();
		}
	};
	
}
