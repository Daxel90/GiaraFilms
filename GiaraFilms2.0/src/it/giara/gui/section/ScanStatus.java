package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.scanservice.AnalizeFileService;
import it.giara.phases.scanservice.LoadFileService;
import it.giara.source.ListLoader;

public class ScanStatus extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	private DefaultGui back;
	private JProgressBar listBar;
	private JProgressBar fileBar;
	private JLabel infoJL = new JLabel();
	
	private Timer timer;
	
	public ScanStatus(DefaultGui gui)
	{
		super();
		back = gui;
	}
	
	public void loadComponent()
	{
		
		this.removeAll();
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Scan Service</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		JLabel listJL = new JLabel();
		listJL.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 1f / 4)-40, FRAME_WIDTH * 2 / 3, 30);
		listJL.setText("<html><h3>Scansione Liste</html>");
		listJL.setHorizontalAlignment(JLabel.CENTER);
		this.add(listJL);
		
		listBar = new JProgressBar();
		listBar.setMaximum(ListLoader.sources.size());
		listBar.setValue(LoadFileService.NList);
		listBar.setStringPainted(true);
		listBar.setString(LoadFileService.NList+" su "+ListLoader.sources.size());
		listBar.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 1f / 4), FRAME_WIDTH * 2 / 3, 20);
		this.add(listBar);
		
		JLabel fileJL = new JLabel();
		fileJL.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 2f / 4)-40, FRAME_WIDTH * 2 / 3, 30);
		fileJL.setText("<html><h3>Caricamento File</html>");
		fileJL.setHorizontalAlignment(JLabel.CENTER);
		this.add(fileJL);
		
		fileBar = new JProgressBar();
		fileBar.setMaximum(LoadFileService.FileSize);
		fileBar.setValue(LoadFileService.FileStatus);
		fileBar.setStringPainted(true);
		fileBar.setString(LoadFileService.FileStatus+" su "+LoadFileService.FileSize);
		fileBar.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 2f / 4), FRAME_WIDTH * 2 / 3, 20);
		if(LoadFileService.loadingList)
		{
			fileBar.setString("Caricamento Lista");
			fileBar.setMaximum(100);
			fileBar.setValue(0);
		}
		this.add(fileBar);
		
		infoJL.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 3f / 4)-40, FRAME_WIDTH * 2 / 3, 100);
		infoJL.setText("<html><h3>File Caricati: "+LoadFileService.TotalFile+"<br>"+
				"File da analizzare: "+AnalizeFileService.pending.size()+"<br>"+
				"File analizzati: "+AnalizeFileService.checked+"<br>"+"</html>");
		this.add(infoJL);
		
		
		
		timer = new Timer(500, UpdateStatus);
		timer.start();
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/icon32px/arrow_left.png"),
				ImageUtils.getImage("gui/icon32px/arrow_left_over.png"),
				ImageUtils.getImage("gui/icon32px/arrow_left_over.png"), BackGui);
		backbt.setBounds(5, 5, 32, 32);
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/icon32px/home.png"),
				ImageUtils.getImage("gui/icon32px/home_over.png"), ImageUtils.getImage("gui/icon32px/home_over.png"),
				OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		homePage.setToolTipText("Home");
		this.add(homePage);
		super.loadComponent();
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
	
	ActionListener UpdateStatus = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			listBar.setMaximum(ListLoader.sources.size());
			listBar.setValue(LoadFileService.NList);
			listBar.setStringPainted(true);
			listBar.setString(LoadFileService.NList+" su "+ListLoader.sources.size());
			
			fileBar.setMaximum(LoadFileService.FileSize);
			fileBar.setValue(LoadFileService.FileStatus);
			fileBar.setStringPainted(true);
			fileBar.setString(LoadFileService.FileStatus+" su "+LoadFileService.FileSize);
			
			if(LoadFileService.loadingList)
			{
				fileBar.setString("Caricamento Lista");
				fileBar.setMaximum(100);
				fileBar.setValue(0);
			}
			
			infoJL.setText("<html><h3>File Caricati: "+LoadFileService.TotalFile+"<br>"+
					"File da analizzare: "+AnalizeFileService.pending.size()+"<br>"+
					"File analizzati: "+AnalizeFileService.checked+"<br>"+"</html>");
		}
	};
	
}
