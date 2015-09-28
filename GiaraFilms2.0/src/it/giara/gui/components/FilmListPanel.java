package it.giara.gui.components;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	
	MainType show = MainType.NULL;
	
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
		init();
	}
	
	public void init()
	{
		this.removeAll();
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
				Object[][] tamplate = {{}};
				ArrayList<Object[]> data = new ArrayList<Object[]>();
				
				for (int l = 0; l < list.unknowFile.size(); l++)
					data.add(new Object[] { list.unknowFile.get(l) });
					
				Object[] columnNames = { "Nome del File" };
				JTable table = new JTable(data.toArray(tamplate), columnNames);
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBounds(10, 50, this.getWidth() - 20, this.getHeight() - 60);
				this.add(scrollPane);
				// ListSelectionModel selectionModel = new
				// DefaultListSelectionModel();
				// selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
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
		if (type.equals(show))
		{
			init();
			this.repaint();
		}
	}
	
}
