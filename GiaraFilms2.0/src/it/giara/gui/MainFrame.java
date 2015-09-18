package it.giara.gui;

import it.giara.gui.section.LoadScreen;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public static MainFrame instance;
	public int FRAME_WIDTH;
	public int FRAME_HEIGHT;
	public JPanel contentPane;
	public DefaultGui internalPane;
	
	public MainFrame()
	{
		instance = this;
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		FRAME_WIDTH = dim.width * 2 / 3;
		FRAME_HEIGHT = dim.height * 2 / 3;
		
		setBounds((dim.width - FRAME_WIDTH) / 2, (dim.height - FRAME_HEIGHT) / 2, FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(true);
		setTitle("GiaraFilms 2.0");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBackground(ColorUtils.Back);
		setLayout(null);
		setIconImage(ImageUtils.getImage("icon.png"));
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		contentPane.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		contentPane.setLayout(null);
		contentPane.setOpaque(true);
		contentPane.setBackground(ColorUtils.Back);
		setContentPane(contentPane);
		
		setInternalPane(new LoadScreen());
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowDeiconified(WindowEvent e)
			{}
		});
		
		addComponentListener(new ComponentListener()
		{
			@Override
			public void componentResized(ComponentEvent arg0)
			{
				FRAME_HEIGHT = getHeight();
				FRAME_WIDTH = getWidth();
				internalPane.init(FRAME_WIDTH, FRAME_HEIGHT);
			}
			
			public void componentHidden(ComponentEvent arg0)
			{}
			
			public void componentMoved(ComponentEvent arg0)
			{}
			
			public void componentShown(ComponentEvent arg0)
			{}
		});
	}
	
	public void setInternalPane(DefaultGui panel)
	{
		if (internalPane != null)
			contentPane.remove(internalPane);
		panel.init(FRAME_WIDTH, FRAME_HEIGHT);
		contentPane.add(panel);
		internalPane = panel;
		repaint();
	}
	
	public static MainFrame getInstance()
	{
		return instance;
		
	}
}
