package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.utils.ColorUtils;
import it.giara.phases.Settings;

public class TOS extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	JEditorPane text;
	JScrollPane scroll;
	
	public TOS()
	{
		super();
		
	}
	
	public void loadComponent()
	{
		this.removeAll();
		JLabel title = new JLabel();
		title.setBounds(FRAME_WIDTH / 6, 2, FRAME_WIDTH * 2 / 3, 40);
		title.setText("<html><h1>Termini di utilizzo</html>");
		title.setHorizontalAlignment(JLabel.CENTER);
		this.add(title);
		JLabel sep2 = new JLabel();
		sep2.setBounds(0, 40, FRAME_WIDTH, 1);
		sep2.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		this.add(sep2);
		
		text = new JEditorPane();
		text.setEditable(false);
		text.setHighlighter(null);
		text.setOpaque(false);
		text.setContentType("text/html");
		text.setText(
				"<html><h2> Accettando i seguenti termini di utilizzo tutte le avvertenze riportate nel seguente testo si ritengono condivise ed accettate in ogni punto, senza alcuna remora. <br><br>"
						+ "- GiaraFilms è un progetto open source con licenza GNU senza alcuna finalità di lucro, è pertanto vietata la vendita, e qualsiasi altro tipo di utilizzo che può portare un profitto. <br>"
						+ "- Coloro che hanno realizzato il Software si dissociano dalle eventuali violazioni della legge n.633 del 22 aprile 1941 e successive modifiche sul diritto d' autore.<br>"
						+ "- Poiché alcuni contenuti e informazioni all'interno della rete Web possono in vario modo violare le leggi vigenti, i realizzatori del software avvertono l'utente che il download di alcuni file può costituire violazione di diritti d'autore e copyright."
						+ " Gli autori non si assumono alcuna responsabilità a riguardo, ed avvertono l'utente che l'uso che egli fa di tutto ciò che può scaricare mediante questo software ricade sotto la sua completa ed esclusiva responsabilità.<br>"
						+ "- Al fine di migliorarne l'efficienza, l'utente accetta di consentire al software di inviare e ricevere statistiche ed informazioni in modo anonimo.<br><br>"
						+ "Chiunque non concordi con quanto suscritto è pregato di non utilizzare GiaraFilms."
						+ " </html>");
		text.setBorder(BorderFactory.createEmptyBorder());
		text.setBackground(ColorUtils.Trasparent);
		text.setBounds(30, 50, FRAME_WIDTH - 60, FRAME_HEIGHT - 150);
		
		this.add(text);
		
		JButton accept = new JButton("<html><h2> Accetto i termini</html>");
		accept.setBounds(FRAME_WIDTH * 5 / 8, FRAME_HEIGHT - 110, FRAME_WIDTH / 4, 80);
		accept.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Settings.setBoolean("tos", true);
				MainFrame.getInstance().setInternalPane(new HomePage());
			}
		});
		this.add(accept);
		
		JButton reject = new JButton("<html><h2> NON accetto i termini</html>");
		reject.setBounds(FRAME_WIDTH / 8, FRAME_HEIGHT - 110, FRAME_WIDTH / 4, 80);
		reject.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		this.add(reject);
		super.loadComponent();
	}
	
}
