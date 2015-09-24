package it.giara.gui.section;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.ScanService;
import it.giara.source.ListLoader;

public class ScanStatus extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	
	JLabel list;
	JLabel file;
	JLabel stat;
	JProgressBar listBar;
	JProgressBar fileBar;
	
	public ScanStatus(DefaultGui gui)
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
		
		listBar = new JProgressBar();
		listBar.setMaximum(ListLoader.sources.size());
		listBar.setValue(ScanService.NList);
		listBar.setBounds(FRAME_WIDTH / 6, FRAME_HEIGHT * 3 / 4, FRAME_WIDTH * 2 / 3, 20);
		this.add(listBar);
		
		
		fileBar = new JProgressBar();
		fileBar.setMaximum(ScanService.LSize);
		fileBar.setValue(ScanService.LStatus);
		fileBar.setBounds(FRAME_WIDTH / 6, FRAME_HEIGHT * 3 / 4, FRAME_WIDTH * 2 / 3, 20);
		
		this.add(fileBar);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/back.png"),ImageUtils.getImage("gui/back_over.png"),ImageUtils.getImage("gui/back_over.png"), BackGui);
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
