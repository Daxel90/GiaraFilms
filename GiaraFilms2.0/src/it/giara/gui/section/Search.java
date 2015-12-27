package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import it.giara.gui.MainFrame;
import it.giara.gui.components.FilmListPanel;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.phases.SearchService;

public class Search extends HomePage
{
	private static final long serialVersionUID = -5303561242288508484L;
	private String searchText;
	FilmListPanel panel;
	AbstractFilmList lista = new AbstractFilmList();
	public static SearchService searchService;
	Timer timer;
	JLabel list;
	
	public Search(String search)
	{
		super();
		searchText = search;
		panel = new FilmListPanel(lista,this);
		searchService = new SearchService(lista, searchText);
		RunSearch = new Runnable()
		{
			@Override
			public void run()
			{
				searchService.StopService();
				lista.clear();
				MainFrame.getInstance().setInternalPane(new Search(searchTx.getText()));
			}
		};
		
	}
	
	public void loadComponent()
	{
		super.loadComponent();
		
		searchTx.setText(searchText);
		
		JLabel topText = new JLabel("<html><h3>Sto cercando \"" + searchText + "\"</html>");
		topText.setHorizontalAlignment(JLabel.CENTER);
		topText.setBounds(FRAME_WIDTH / 4, 40, FRAME_WIDTH / 2, 20);
		this.add(topText);
		
		list = new JLabel("<html><h3>" + searchService.endCheckList + " / " + searchService.SizeCheckList
				+ " liste controllate</html>");
		list.setHorizontalAlignment(JLabel.CENTER);
		list.setBounds(FRAME_WIDTH / 4, 60, FRAME_WIDTH / 2, 20);
		this.add(list);
		
		timer = new Timer(500, updateprogression);
		timer.start();
		
		panel.setBounds(20 - 8, 80, FRAME_WIDTH - 40, FRAME_HEIGHT - 100);
		// panel.setBorder(BorderFactory.createLineBorder(Color.white));
		this.add(panel);
		
	}
	
	@Override
	public void content()
	{
	
	}
	
	ActionListener updateprogression = new ActionListener()
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			list.setText("<html><h3>" + searchService.endCheckList + " / " + searchService.SizeCheckList
					+ " liste controllate</html>");
			if (!lista.loading)
				timer.start();
		}
		
	};
	
	Runnable OpenHomePage = new Runnable()
	{
		@Override
		public void run()
		{
			searchService.StopService();
			lista.clear();
			MainFrame.getInstance().setInternalPane(new HomePage());
		}
	};
}
