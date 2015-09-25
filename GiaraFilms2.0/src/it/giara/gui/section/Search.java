package it.giara.gui.section;

import javax.swing.JLabel;

import it.giara.gui.components.FilmListPanel;
import it.giara.gui.utils.AbstractFilmList;

public class Search extends HomePage
{
	private static final long serialVersionUID = -5303561242288508484L;
	private String searchText;
	FilmListPanel panel;
	AbstractFilmList lista = new AbstractFilmList();
	
	public Search(String search)
	{
		super();
		searchText = search;
		panel = new FilmListPanel(lista);
	}
	
	public void loadComponent()
	{
		super.loadComponent();
		
		searchTx.setText(searchText);
		
		JLabel topText = new JLabel("<html><h3>Sto cercando \"" + searchText + "\"</html>");
		topText.setHorizontalAlignment(JLabel.CENTER);
		topText.setBounds(FRAME_WIDTH / 4, 40, FRAME_WIDTH / 2, 32);
		
		this.add(topText);
		
		
		panel.setBounds(20-8, 80, FRAME_WIDTH - 40, FRAME_HEIGHT - 100);
		// panel.setBorder(BorderFactory.createLineBorder(Color.white));
		this.add(panel);
		
	}
	
}
