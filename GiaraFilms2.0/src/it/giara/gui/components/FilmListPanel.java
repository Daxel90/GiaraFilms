package it.giara.gui.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.giara.analyze.enums.MainType;
import it.giara.gui.DefaultGui;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.ThreadManager;

public class FilmListPanel extends JPanel
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
	
	private JButton film, serietv, unknowfile;
	private ImageButton ArrowUp, ArrowDown;
	
	DownloadList allFile;
	JScrollPane scroll;
	DefaultGui gui;
	
	public FilmListPanel(AbstractFilmList l, MainType t, DefaultGui gui)
	{
		this(l, gui);
		show = t;
		
	}
	
	public FilmListPanel(AbstractFilmList l, DefaultGui g)
	{
		setLayout(null);
		setOpaque(false);
		setBackground(ColorUtils.Back);
		list = l;
		list.setJPanel(this);
		gui = g;
		
		film = new JButton();
		film.addActionListener(filmAL);
		serietv = new JButton();
		serietv.addActionListener(serieTvAL);
		unknowfile = new JButton();
		unknowfile.addActionListener(altroAL);
		
		film.setText("<html><h3>Film    " + list.films.size() + "</html>");
		serietv.setText("<html><h3>SerieTv    " + list.series.size() + "</html>");
		unknowfile.setText("<html><h3>Tutti i File    " + list.allFile.size() + "</html>");
		
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
				
		allFile = new DownloadList(list.allFile, gui);
		scroll = new JScrollPane(allFile);
		scroll.setBorder(BorderFactory.createEtchedBorder());
		scroll.setFocusable(true);
		scroll.setOpaque(false);
		scroll.setBackground(ColorUtils.Trasparent);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		allFile.addMouseWheelListener(new MouseAdapter()
		{
			public void mouseWheelMoved(MouseWheelEvent evt)
			{
				if (evt.getWheelRotation() == 1)
				{
					int iScrollAmount = evt.getScrollAmount();
					int iNewValue = scroll.getVerticalScrollBar().getValue()
							+ scroll.getVerticalScrollBar().getBlockIncrement() * iScrollAmount;
					if (iNewValue <= scroll.getVerticalScrollBar().getMaximum())
					{
						scroll.getVerticalScrollBar().setValue(iNewValue);
					}
					else
					{
						scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					}
				}
				else if (evt.getWheelRotation() == -1)
				{
					int iScrollAmount = evt.getScrollAmount();
					int iNewValue = scroll.getVerticalScrollBar().getValue()
							- scroll.getVerticalScrollBar().getBlockIncrement() * iScrollAmount;
					if (iNewValue <= scroll.getVerticalScrollBar().getMaximum())
					{
						scroll.getVerticalScrollBar().setValue(iNewValue);
					}
					else
					{
						scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					}
				}
			}
		});
		
		init();
		
	}
	
	public void init()
	{
		this.removeAll();
		
		film.setBounds(this.getWidth() / 16, 5, this.getWidth() / 4, 30);
		serietv.setBounds(2 * this.getWidth() / 16 + this.getWidth() / 4, 5, this.getWidth() / 4, 30);
		unknowfile.setBounds(3 * this.getWidth() / 16 + this.getWidth() / 4 * 2, 5, this.getWidth() / 4, 30);
		
		switch (show)
		{
			case Film:
				film.setEnabled(false);
				serietv.setEnabled(true);
				unknowfile.setEnabled(true);
				break;
			case SerieTV:
				serietv.setEnabled(false);
				film.setEnabled(true);
				unknowfile.setEnabled(true);
				break;
			case NULL:
				unknowfile.setEnabled(false);
				serietv.setEnabled(true);
				film.setEnabled(true);
				break;
			default:
				break;
		}
		
		this.add(film);
		this.add(serietv);
		this.add(unknowfile);
		
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, this.getWidth(), 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		column = (this.getWidth() - 32 - 2 * spaceFileButton) / (FileButtonWidth + spaceFileButton);
		int COLUMNcenterOffset = ((this.getWidth() - 32 - 2 * spaceFileButton)
				- (FileButtonWidth + spaceFileButton) * column) / 2;
		row = (this.getHeight() - 40) / (FileButtonHeight + spaceFileButton);
		int ROWcenterOffset = (((this.getHeight() - 40) - ((FileButtonHeight + spaceFileButton) * row))) / 2;
		
		boolean upArr = false;
		boolean downArr = false;
		switch (show)
		{
			case Film:
				film.setEnabled(false);
				serietv.setEnabled(true);
				unknowfile.setEnabled(true);
				
				upArr = offset > 0;
				downArr = list.films.size() > ((column * row) + offset);
				
				break;
			case SerieTV:
				serietv.setEnabled(false);
				film.setEnabled(true);
				unknowfile.setEnabled(true);
				
				upArr = offset > 0;
				downArr = list.series.size() > ((column * row) + offset);
				
				break;
			default:
				break;
		}
		
		ArrowUp.setBounds(this.getWidth() - 37, 55, 32, 32);
		ArrowUp.setVisible(upArr);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 37, this.getHeight() - 42, 32, 32);
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
								ROWcenterOffset + 40 + spaceFileButton + j * (FileButtonHeight + spaceFileButton),
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
								ROWcenterOffset + 40 + spaceFileButton + j * (FileButtonHeight + spaceFileButton),
								this);
						this.add(f);
						number++;
					}
			}
				break;
				
			case NULL:
			{
				
				allFile.setBounds(0, 0, this.getWidth() - 20, this.getHeight() - 60);
				allFile.setPreferredSize(new Dimension(this.getWidth() - 20, this.getHeight() - 60));
				allFile.init();
				
				scroll.setBounds(10, 50, this.getWidth() - 20, this.getHeight() - 60);
				scroll.setPreferredSize(new Dimension(this.getWidth() - 20, this.getHeight() - 60));
				
				this.add(scroll);
			}
			
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
	
	int updateN = 0;
	boolean thUpdateRunning = false;
	
	public void updateFromList(MainType type)
	{
		film.setText("<html><h3>Film    " + list.films.size() + "</html>");
		serietv.setText("<html><h3>SerieTv    " + list.series.size() + "</html>");
		unknowfile.setText("<html><h3>Tutti i File    " + list.allFile.size() + "</html>");
		if (type.equals(show) && type.equals(MainType.NULL))
		{
			updateN++;
			if (!thUpdateRunning && updateN > 0)
			{
				Runnable run = new Runnable()
				{
					@Override
					public void run()
					{
						FunctionsUtils.sleep(1000);
						allFile.init();
						updateN = 0;
						thUpdateRunning = false;
					}
				};
				thUpdateRunning = true;
				ThreadManager.submitCacheTask(run);
			}
			
		}
		else if (type.equals(show))
		{
			init();
			this.repaint();
		}
	}
	
	private ActionListener filmAL = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			show = MainType.Film;
			offset = 0;
			updateFromList(MainType.Film);
			repaint();
		}
	};
	
	private ActionListener serieTvAL = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			show = MainType.SerieTV;
			offset = 0;
			updateFromList(MainType.SerieTV);
			repaint();
		}
	};
	
	private ActionListener altroAL = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			show = MainType.NULL;
			offset = 0;
			updateFromList(MainType.NULL);
			init();
			repaint();
		}
	};
	
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
