package it.giara.phases.scanservice;

import it.giara.utils.Log;

public class ElaborateRequestService implements Runnable
{
	public static boolean running = false;
	
	/*
		Accepted Commands
		
		C:-:FileName			checkFile
		US:-:type:-:id			update SchedeId
		UF:-:FileName			update File
		
	*/
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start ElaborateRequestService");
		running = true;
		
	}

}
