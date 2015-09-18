package it.giara.gui.section;

import it.giara.gui.DefaultGui;
import it.giara.gui.utils.ColorUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class HomePage extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		JTextField search = new JTextField();
		search.setBounds(FRAME_WIDTH * 3 / 4 - 50, 5, FRAME_WIDTH / 4, 30);
		
		this.add(search);
	}
}
