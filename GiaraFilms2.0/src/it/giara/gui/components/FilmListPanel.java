package it.giara.gui.components;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.sql.SQLQuery;

public class FilmListPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private AbstractFilmList list;
	
	private int FileButtonWidth = 140;
	private int FileButtonHeight = 240;
	private int spaceFileButton = 10;
	
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
		int ROWcenterOffset = (((this.getHeight() - 40) - ((FileButtonHeight + spaceFileButton) * row)))/2;
		
		int number = 0;
		for (int j = 0; j < row; j++)
			for (int k = 0; k < column; k++)
			{
				if(number>= list.films.size())
					continue;
				FilmButton f = new FilmButton(list.films.get(number),
						COLUMNcenterOffset + spaceFileButton + k * (FileButtonWidth + spaceFileButton),
						ROWcenterOffset + 40 + spaceFileButton + j * (FileButtonHeight + spaceFileButton));
				this.add(f);
				number++;
			}
			
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
	public void updateFromList()
	{
		init();
		this.repaint();
	}
	
}
