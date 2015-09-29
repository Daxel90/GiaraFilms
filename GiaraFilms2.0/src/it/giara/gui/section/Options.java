package it.giara.gui.section;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class Options extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	
	public Options(DefaultGui gui)
	{
		super();
		back = gui;
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),ImageUtils.getImage("gui/arrow_left_over.png"),ImageUtils.getImage("gui/arrow_left_over.png"), BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
	}
	
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(back);
		}
	};
	
}
