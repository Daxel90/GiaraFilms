package it.giara.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

public class ChangeLogFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public int FRAME_WIDTH;
	public int FRAME_HEIGHT;
	public JPanel contentPane;
	public DefaultGui internalPane;
	
	public ChangeLogFrame()
	{
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		FRAME_WIDTH = dim.width * 1 / 3;
		FRAME_HEIGHT = dim.height * 2 / 3;
		
		setBounds((dim.width - FRAME_WIDTH) / 2, (dim.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT+50);
		setResizable(true);
		setTitle("ChangeLog");
		setBackground(ColorUtils.Back);
		setLayout(null);
		setIconImage(ImageUtils.getImage("icon.png"));
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT+50);
		contentPane.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT+50));
		contentPane.setLayout(null);
		contentPane.setOpaque(true);
		contentPane.setBackground(ColorUtils.Back);
		setContentPane(contentPane);
		
		JEditorPane text = new JEditorPane();
		text.setEditable(false);
		text.setHighlighter(null);
		text.setOpaque(false);
		text.setContentType("text/html");
		text.setText("<html>"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 15"
				+ "<br><h3>\t- Migliorato algoritmo analisi file"
				+ "<br><h3>\t- Aggiunto Sfondi schede informative"
				+ "<br><h3>\t- Aggiunto Download per Stagioni Complete SerieTv"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 14"
				+ "<br><h3>\t- Fix Caricamento Liste (CloudFlare fa schifo)"
				+ "<br><h3>\t- Funzioni sperimentali per sincronia schede (Non ancora attivati)"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 13"
				+ "<br><h3>\t- Fix download 100% non risulta completato (Da testare)"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 12"
				+ "<br><h3>\t- Vari Fix nei download"
				+ "<br><h3>\t- Auto riconnessione per file in attesa"
				+ "<br><h3>\t- Migliorato algoritmo di connessione"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 11"
				+ "<br><h3>\t- Settato descrizioine di FallBack in Inglese"
				+ "<br><h3>\t- Fix visualizzazione Generi"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 10 BIG UPDATE"
				+ "<br><h3>\t- Cambiato motore di ricerca Film!!"
				+ "<br><h3>\t- Cambiato sistema Database"
				+ "<br><h3>\t- Modificato Score Film (sorgente TMDb)"
				+ "<br><h3>\t- Aggiunto score SerieTv (sorgente TMDb)"
				+ "<br><h3>\t- Rimosso Caricamento per i Dettagli Film e serieTv"
				+ "<br><h3>\t- Migliorato algoritmo riconoscimento file"
				+ "<br><h3>\t- Riabilitato scan service (sperimentale)"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 9"
				+ "<br><h3>\t- Fix per Aggiornamento sito Cooming Soon"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 8"
				+ "<br><h3>\t- Fix Importante Salvataggio file in directory con spazi"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 7"
				+ "<br><h3>\t- Fix del Fix Pausa Download (Dev6)"
				+ "<br><h3>\t- Apportate piccole modifice alle gui"
				+ "<br><h3>\t- Appicati fix sul Database"
				+ "<br><h3>\t- Aggiunto tempo rimanente per i download"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 6"
				+ "<br><h3>\t- Aggiunta dimensione dei file nelle liste"
				+ "<br><h3>\t- Fix sulla riduzione del tempo di attesa (Dev5)"
				+ "<br><h3>\t- Fix Pausa Download"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 5"
				+ "<br><h3>\t- Effettuati alcuni Fix sul Completamento del Download"
				+ "<br><h3>\t- Ridotto tempo di attesa per l'avvio del download"
				+ "<br><h3>\t- Aggiunta dimensione dei file in download"
				+ "<h2>--------------------" + "<h2>Dev PreRelese 4"
				+ "<br><h3>\t- Sistemato errore di rendering nella sezione Download"
				+ "<br><h3>\t- Creata visualizzazione gerarchica per le SerieTV"
				+ "<br><h3>\t- Migliorata gestione delle eccezioni nei download"
				+ "<br><h3>\t- Aggiunta possibilità di eliminare i download e metterli in pausa" 
				+ "<br><h3>\t- Aggiunte opzioni, Tra cui la cartella di download!" 
				+ "<h2>--------------------" + "<h2>Dev PreRelese 3"
				+ "<br><h3>\t- Sospesa esecuzione ScanService per ottimizzare download (in vista di una futura implementazione)"
				+ "<br><h3>\t- Aggiunto bottone download per le serieTv (nel prossimo update, divisione per stagione)"
				+ "<br><h3>\t- Risolto problema slavataggio download in lista"
				+ "<br><h3>\t- Risolto problema nel completamento del download" 
				+ "<br><h3>\t- Aggiunto ChangeLog Frame"
				+ "</html>");
		text.setBorder(BorderFactory.createEmptyBorder());
		text.setBackground(ColorUtils.Back);
		
		JScrollPane scroll = new JScrollPane(text);
		scroll.setFocusable(false);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setBackground(ColorUtils.Back);
		scroll.setBounds(5, 10, FRAME_WIDTH - 25, FRAME_HEIGHT - 50);
		scroll.setOpaque(false);
		contentPane.add(scroll);
		
		JButton butt = new JButton("OK");
		butt.setBounds(FRAME_WIDTH / 4, FRAME_HEIGHT - 40, FRAME_WIDTH / 2, 30);
		butt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
		contentPane.add(butt);
		setVisible(true);
		
	}
	
}
