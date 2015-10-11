package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.AnimatedImageButton;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.ScanService;

public class HomePage extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public JTextField searchTx;
	
	public static AnimatedImageButton sync = null; // static for using same
													// instance in all section
	
	public HomePage()
	{
		super();
		if (sync != null)
			sync.updateRunnable(OpenSync);
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
		this.add(search);
		
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/options.png"),
				ImageUtils.getImage("gui/options_over.png"), ImageUtils.getImage("gui/options_over.png"), OpenOptions);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
		
		ImageButton downloads = new ImageButton(ImageUtils.getImage("gui/download.png"),
				ImageUtils.getImage("gui/download_over.png"), ImageUtils.getImage("gui/download_over.png"), OpenDownloads);
		downloads.setBounds(40, 5, 32, 32);
		this.add(downloads);
		
		if (sync == null)
			sync = new AnimatedImageButton("sync(n)", 5, OpenSync, CheckSync);
		sync.setBounds(75, 5, 32, 32);
		CheckSync.run();
		this.add(sync);
		
		this.content();
	}
	
	public void content()
	{
		JLabel avviso = new JLabel();
		avviso.setText("<html><h1>L'Home page è ancora in sviluppo, <br><br>Puoi comunque effettuare ricerche</html>");
		avviso.setBounds(FRAME_WIDTH/4, FRAME_HEIGHT*3/8, FRAME_WIDTH/2, FRAME_HEIGHT/4);
		avviso.setHorizontalAlignment(JLabel.CENTER);
		this.add(avviso);
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

}
