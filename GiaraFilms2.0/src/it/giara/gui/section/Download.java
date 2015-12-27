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
	private ImageButton ArrowUp, ArrowDown;
	int offset = 0;
	
	public Download(DefaultGui gui)
	{
		super();
		back = gui;
		timer = new Timer(1000, update);
		timer.start();
		ArrowUp = new ImageButton(ImageUtils.getImage("gui/arrow_up.png"), ImageUtils.getImage("gui/arrow_up_over.png"),
				ImageUtils.getImage("gui/arrow_up_over.png"), RunUp);
		ArrowDown = new ImageButton(ImageUtils.getImage("gui/arrow_down.png"),
				ImageUtils.getImage("gui/arrow_down_over.png"), ImageUtils.getImage("gui/arrow_down_over.png"),
				RunDown);
	}
	
	public void loadComponent()
	{
		this.removeAll();
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Download</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"),
				BackGui);
		backbt.setBounds(5, 5, 32, 32);
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		this.add(homePage);
		
		int x = -1;
		for (Entry<String, FileSources> data : DownloadManager.AllFile.entrySet())
		{
			x++;
			if (x < offset)
			{
				continue;
			}
			FileSources file = data.getValue();
			int off = 80 * (x - offset);
			
			DownloadBlock downbk = new DownloadBlock(file, this);
			
			downbk.setBounds(10, off + 45, FRAME_WIDTH - 70, 80);
			downbk.setBorder(BorderFactory.createEtchedBorder());
			this.add(downbk);
		}
		
		ArrowUp.setBounds(this.getWidth() - 57, 45, 32, 32);
		ArrowUp.setVisible(offset > 0);
		this.add(ArrowUp);
		
		ArrowDown.setBounds(this.getWidth() - 57, this.getHeight() - 42, 32, 32);
		ArrowDown.setVisible((x + 1 - offset) > ((this.getHeight() - 40) / 80));
		this.add(ArrowDown);
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
	
	Runnable OpenHomePage = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new HomePage());
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
	
	Runnable RunUp = new Runnable()
	{
		@Override
		public void run()
		{
			offset -= 2;
			if (offset < 0)
				offset = 0;
			loadComponent();
			repaint();
		}
	};
	
	Runnable RunDown = new Runnable()
	{
		@Override
		public void run()
		{
			offset += 2;
			loadComponent();
			repaint();
		}
	};
}
