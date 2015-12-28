package it.giara.gui.components.home;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.giara.gui.components.ImageButton;
import it.giara.gui.utils.ColorUtils;
import it.giara.gui.utils.ImageUtils;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.ThreadManager;

public class LateralDrag extends JPanel
{
	private static final long serialVersionUID = -5790293229312148746L;
	
	public int progress = 0;
	public boolean out = false;
	public boolean inUse = false;
	ImageButton letOut;
	ImageButton letIn;
	JLabel allFilm;
	JLabel sep3;
	public LateralDrag()
	{
		setLayout(null);
		setOpaque(false);
		setBackground(ColorUtils.Back);
		
		letOut = new ImageButton(ImageUtils.getImage("gui/drag_right.png"),
				ImageUtils.getImage("gui/drag_right_over.png"), ImageUtils.getImage("gui/drag_right_over.png"),
				RunLetOut);
		letIn = new ImageButton(ImageUtils.getImage("gui/drag_left.png"), ImageUtils.getImage("gui/drag_left_over.png"),
				ImageUtils.getImage("gui/drag_left_over.png"), RunLetIn);
		
		allFilm = new JLabel();
		allFilm.setText("<html> <h3> Tutti i Film</html>");
		allFilm.setHorizontalAlignment(JLabel.CENTER);
		allFilm.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		
		sep3 = new JLabel();
		sep3.setBorder(BorderFactory.createLineBorder(ColorUtils.Separator));
		init();
	}
	
	public void init()
	{
		this.removeAll();
		
		letOut.setBounds(this.getWidth() - 16, this.getHeight()/2-16, 16, 32);
		letIn.setBounds(this.getWidth() - 16, this.getHeight()/2-16, 16, 32);
		
		letIn.setVisible(out);
		letOut.setVisible(!out);
		
		this.add(letOut);
		this.add(letIn);
		
		allFilm.setBounds(0, 10, this.getWidth() - 16, 30);
		this.add(allFilm);
		
		sep3.setBounds(this.getWidth() - 16, 0, 1, this.getHeight());
		this.add(sep3);
		
	}
	
	@Override
	public void setBounds(int a, int b, int c, int d)
	{
		super.setBounds(a, b, c, d);
		init();
	}
	
	Runnable RunLetOut = new Runnable()
	{	
		@Override
		public void run()
		{
			if(!inUse)
			ThreadManager.submitCacheTask(new Runnable()
			{
				@Override
				public void run()
				{
					inUse = true;
					out = true;
					while(progress < 20)
					{
						progress++;
						FunctionsUtils.sleep(10);
						setBounds(-getWidth()+16+(getWidth()-16)*progress/20, 40, getWidth(), getHeight());
					}
					inUse = false;
					repaint();
				}
			});
		}
	};
	
	Runnable RunLetIn = new Runnable()
	{
		@Override
		public void run()
		{
			if(!inUse)
			ThreadManager.submitCacheTask(new Runnable()
			{
				@Override
				public void run()
				{
					inUse = true;
					out = false;
					while(progress > 0)
					{
						progress--;
						FunctionsUtils.sleep(10);
						setBounds(-getWidth()+16+(getWidth()-16)*progress/20, 40, getWidth(), getHeight());
					}
					inUse = false;
					repaint();
				}
			});
		}
	};
	
}
