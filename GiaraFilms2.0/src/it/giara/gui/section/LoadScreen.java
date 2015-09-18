package it.giara.gui.section;

import it.giara.gui.DefaultGui;
import it.giara.gui.MainFrame;
import it.giara.gui.utils.ImageUtils;
import it.giara.phases.UpdateProgram;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.ThreadManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class LoadScreen extends DefaultGui
{
	private static final long serialVersionUID = -5303561242288508484L;
	public LoadScreen instance;
	public JLabel logo;
	public JProgressBar bar;
	public  JLabel textProgress;
	private Timer timer1;
	private Timer timer2;
	public float alpha = 0F;
	private int shift = 0;
	
	public void loadComponent()
	{
		instance = this;
		
		logo = new JLabel();
		logo.setIcon(ImageUtils.getIcon(ImageUtils.alphaSet(ImageUtils.getImage("logo.png"), alpha)));
		logo.setHorizontalAlignment(JLabel.CENTER);
		logo.setBounds(0, -FRAME_HEIGHT*shift/80, FRAME_WIDTH, FRAME_HEIGHT);
		add(logo);
		
	    timer1 = new Timer(20, fade);
	    timer1.setInitialDelay(500);
	    timer1.start();
	    
	    bar = new JProgressBar();
	    bar.setBounds(FRAME_WIDTH/6, FRAME_HEIGHT*3/4, FRAME_WIDTH*2/3, 20);
	    bar.setVisible(false);
	    add(bar);
	    
	    textProgress = new JLabel();
	    textProgress.setBounds(FRAME_WIDTH/6, FRAME_HEIGHT*3/4-30, FRAME_WIDTH*2/3, 20);
	    textProgress.setVisible(false);
	    add(textProgress);
	}
	
	ActionListener fade = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			if(alpha >= 0.9)
			{
				timer1.stop();
			    timer1 = null;
				timer2 = new Timer(20, up);
				timer2.setInitialDelay(100);
				timer2.start();
			}
			else
			{
				alpha+=0.02f;
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
			if(shift >= 10)
			{
				timer2.stop();
			    timer2 = null;
				bar.setVisible(true);
				ThreadManager.submitCacheTask( new Runnable()
				{
					@Override
					public void run()
					{
						bar.setMaximum(3);
						bar.setValue(0);
						textProgress.setText("Carico le sorgenti");
						textProgress.setVisible(true);
						ListLoader.loadSources();
						bar.setValue(1);
						textProgress.setText("Mi connetto al Database");
						SQL.connect();
						bar.setValue(2);
						textProgress.setText("Verifico aggiornamenti");
						UpdateProgram.checkUpdate(instance);
						textProgress.setText("Verifica Completata");
						bar.setValue(3);
						FunctionsUtils.sleep(500);
						MainFrame.getInstance().setInternalPane(new HomePage());
					}
				});
			}
			else
			{
				shift++;
				logo.setBounds(0, -FRAME_HEIGHT*shift/80, FRAME_WIDTH, FRAME_HEIGHT);
				repaint();
			}
		}
	};
}
