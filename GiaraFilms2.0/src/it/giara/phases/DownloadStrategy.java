package it.giara.phases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class DownloadStrategy
{
	public static int nEpisodesSeries(TreeMap<Integer, ArrayList<String[]>> list)
	{
		int max = 0;
		for (Entry<Integer, ArrayList<String[]>> episode : list.entrySet())
		{
			if (max < episode.getKey())
			{
				max = episode.getKey();
			}
		}
		return max;
	}
	
	public static HashMap<String, HashMap<Integer, String>> downloadSeries(TreeMap<Integer, ArrayList<String[]>> list)
	{
		HashMap<String, HashMap<Integer, String>> result = new HashMap<String, HashMap<Integer, String>>();
		
		ArrayList<Integer> Nepisode = new ArrayList<Integer>();
		ArrayList<String> tags = new ArrayList<String>();
		
		for (Entry<Integer, ArrayList<String[]>> episode : list.entrySet())
		{
			Nepisode.add(episode.getKey());
		}
		
		for (int x = 0; x < Nepisode.size(); x++)
		{
			int episode = Nepisode.get(x);
			ArrayList<String[]> files = list.get(episode);
			
			for (String[] name : files)
			{
				String analizedName = name[0].replace(".", " ").replace("-", " ").trim();
				while (analizedName.contains("  "))
				{
					analizedName = analizedName.replace("  ", " ");
				}
				String[] pieces = analizedName.split(" ");
				
				// --
				for (int x2 = 0; x2 < Nepisode.size(); x2++)
				{
					if (Nepisode.get(x2) == episode)
						continue;
						
					ArrayList<String[]> files2 = list.get(Nepisode.get(x2));
					
					for (String[] name2 : files2)
					{
						String analizedName2 = name2[0].replace(".", " ").replace("-", " ").trim();
						
						while (analizedName2.contains("  "))
						{
							analizedName2 = analizedName2.replace("  ", " ");
						}
						String[] pieces2 = analizedName2.split(" ");
						String res = "";
						int count = 0;
						for (int k2 = 0; k2 < pieces2.length; k2++)
						{
							if (pieces2[pieces2.length - k2 - 1].equals(pieces[pieces.length - k2 - 1]))
							{
								res = pieces2[pieces2.length - k2 - 1] + " " + res;
								count++;
							}
							else
							{
								break;
							}
						}
						if (count >= 2)
						{
							if (!tags.contains(res))
							{
								tags.add(res.trim());
							}
						}
					}
				}
			}
		}
		
		for (int x = 0; x < Nepisode.size(); x++)
		{
			int episode = Nepisode.get(x);
			ArrayList<String[]> files = list.get(episode);
			
			for (String[] name : files)
			{
				String analizedName = name[0].replace(".", " ").replace("-", " ").trim();
				while (analizedName.contains("  "))
				{
					analizedName = analizedName.replace("  ", " ");
				}
				analizedName = analizedName.trim();
				
				for (String st : tags)
				{
					if (analizedName.endsWith(st))
					{
						if (!result.containsKey(st))
							result.put(st, new HashMap<Integer, String>());
						result.get(st).put(episode, name[0]);
					}
				}
			}
		}
		
		// for(Entry<String, HashMap<Integer, String>> tag : result.entrySet())
		// {
		// String s = tag.getKey();
		// HashMap<Integer, String> episodes = tag.getValue();
		//
		// Log.log(Log.DB, "--"+s+"--");
		// for(Entry<Integer, String> entry : episodes.entrySet())
		// {
		// int i = entry.getKey();
		// String se = entry.getValue();
		// Log.log(Log.DB, " EP:"+i);
		// Log.log(Log.DB, " "+se);
		// }
		// }
		return result;
	}
	
}
