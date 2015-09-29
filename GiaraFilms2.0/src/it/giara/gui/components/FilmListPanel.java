package it.giara.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;

public class FilmListPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private AbstractFilmList list;
	
	private int FileButtonWidth = 140;
	private int FileButtonHeight = 240;
	private int spaceFileButton = 10;
	
	MainType show = MainType.Film;
	
	private JButton film, serietv, unknowfile;
	
	public FilmListPanel(AbstractFilmList l, MainType t)
	{
		this(l);
		show = t;
	}
	
	public FilmListPanel(AbstractFilmList l)
	{
		setLayout(null);
		setOpaque(false);
		list = l;
		list.setJPanel(this);
		
		film = new JButton();
		film.addActionListener(filmAL);
		serietv = new JButton();
		serietv.addActionListener(serieTvAL);
		unknowfile = new JButton();
		unknowfile.addActionListener(altroAL);
		
		film.setText("<html><h3>Film    "+list.films.size()+"</html>");
		serietv.setText("<html><h3>SerieTv    "+list.series.size()+"</html>");
		unknowfile.setText("<html><h3>Tutti i File    "+list.allFile.size()+"</html>");
		
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
		
		int column = (this.getWidth() - 2 * spaceFileButton) / (FileButtonWidth + spaceFileButton);
		int COLUMNcenterOffset = ((this.getWidth() - 2 * spaceFileButton)
				- (FileButtonWidth + spaceFileButton) * column) / 2;
		int row = (this.getHeight() - 40) / (FileButtonHeight + spaceFileButton);
		int ROWcenterOffset = (((this.getHeight() - 40) - ((FileButtonHeight + spaceFileButton) * row))) / 2;
		
		switch (show)
		{
			case Film:
			{
				int number = 0;
				for (int j = 0; j < row; j++)
					for (int k = 0; k < column; k++)
					{
						if (number >= list.films.size())
							continue;
						FilmButton f = new FilmButton(list.films.get(number),
								COLUMNcenterOffset + spaceFileButton + k * (FileButtonWidth + spaceFileButton),
								ROWcenterOffset + 40 + spaceFileButton + j * (FileButtonHeight + spaceFileButton));
						this.add(f);
						number++;
					}
			}
				break;
				
			case SerieTV:
			{
				int number = 0;
				for (int j = 0; j < row; j++)
					for (int k = 0; k < column; k++)
					{
						if (number >= list.series.size())
							continue;
						TvSerieButton f = new TvSerieButton(list.series.get(number),
								COLUMNcenterOffset + spaceFileButton + k * (FileButtonWidth + spaceFileButton),
								ROWcenterOffset + 40 + spaceFileButton + j * (FileButtonHeight + spaceFileButton));
						this.add(f);
						number++;
					}
			}
				break;
				
			case NULL:
			{
				Object[][] tamplate = { { "" } };
				ArrayList<Object[]> data = new ArrayList<Object[]>();
				
				for (int l = 0; l < list.allFile.size(); l++)
					data.add(new Object[] { list.allFile.get(l) });
				if (data.size() > 0)
					tamplate = data.toArray(tamplate);
				Object[] columnNames = { "Nome del File" };
				JTable table = new JTable(tamplate, columnNames);
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBounds(10, 50, this.getWidth() - 20, this.getHeight() - 60);
				this.add(scrollPane);
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
	
	public void updateFromList(MainType type)
	{
		film.setText("<html><h3>Film    "+list.films.size()+"</html>");
		serietv.setText("<html><h3>SerieTv    "+list.series.size()+"</html>");
		unknowfile.setText("<html><h3>Tutti i File    "+list.allFile.size()+"</html>");
		
		if (type.equals(show))
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
			updateFromList(MainType.Film);
		}
	};
	
	private ActionListener serieTvAL = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			show = MainType.SerieTV;
			updateFromList(MainType.SerieTV);
		}
	};
	
	private ActionListener altroAL = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			show = MainType.NULL;
			updateFromList(MainType.NULL);
		}
	};
}
