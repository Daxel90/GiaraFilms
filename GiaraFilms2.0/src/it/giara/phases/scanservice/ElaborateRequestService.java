package it.giara.phases.scanservice;

import java.util.ArrayList;

import it.giara.syncdata.NewServerQuery;
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
		
		while (running && commands.size() > 0)
		{
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
						
					}
				}
			}
			
			commands = NewServerQuery.loadRequestCommand();
		}
	}
	
}