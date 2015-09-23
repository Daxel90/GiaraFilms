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

public class HomePage extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public JTextField searchTx;
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		searchTx = new JTextField();
		searchTx.setBounds(FRAME_WIDTH * 3 / 4 - 50, 5, FRAME_WIDTH / 4, 30);
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
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/options.png"),ImageUtils.getImage("gui/options_over.png"),ImageUtils.getImage("gui/options_over.png"), OpenOptions);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		
		
	}
	
	Runnable RunSearch = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new Search(searchTx.getText()));
		}
	};
	
	Runnable OpenOptions = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new Options(guiInstance));
		}
	};
	
}
