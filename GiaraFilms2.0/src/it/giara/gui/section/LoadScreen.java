package it.giara.gui.section;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import it.giara.gui.DefaultGui;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.InitializeRunnable;
import it.giara.utils.ThreadManager;

public class LoadScreen extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public LoadScreen instance;
	public JLabel logo;
	public JProgressBar bar;
	public JLabel textProgress;
	private Timer timer1;
	private Timer timer2;
	public float alpha = 0F;
	private int shift = 0;
	boolean visible = false;
	
	public LoadScreen()
	{
		logo = new JLabel();
		timer1 = new Timer(20, fade);
		bar = new JProgressBar();
		textProgress = new JLabel();
	}
	
	public void loadComponent()
	{
		instance = this;
		
		logo.setIcon(ImageUtils.getIcon(ImageUtils.alphaSet(ImageUtils.getImage("logo.png"), alpha)));
		logo.setHorizontalAlignment(JLabel.CENTER);
		logo.setBounds(0, -FRAME_HEIGHT * shift / 80, FRAME_WIDTH, FRAME_HEIGHT);
		add(logo);
		
		if (timer1 != null)
		{
			timer1.setInitialDelay(500);
			timer1.start();
		}
		
		bar.setBounds(FRAME_WIDTH / 6, FRAME_HEIGHT * 3 / 4, FRAME_WIDTH * 2 / 3, 20);
		bar.setVisible(visible);
		add(bar);
		
		textProgress.setBounds(FRAME_WIDTH / 6, FRAME_HEIGHT * 3 / 4 - 30, FRAME_WIDTH * 2 / 3, 20);
		textProgress.setVisible(visible);
		add(textProgress);
		
		super.loadComponent();
	}
	
	ActionListener fade = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (alpha >= 0.9)
			{
				timer1.stop();
				timer1 = null;
				timer2 = new Timer(20, up);
				timer2.setInitialDelay(100);
				timer2.start();
			}
			else
			{
				alpha += 0.02f;
				logo.setIcon(ImageUtils.getIcon(ImageUtils.alphaSet(ImageUtils.getImage("logo.png"), alpha)));
				repaint();
			}
		}
	};
	
	ActionListener up = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (shift >= 10)
			{
				timer2.stop();
				timer2 = null;
				bar.setVisible(true);
				visible = true;
				ThreadManager.submitCacheTask(new InitializeRunnable(instance));
			}
			else
			{
				shift++;
				logo.setBounds(0, -FRAME_HEIGHT * shift / 80, FRAME_WIDTH, FRAME_HEIGHT);
				repaint();
			}
		}
	};
}
