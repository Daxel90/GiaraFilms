package it.giara.phases;

import it.giara.gui.MainFrame;
import it.giara.gui.section.HomePage;
import it.giara.gui.section.LoadScreen;
import it.giara.gui.section.TOS;
import it.giara.phases.scanservice.ElaborateRequestService;
import it.giara.phases.scanservice.LoadFileService;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.utils.ThreadManager;

public class InitializeRunnable implements Runnable
{
	LoadScreen screen;
	
	public InitializeRunnable(LoadScreen scr)
	{
		screen = scr;
	}
	
	@Override
	public void run()
	{
		if (screen != null)
		{
			screen.bar.setMaximum(5);
			screen.bar.setValue(0);
			screen.textProgress.setText("Mi connetto al Database");
			screen.textProgress.setVisible(true);
		}
		SQL.connect();
		if (screen != null)
		{
			screen.bar.setValue(1);
			screen.textProgress.setText("Carico Impostazioni");
		}
		Settings.init();
		Settings.UpdateFixer();
		if (screen != null)
			screen.textProgress.setText("Inizializzo il ThreadManager");
		ThreadManager.init();
		if (screen != null)
		{
			screen.bar.setValue(2);
			screen.textProgress.setText("Carico le sorgenti");
		}
		ListLoader.loadSources();
		if (screen != null)
		{
			screen.bar.setValue(3);
			screen.textProgress.setText("Verifico aggiornamenti");
		}
		UpdateProgram.checkUpdate(screen);
		if (screen != null)
			screen.textProgress.setText("Riavvio Download Interrotti");
		RecoverDownload.asyncRestartDownload();
		if (screen != null)
			screen.bar.setValue(4);
		if (screen != null)
		{
			screen.textProgress.setText("Verifica Completata");
			screen.bar.setValue(5);
		}
		
		if (Settings.getBoolean("scanservice"))
		{
			if (screen != null)
				screen.textProgress.setText("Avvio ScanService");
			ThreadManager.submitCacheTask(new LoadFileService());
			ThreadManager.submitCacheTask(new ElaborateRequestService());
		}
		
		if (Settings.getBoolean("tos"))
			MainFrame.getInstance().setInternalPane(new HomePage());
		else
			MainFrame.getInstance().setInternalPane(new TOS());
			
	}
	
}
