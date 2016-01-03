package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.Settings;
import it.giara.utils.DirUtils;

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
		this.removeAll();
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Opzioni</html>");
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
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/home.png"),
				ImageUtils.getImage("gui/home_over.png"), ImageUtils.getImage("gui/home_over.png"), OpenHomePage);
		homePage.setBounds(40, 5, 32, 32);
		homePage.setToolTipText("Home");
		this.add(homePage);
		
		JLabel downloadDirLabel = new JLabel();
		downloadDirLabel.setBounds(50, 50, FRAME_WIDTH / 2, 30);
		downloadDirLabel.setText("<html><h3>Cartella in cui scaricare i file:</html>");
		this.add(downloadDirLabel);
		
		final JTextField downloadDir = new JTextField();
		downloadDir.setBounds(50, 85, FRAME_WIDTH / 2 - 50, 35);
		downloadDir.setText(Settings.getParameter("downloadfolder"));
		downloadDir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				File f = new File(downloadDir.getText());
				Settings.setParameter("downloadfolder", f.getAbsolutePath());
				DirUtils.downloadDir = f;
			}
		});
		this.add(downloadDir);
		
		JButton downloadDirButt = new JButton("...");
		downloadDirButt.setBounds(FRAME_WIDTH / 2, 85, 50, 35);
		downloadDirButt.setToolTipText("Cambia Cartella");
		downloadDirButt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Scegli la cartella di download");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					Settings.setParameter("downloadfolder", chooser.getSelectedFile().getAbsolutePath());
					DirUtils.downloadDir = chooser.getSelectedFile();
					downloadDir.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		this.add(downloadDirButt);
		
		final JCheckBox log = new JCheckBox();
		log.setText("Salvataggio del file Log");
		log.setSelected(Settings.prop.getBoolean("Savelog", false));
		log.setBounds(50, 130, FRAME_WIDTH / 2, 30);
		log.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.prop.putBoolean("Savelog", log.isSelected());
			}
		});
		
		this.add(log);
		
		final JCheckBox scanService = new JCheckBox();
		scanService.setText("ScanService (sperimentale) richiede riavvio");
		scanService.setSelected(Settings.getParameter("scanservice").equals("1"));
		scanService.setBounds(50, 170, FRAME_WIDTH / 2, 30);
		scanService.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(scanService.isSelected())
					Settings.setParameter("scanservice", "1");
				else
					Settings.setParameter("scanservice", "0");
			}
		});
		
		this.add(scanService);
		
	}
	
	Runnable BackGui = new Runnable()
	{
		@Override
		public void run()
		{
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
}
