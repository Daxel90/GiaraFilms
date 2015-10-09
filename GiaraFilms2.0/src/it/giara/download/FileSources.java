package it.giara.download;

import java.util.ArrayList;
import java.util.HashMap;

import it.giara.source.SourceChan;

public class FileSources
{
	public String filename;
	int totalBot = 0;
	
	HashMap<SourceChan, ArrayList<BotPackage>> sourcesBot = new HashMap<SourceChan, ArrayList<BotPackage>>();
	
	public FileSources(String name)
	{
		filename = name;
	}
	
	public void addBot(SourceChan chan, BotPackage bot)
	{
		totalBot++;
		if (!sourcesBot.containsKey(chan))
		{
			sourcesBot.put(chan, new ArrayList<BotPackage>());
		}
		
		sourcesBot.get(chan).add(bot);
	}
	
}
