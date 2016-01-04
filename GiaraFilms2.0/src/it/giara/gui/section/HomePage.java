package it.giara.gui.section;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import it.giara.analyze.enums.MainType;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.AnimatedImageButton;
import it.giara.gui.components.ImageButton;
import it.giara.gui.components.home.HomeListPanel;
import it.giara.gui.components.home.LateralDrag;
import it.giara.gui.components.home.NewsPanel;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.ScanService;

public class HomePage extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public JTextField searchTx;
	
	public static AnimatedImageButton sync = null; // static for using same
													// instance in all section
	
	LateralDrag lateralDrag;
	NewsPanel news;
	HomeListPanel homelist;
	
	public HomePage()
	{
		super();
		if (sync != null)
			sync.updateRunnable(OpenSync);
		
		lateralDrag = new LateralDrag(this);
		news = new NewsPanel();
		homelist = new HomeListPanel();
		homelist.setVisible(false);
	}
	
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
		search.setToolTipText("Cerca");
		this.add(search);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/options.png"),
				ImageUtils.getImage("gui/options_over.png"), ImageUtils.getImage("gui/options_over.png"), OpenOptions);
		options.setBounds(5, 5, 32, 32);
		options.setToolTipText("Opzioni");
		this.add(options);
		
		ImageButton downloads = new ImageButton(ImageUtils.getImage("gui/download.png"),
				ImageUtils.getImage("gui/download_over.png"), ImageUtils.getImage("gui/download_over.png"), OpenDownloads);
		downloads.setBounds(40, 5, 32, 32);
		downloads.setToolTipText("Downloads");
		this.add(downloads);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(75, 5, 32, 32);
		homePage.setToolTipText("Home");
		this.add(homePage);
		
		if (sync == null)
			sync = new AnimatedImageButton("sync(n)", 5, OpenSync, CheckSync);
		sync.setBounds(110, 5, 32, 32);
		sync.setToolTipText("Scan Service");
		CheckSync.run();
		this.add(sync);
		
		this.content();
	}
	
	public void content()
	{
		lateralDrag.setBounds(-FRAME_WIDTH/5+(FRAME_WIDTH/5)*lateralDrag.progress/20, 40, FRAME_WIDTH/5+16, FRAME_HEIGHT-40);
		this.add(lateralDrag);
		
		news.setBounds(16,56,FRAME_WIDTH-48,FRAME_HEIGHT-56-16);
		this.add(news);
		
		homelist.setBounds(16,56,FRAME_WIDTH-48,FRAME_HEIGHT-56-16);
		homelist.init();
		this.add(homelist);
	}
	
	public void showOnHomepage(AbstractFilmList list, MainType t)
	{
		if(list == null)
		{
			news.setVisible(true);
			homelist.setVisible(false);
		}
		else
		{
			news.setVisible(false);
			homelist.updateAbstractFilmList(list, t);
			homelist.setVisible(true);
		}
		
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
	
	Runnable OpenDownloads = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new Download(guiInstance));
		}
	};
	
	Runnable OpenSync = new Runnable()
	{
		@Override
		public void run()
		{
			MainFrame.getInstance().setInternalPane(new ScanStatus(guiInstance));
		}
	};
	
	Runnable CheckSync = new Runnable()
	{
		@Override
		public void run()
		{
			if (ScanService.scanning)
				sync.setVisible(true);
			else
				sync.setVisible(false);
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

}
