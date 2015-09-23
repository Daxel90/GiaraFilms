package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class Search extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	private String searchText;
	JTextField searchTx;
	
	public Search(String search)
	{
		super();
		searchText = search;
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		searchTx = new JTextField();
		searchTx.setBounds(FRAME_WIDTH * 3 / 4 - 50, 5, FRAME_WIDTH / 4, 30);
		searchTx.setText(searchText);
		searchTx.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				RunSearch.run();
			}
		});
		this.add(searchTx);
		
		ImageButton search = new ImageButton(ImageUtils.getImage("gui/search.png"),
				ImageUtils.getImage("gui/search_over.png"), ImageUtils.getImage("gui/search_clicked.png"), RunSearch);
		search.setBounds(FRAME_WIDTH - 50, 5, 32, 32);
		this.add(search);
		
		JLabel topText = new JLabel("<html><h3>Sto cercando \"" + searchText + "\"</html>");
		topText.setHorizontalAlignment(JLabel.CENTER);
		topText.setBounds(FRAME_WIDTH / 4, 40, FRAME_WIDTH / 2, 32);
		
		this.add(topText);
		
	}
	
	Runnable RunSearch = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new Search(searchTx.getText()));
		}
	};
	
}
