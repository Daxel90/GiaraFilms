package it.giara.gui.section;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.Timer;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadBlock;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class Download extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	Timer timer;
	int progress = 0;
	
	public Download(DefaultGui gui)
	{
		super();
		back = gui;
		timer = new Timer(1000, update);
		timer.start();
	}
	
	public void loadComponent()
	{
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Download</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		int x = -1;
		for (Entry<String, FileSources> data : DownloadManager.AllFile.entrySet())
		{
			x++;
			FileSources file = data.getValue();
			int off = 80 * x;
			
			DownloadBlock downbk = new DownloadBlock(file, this);
			
			downbk.setBounds(10, off + 45, FRAME_WIDTH - 30, 80);
			downbk.setBorder(BorderFactory.createEtchedBorder());
			this.add(downbk);
		}
	}
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
			timer.stop();
			MainFrame.getInstance().setInternalPane(back);
		}
	};
	
	ActionListener update = new ActionListener()
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			for (Component comp : getComponents())
			{
				if (comp instanceof DownloadBlock)
				{
					DownloadBlock dw = (DownloadBlock) comp;
					dw.updateBarData();
				}
			}
		}
		
	};
}
