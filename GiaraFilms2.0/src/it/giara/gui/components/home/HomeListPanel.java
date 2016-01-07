package it.giara.gui.components.home;

import javax.swing.JPanel;

import it.giara.analyze.enums.MainType;
import it.giara.gui.components.FilmButton;
import it.giara.gui.components.ImageButton;
import it.giara.gui.components.TvSerieButton;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class HomeListPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private AbstractFilmList list;
	
	private int FileButtonWidth = 140;
	private int FileButtonHeight = 240;
	private int spaceFileButton = 10;
	private int row = 1;
	private int column = 1;
	private int offset = 0;
	
	MainType show = MainType.Film;
	
	private ImageButton ArrowUp, ArrowDown;
	
	public void updateAbstractFilmList(AbstractFilmList l, MainType t)
	{
		if(list != null)
			list.clear();
		list = l;
		list.setJPanel(this);
		show = t;
		offset = 0;
		
	}
	
	public HomeListPanel()
	{
		setLayout(null);
		setOpaque(false);
		setBackground(ColorUtils.Back);
		
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
				
		init();
	}
	
	public void init()
	{
		this.removeAll();
		
		if (list == null)
			return;
		
		column = (this.getWidth() - 32 - 2 * spaceFileButton) / (FileButtonWidth + spaceFileButton);
		int COLUMNcenterOffset = ((this.getWidth() - 32 - 2 * spaceFileButton)
				- (FileButtonWidth + spaceFileButton) * column) / 2;
		row = (this.getHeight()) / (FileButtonHeight + spaceFileButton);
		int ROWcenterOffset = (((this.getHeight()) - ((FileButtonHeight + spaceFileButton) * row))) / 2;
		
		boolean upArr = false;
		boolean downArr = false;
		switch (show)
		{
			case Film:
				upArr = offset > 0;
				downArr = list.films.size() > ((column * row) + offset);
				
				break;
			case SerieTV:
				upArr = offset > 0;
				downArr = list.series.size() > ((column * row) + offset);
				
				break;
			default:
				break;
		}
		
		ArrowUp.setBounds(this.getWidth() - 37, 55, 32, 32);
		ArrowUp.setVisible(upArr);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 37, this.getHeight() - 32, 32, 32);
		ArrowDown.setVisible(downArr);
		this.add(ArrowDown);
		
		switch (show)
		{
			case Film:
			{
				int number = offset;
				for (int j = 0; j < row; j++)
					for (int k = 0; k < column; k++)
					{
						if (number >= list.films.size())
							continue;
						FilmButton f = new FilmButton(list.films.get(number),
								COLUMNcenterOffset + spaceFileButton + k * (FileButtonWidth + spaceFileButton),
								ROWcenterOffset  + spaceFileButton + j * (FileButtonHeight + spaceFileButton),
								this);
						this.add(f);
						number++;
					}
			}
				break;
				
			case SerieTV:
			{
				int number = offset;
				for (int j = 0; j < row; j++)
					for (int k = 0; k < column; k++)
					{
						if (number >= list.series.size())
							continue;
						TvSerieButton f = new TvSerieButton(list.series.get(number),
								COLUMNcenterOffset + spaceFileButton + k * (FileButtonWidth + spaceFileButton),
								ROWcenterOffset  + spaceFileButton + j * (FileButtonHeight + spaceFileButton),
								this);
						this.add(f);
						number++;
					}
			}
				break;
				
			default:
				break;
				
		}
		
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
	public void updateFromList(MainType type)
	{
		if (type.equals(show))
		{
			init();
			this.repaint();
		}
	}
	
	Runnable RunUp = new Runnable()
	{
		@Override
		public void run()
		{
			offset -= column;
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
			offset += column;
			init();
			repaint();
		}
		
	};
}
