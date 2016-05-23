package it.giara.gui.section;

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
import it.giara.phases.Settings;
import it.giara.syncdata.ServerQuery;

public class HomePage extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public JTextField searchTx;
	
	public static AnimatedImageButton sync = null; // static for using same
													// instance in all section
	
	LateralDrag lateralDrag;
	NewsPanel news;
	HomeListPanel homelist;
	public static AnimatedImageButton loadingHomePage;
	JLabel Colltext;
	
	public HomePage()
	{
		super();
		if (sync != null)
			sync.updateRunnable(OpenSync);
			
		if (loadingHomePage != null && Settings.getParameter("servercollaborate").equals("1"))
			loadingHomePage.updateRunnable(CheckHome);
			
		lateralDrag = new LateralDrag(this);
		
		Colltext = new JLabel();
		Colltext.setVisible(!Settings.getParameter("servercollaborate").equals("1"));
		
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
		
		if (news == null)
			if (Settings.getParameter("servercollaborate").equals("1"))
			{
				news = new NewsPanel();
				news.setVisible(ServerQuery.newsLoaded);
			}
			
		ImageButton search = new ImageButton(ImageUtils.getImage("gui/icon32px/search.png"),
				ImageUtils.getImage("gui/icon32px/search_over.png"), ImageUtils.getImage("gui/icon32px/search_over.png"), RunSearch);
		search.setBounds(FRAME_WIDTH - 50, 5, 32, 32);
		search.setToolTipText("Cerca");
		this.add(search);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/icon32px/options.png"),
				ImageUtils.getImage("gui/icon32px/options_over.png"), ImageUtils.getImage("gui/icon32px/options_over.png"), OpenOptions);
		options.setBounds(5, 5, 32, 32);
		options.setToolTipText("Opzioni");
		this.add(options);
		
		ImageButton downloads = new ImageButton(ImageUtils.getImage("gui/icon32px/download.png"),
				ImageUtils.getImage("gui/icon32px/download_over.png"), ImageUtils.getImage("gui/icon32px/download_over.png"),
				OpenDownloads);
		downloads.setBounds(40, 5, 32, 32);
		downloads.setToolTipText("Downloads");
		this.add(downloads);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/icon32px/home.png"),
				ImageUtils.getImage("gui/icon32px/home_over.png"), ImageUtils.getImage("gui/icon32px/home_over.png"), OpenHomePage);
		homePage.setBounds(75, 5, 32, 32);
		homePage.setToolTipText("Home");
		this.add(homePage);
		
		if (sync == null)
			sync = new AnimatedImageButton("sync(n)", 5, OpenSync, CheckSync, 500);
		sync.setBounds(110, 5, 32, 32);
		sync.setToolTipText("Scan Service");
		CheckSync.run();
		this.add(sync);
		
		if (Settings.getParameter("servercollaborate").equals("1"))
		{
			if (loadingHomePage == null)
				loadingHomePage = new AnimatedImageButton("loading(n)", 9, null, CheckHome, 100);
				
			loadingHomePage.setVisible(!ServerQuery.newsLoaded);
			loadingHomePage.setBounds((FRAME_WIDTH - 256) / 2, (FRAME_HEIGHT - 128) / 2, 256, 128);
			this.add(loadingHomePage);
		}
		
		this.content();
		if (!(this instanceof Search))
			printBack();
	}
	
	public void printBack()
	{
		super.loadComponent();
	}
	
	public void content()
	{
		lateralDrag.setBounds(-FRAME_WIDTH / 5 + (FRAME_WIDTH / 5) * lateralDrag.progress / 20, 40,
				FRAME_WIDTH / 5 + 16, FRAME_HEIGHT - 40);
		this.add(lateralDrag);
		
		if (Settings.getParameter("servercollaborate").equals("1"))
		{
			news.setBounds(16, 56, FRAME_WIDTH - 48, FRAME_HEIGHT - 56 - 16);
			this.add(news);
		}
		else
		{
			Colltext.setText(
					"<html><h2>Per poter usare l'HomePage, è necessario abilitare la Collaborazione Server</html>");
			Colltext.setHorizontalAlignment(JLabel.CENTER);
			Colltext.setBounds(0, (FRAME_HEIGHT - 128) / 2, FRAME_WIDTH, 128);
			this.add(Colltext);
		}
		
		homelist.setBounds(16, 56, FRAME_WIDTH - 48, FRAME_HEIGHT - 56 - 16);
		homelist.init();
		this.add(homelist);
	}
	
	public void showOnHomepage(AbstractFilmList list, MainType t)
	{
		if (list == null)
		{
			if (news != null)
				news.setVisible(true);
			homelist.setVisible(false);
			Colltext.setVisible(!Settings.getParameter("servercollaborate").equals("1"));
		}
		else
		{
			if (news != null)
				news.setVisible(false);
			homelist.updateAbstractFilmList(list, t);
			homelist.setVisible(true);
			homelist.updateFromList(homelist.show);
			Colltext.setVisible(false);
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
	
	Runnable CheckHome = new Runnable()
	{
		@Override
		public void run()
		{
			if (ServerQuery.newsLoaded)
			{
				loadingHomePage.setVisible(false);
				news.setVisible(true);
				news.setBounds(16, 56, FRAME_WIDTH - 48, FRAME_HEIGHT - 56 - 16);
			}
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
