package it.giara.gui.section;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import it.giara.analyze.enums.MainType;
import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.components.DownloadList;
import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.schede.TMDBScheda;

public class DownloadFilm extends DefaultGui
{
	private static final long serialVersionUID = -1;
	
	DefaultGui back;
	TMDBScheda scheda;
	ArrayList<String[]> lista = new ArrayList<String[]>();
	DownloadList panel;
	JScrollPane scroll;
	
	public DownloadFilm(DefaultGui gui, TMDBScheda s)
	{
		super();
		back = gui;
		scheda = s;
		for (int x : SQLQuery.readFileListBySchedeId(scheda.ID, MainType.Film))
		{
			lista.add(SQLQuery.getFileNameAndSize(x));
		}
		panel = new DownloadList(lista, this);
		
		scroll = new JScrollPane(panel);
		scroll.setBorder(BorderFactory.createEtchedBorder());
		scroll.setFocusable(true);
		scroll.setOpaque(false);
		scroll.setBackground(ColorUtils.Trasparent);
		scroll.getVerticalScrollBar().setUnitIncrement(10);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.addMouseWheelListener(new MouseAdapter()
		{
			public void mouseWheelMoved(MouseWheelEvent evt)
			{
				if (evt.getWheelRotation() == 1)
				{
					int iScrollAmount = evt.getScrollAmount();
					int iNewValue = scroll.getVerticalScrollBar().getValue()
							+ scroll.getVerticalScrollBar().getBlockIncrement() * iScrollAmount;
					if (iNewValue <= scroll.getVerticalScrollBar().getMaximum())
					{
						scroll.getVerticalScrollBar().setValue(iNewValue);
					}
					else
					{
						scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					}
				}
				else if (evt.getWheelRotation() == -1)
				{
					int iScrollAmount = evt.getScrollAmount();
					int iNewValue = scroll.getVerticalScrollBar().getValue()
							- scroll.getVerticalScrollBar().getBlockIncrement() * iScrollAmount;
					if (iNewValue <= scroll.getVerticalScrollBar().getMaximum())
					{
						scroll.getVerticalScrollBar().setValue(iNewValue);
					}
					else
					{
						scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					}
				}
			}
		});
	}
	
	public void loadComponent()
	{
		this.removeAll();
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
		
		Dimension dim = new Dimension(this.getWidth() - 40, this.getHeight() - 100);
		
		panel.setSize(dim);
		panel.setPreferredSize(dim);
		panel.init();
		
		scroll.setBounds(20, 60, this.getWidth() - 40, this.getHeight() - 80);
		scroll.setPreferredSize(new Dimension(this.getWidth() - 40, this.getHeight() - 100));
		
		this.add(scroll);
		
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
			Search.searchService.StopService();
			MainFrame.getInstance().setInternalPane(new HomePage());
		}
	};
	
}