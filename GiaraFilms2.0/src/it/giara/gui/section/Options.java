package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
		
		ImageButton backbt = new ImageButton(ImageUtils.getImage("gui/icon32px/arrow_left.png"),
				ImageUtils.getImage("gui/icon32px/arrow_left_over.png"), ImageUtils.getImage("gui/icon32px/arrow_left_over.png"),
				BackGui);
		backbt.setBounds(5, 5, 32, 32);
		backbt.setToolTipText("Indietro");
		this.add(backbt);
		
		ImageButton homePage = new ImageButton(ImageUtils.getImage("gui/icon32px/home.png"),
				ImageUtils.getImage("gui/icon32px/home_over.png"), ImageUtils.getImage("gui/icon32px/home_over.png"), OpenHomePage);
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
		scanService.setText("ScanService (richiede riavvio)");
		scanService.setSelected(Settings.getBoolean("scanservice"));
		scanService.setBounds(50, 170, FRAME_WIDTH / 2, 30);
		scanService.setToolTipText(
				"<html>Lo ScanService � un algoritmo di scansione in background che permette di analizzare tutte le liste,<br>"
						+ "in modo da tenere in memoria i risultati delle analisi al fine di velocizzare le ricerche successive.<br>"
						+ "In combinazione con la Collaborazione Server, permette di generare una lista completa delle risorse disponibili sulla rete<br>"
						+ "DA DISATTIVARE SE SI NOTANO RALLENTAMENTI</html>");
		scanService.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.setBoolean("scanservice", scanService.isSelected());
			}
		});

		this.add(scanService);
		
		final JCheckBox serverCollaborate = new JCheckBox();
		serverCollaborate.setText("Collaborazione con il server");
		serverCollaborate.setSelected(Settings.getBoolean("servercollaborate"));
		serverCollaborate.setBounds(50, 210, FRAME_WIDTH / 2, 30);
		serverCollaborate.setToolTipText(
				"<html>La collaborazione con il server permette di condividere i dati di analisi dal tuo GiaraFilms con altri utenti,<br>"
						+ " cos� da poter generare un Homepage sempre aggiornata, grazie alla collaborazione di molteplici utenti</html>");
		serverCollaborate.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.setBoolean("servercollaborate", serverCollaborate.isSelected());
			}
		});
		
		this.add(serverCollaborate);
		
		
		final JCheckBox removeCompleted = new JCheckBox();
		removeCompleted.setText("Rimuovi Download Completati");
		removeCompleted.setSelected(Settings.getBoolean("removecompleted"));
		removeCompleted.setBounds(50, 250, FRAME_WIDTH / 2, 30);
		removeCompleted.setToolTipText(
				"<html>Permette di rimuovere automaticamente i download completati dall' elenco</html>");
		removeCompleted.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.setBoolean("removecompleted", removeCompleted.isSelected());
			}
		});
		
		this.add(removeCompleted);
		
		//----
		
		final JTextField maxDownloadN = new JTextField();
		maxDownloadN.setText(Settings.getParameter("downloadlimitN"));
		maxDownloadN.setBounds(FRAME_WIDTH / 2, 295, 30, 20);
		maxDownloadN.addKeyListener(new KeyListener()
		{
			public void keyTyped(KeyEvent e)
			{}
			
			public void keyPressed(KeyEvent e)
			{}
			
			public void keyReleased(KeyEvent e)
			{
				try
				{
					if (maxDownloadN.getText().equals(""))
						return;
					int n = Integer.parseInt(maxDownloadN.getText());
					if (n <= 0 || n > 100)
					{
						maxDownloadN.setText(Settings.getParameter("downloadlimitN"));
					}
					else
					{
						Settings.setParameter("downloadlimitN", "" + n);
					}
				} catch (NumberFormatException ex)
				{
					maxDownloadN.setText(Settings.getParameter("downloadlimitN"));
				}
			}
		});
		
		this.add(maxDownloadN);
		
		final JLabel maxDownloadlabel = new JLabel();
		maxDownloadlabel.setText("Numero Download contemporanei");
		maxDownloadlabel.setBounds(FRAME_WIDTH / 2 + 35, 290, FRAME_WIDTH / 2, 30);
		maxDownloadlabel
				.setToolTipText("<html>Numero di downlaod contemporanei "
						+ "</html>");
		this.add(maxDownloadlabel);
		
		final JCheckBox maxDownload = new JCheckBox();
		maxDownload.setText("Limita Download Contemporanei (richiede riavvio)");
		maxDownload.setSelected(Settings.getBoolean("downloadlimit"));
		maxDownload.setBounds(50, 290, FRAME_WIDTH / 2, 30);
		maxDownload.setToolTipText(
				"<html>Questa funzione permette di limitare i download contemporanei</html>");
		maxDownload.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.setBoolean("downloadlimit", maxDownload.isSelected());
				maxDownloadN.setVisible(maxDownload.isSelected());
				maxDownloadlabel.setVisible(maxDownload.isSelected());
			}
		});
		maxDownloadlabel.setVisible(maxDownload.isSelected());
		maxDownloadN.setVisible(maxDownload.isSelected());
		
		this.add(maxDownload);
		
		
		super.loadComponent();
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
