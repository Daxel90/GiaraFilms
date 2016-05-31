package it.giara.phases.scanservice;

import it.giara.utils.Log;

public class ElaborateRequestService implements Runnable
{
	public static boolean running = false;
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start ElaborateRequestService");
		running = true;
		
	}

}
