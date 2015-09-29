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
import it.giara.phases.ScanService;
import it.giara.source.ListLoader;
import it.giara.utils.Log;

public class ScanStatus extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	private DefaultGui back;
	private JLabel list;
	private JLabel file;
	private JLabel stat;
	private JProgressBar listBar;
	private JProgressBar fileBar;
	private Timer timer;
	
	public ScanStatus(DefaultGui gui)
	{
		super();
		back = gui;
		Log.log(Log.DEBUG, gui.getClass());
	}
	
	public void loadComponent()
	{
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 60 - 35, FRAME_WIDTH * 2 / 3, 80);
		title.setText("<html><h1>Scansione File</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		
		stat = new JLabel();
		stat.setBounds(FRAME_WIDTH / 6, FRAME_HEIGHT * 3 / 4 - 35, FRAME_WIDTH * 2 / 3, 80);
		stat.setText("<html><h3>Film: " + ScanService.Nfilm + "<br>Episodi SerieTV: " + ScanService.NEpisode
				+ "<br>File Totali: " + ScanService.Nfile + "</html>");
		this.add(stat);
		
		list = new JLabel();
		list.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 1.5f / 4 - 35), FRAME_WIDTH * 2 / 3, 30);
		list.setText(
				"<html><h3>Controllate " + ScanService.NList + " liste su " + ListLoader.sources.size() + "</html>");
		list.setHorizontalAlignment(JLabel.CENTER);
		this.add(list);
		
		listBar = new JProgressBar();
		listBar.setMaximum(ListLoader.sources.size());
		listBar.setValue(ScanService.NList);
		listBar.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 1.5f / 4), FRAME_WIDTH * 2 / 3, 20);
		this.add(listBar);
		
		file = new JLabel();
		file.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 2.5f / 4 - 35), FRAME_WIDTH * 2 / 3, 30);
		file.setText("<html><h3>Sto scaricando la lista</html>");
		file.setHorizontalAlignment(JLabel.CENTER);
		this.add(file);
		
		fileBar = new JProgressBar();
		fileBar.setMaximum(ScanService.LSize);
		fileBar.setValue(ScanService.LStatus);
		fileBar.setBounds(FRAME_WIDTH / 6, (int) (FRAME_HEIGHT * 2.5f / 4), FRAME_WIDTH * 2 / 3, 20);
		
		this.add(fileBar);
		
		timer = new Timer(500, UpdateStatus);
		timer.start();
		ImageButton options = new ImageButton(ImageUtils.getImage("gui/arrow_left.png"),
				ImageUtils.getImage("gui/arrow_left_over.png"), ImageUtils.getImage("gui/arrow_left_over.png"), BackGui);
		options.setBounds(5, 5, 32, 32);
		this.add(options);
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
	
	ActionListener UpdateStatus = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			list.setText("<html><h3>Controllate " + ScanService.NList + " liste su " + ListLoader.sources.size()
					+ "</html>");
			listBar.setMaximum(ListLoader.sources.size());
			listBar.setValue(ScanService.NList);
			stat.setText("<html><h3>Film: " + ScanService.Nfilm + "<br>Episodi SerieTV: " + ScanService.NEpisode
					+ "<br>File Totali: " + ScanService.Nfile + "</html>");
			if (ScanService.HaveList)
			{
				file.setText("<html><h3>Analizzati " + ScanService.LStatus + " file di " + ScanService.LSize
						+ " della lista corrente</html>");
				fileBar.setMaximum(ScanService.LSize);
				fileBar.setValue(ScanService.LStatus);
			}
			else
			{
				file.setText("<html><h3>Sto scaricando la lista</html>");
				fileBar.setMaximum(0);
				fileBar.setValue(100);
			}
		}
	};
	
}
