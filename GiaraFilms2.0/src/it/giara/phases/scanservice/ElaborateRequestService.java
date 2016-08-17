package it.giara.phases.scanservice;

import java.util.ArrayList;

import it.giara.analyze.enums.MainType;
import it.giara.syncdata.NewServerQuery;
import it.giara.tmdb.api.TmdbApiLoadSchede;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class ElaborateRequestService implements Runnable
{
	public static boolean running = false;
	public static int command_recived = 0;
	public static int command_checkfile = 0;
	public static int command_update_schede = 0;
	
	/*
	 * Accepted Commands
	 * 
	 * C:-:FileName:-:size checkFile US:-:type:-:id update SchedeId
	 * 
	 */
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start ElaborateRequestService");
		running = true;
		FunctionsUtils.sleep(10000);
		ArrayList<String> commands = NewServerQuery.loadRequestCommand();
		boolean flag = true;
		while (running && commands.size() > 0 && flag)
		{
			if(commands.size() <200)
				flag = false;
			
			for (String command : commands)
			{
				command_recived++;
				
				if (command.contains(":--:"))
				{
					String prx = command.split(":--:")[0];
					if (prx.equals("C"))
					{
						command_checkfile++;
						String filename = command.split(":--:")[1];
						String size = command.split(":--:")[2];
						AnalizeFileService.addFile(filename, size);
					}
					else if (prx.equals("US"))
					{
						command_update_schede++;
						int type = Integer.parseInt(command.split(":--:")[1]);
						int schedeid = Integer.parseInt(command.split(":--:")[2]);
						
						TmdbApiLoadSchede tmapi = new TmdbApiLoadSchede(schedeid, MainType.getMainTypeByID(type));
						NewServerQuery.uploadSchede(tmapi.scheda);
						
					}
				}
			}
			
			commands = NewServerQuery.loadRequestCommand();
		}
		running = false;
		Log.log(Log.INFO, "GiaraFilms finish ElaborateRequestService");
	}
	
	
}
