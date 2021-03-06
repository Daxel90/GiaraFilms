package it.giara.gui.components.home;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.ColorUtils;
import it.giara.sql.SQLQuery;
import it.giara.syncdata.NewServerQuery;
import it.giara.tmdb.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class NewsPanel extends JPanel
{
	private static final long serialVersionUID = 2068103664239879612L;
	
	private final int IMG_H = 280;
	private final int IMG_W1 = 185; // scheme 1
	private final int IMG_W2 = 500; // scheme 2
	
	int[][] scheme;
	
	public NewsPanel()
	{
		setLayout(null);
		setOpaque(false);
		setBackground(ColorUtils.Back);
		
		if (!NewServerQuery.newsLoaded)
		{
			Runnable r = new Runnable()
			{
				@Override
				public void run()
				{
					NewServerQuery.load100News();
				}
			};
			ThreadManager.submitCacheTask(r);
		}
		
		createScheme();
		init();
	}
	
	public void createScheme()
	{
		int w = this.getWidth() - 16;
		int h = this.getHeight() - 16;
		
		int maxH = h / IMG_H;
		int maxW = w / IMG_W1;
		
		scheme = new int[maxH][maxW];
		int x = 0;
		int i = 0;
		while (x < w)
		{
			int rand = FunctionsUtils.getRandom().nextInt(3);
			if (rand == 0 && (x + IMG_W2) < w) // 0 is 33% of probability
			{
				scheme[0][i] = 2;
				x += IMG_W2;
			}
			else if ((x + IMG_W1) < w)
			{
				scheme[0][i] = 1;
				x += IMG_W1;
			}
			else
			{
				break;
			}
			i++;
		}
		
		for (int j = 1; j < scheme.length; j++)
		{
			scheme[j] = FunctionsUtils.shuffleArray(FunctionsUtils.truncateArray(scheme[0], i));
		}
	}
	
	public void init()
	{
		this.removeAll();
		int posX;
		int posY = 8 + ((this.getHeight() - 16) - scheme.length * IMG_H) / 2;
		
		int nScheda = 0;
		
		for (int y = 0; y < scheme.length; y++)
		{
			int size = 0;
			for (int k = 0; k < scheme[y].length; k++)
			{
				if (scheme[y][k] == 0)
					break;
				if (scheme[y][k] == 1)
					size += IMG_W1;
				else if (scheme[y][k] == 2)
					size += IMG_W2;
			}
			
			posX = 8 + ((this.getWidth() - 16) - size) / 2;
			for (int x = 0; x < scheme[y].length; x++)
			{
				if (scheme[y][x] == 0)
					break;
					
				if(nScheda >= 100)
					return;
				
				TMDBScheda scheda = SQLQuery.readScheda(NewServerQuery.news[nScheda], MainType.Film);
				
				while (scheda == null || scheda.back.equals("") || scheda.poster.equals(""))
				{
					nScheda++;
					if (nScheda >= 100)
						return;
					scheda = SQLQuery.readScheda(NewServerQuery.news[nScheda], MainType.Film);
				}
				
				NewsButton sep3 = new NewsButton(SQLQuery.readScheda(NewServerQuery.news[nScheda], MainType.Film),
						scheme[y][x]);
				sep3.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
				if (scheme[y][x] == 1)
				{
					sep3.setBounds(posX, posY, IMG_W1, IMG_H);
					posX += IMG_W1;
				}
				else if (scheme[y][x] == 2)
				{
					sep3.setBounds(posX, posY, IMG_W2, IMG_H);
					posX += IMG_W2;
				}
				
				this.add(sep3);
				nScheda++;
			}
			
			posY += IMG_H;
		}
		
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		createScheme();
		init();
	}
	
}
