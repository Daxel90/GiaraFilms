package it.giara.gui.section;

import javax.swing.JLabel;

public class Search extends HomePage
{
	private static final long serialVersionUID = -5303561242288508484L;
	private String searchText;
	
	public Search(String search)
	{
		super();
		searchText = search;
	}
	
	public void loadComponent()
	{
		super.loadComponent();
		
		searchTx.setText(searchText);
		
		JLabel topText = new JLabel("<html><h3>Sto cercando \"" + searchText + "\"</html>");
		topText.setHorizontalAlignment(JLabel.CENTER);
		topText.setBounds(FRAME_WIDTH / 4, 40, FRAME_WIDTH / 2, 32);
		
		this.add(topText);
		
	}
	
}
