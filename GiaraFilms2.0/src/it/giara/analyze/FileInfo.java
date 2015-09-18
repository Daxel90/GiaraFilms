package it.giara.analyze;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.giara.analyze.enums.MainType;
import it.giara.utils.Log;
import it.giara.utils.StringUtils;

public class FileInfo
{
	public MainType type = MainType.NULL;
	public String Filename;
	public String title;
	
	//SerieTv
	public int series = -1;
	public int episode = -1;
	
	
	public FileInfo(String s )
	{
		Filename = s.trim().toLowerCase();
		
		if(Filename.endsWith(".avi") || Filename.endsWith(".mkv") || Filename.endsWith(".mp4"))
			Video();
		else if(Filename.endsWith(".rar") || Filename.endsWith(".zip") || Filename.endsWith(".tar"))
			Archive();
		
	}
	
	private void Video()
	{
		Log.log(Log.FILEINFO, Filename);
		String n = Filename.replace(".", " ");
		while(n.contains("  "))
		{
			n = n.replace("  ", " ");
		}

		String[] prt = n.split(" ");
		
		boolean flagTVSeries = false;
		boolean isFilm = false;
		
		for(String s : prt)
		{
			//ANALISI PER SERIE TV
			if(StringUtils.containsDigit(s) && (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				if(s.matches(".*(\\d+)x(\\d+).*") || s.matches(".*s(\\d+)e(\\d+).*") || s.matches(".*s(\\d+)ep(\\d+).*"))
				{
					TVSeries();
					isFilm = false;
					break;
				}
				else if(s.matches(".*s(\\d+).*"))
				{
					flagTVSeries = true;
				}
				
				if(flagTVSeries && (s.matches(".*e(\\d+).*") || s.matches(".*ep(\\d+).*")))
				{
					TVSeries();
					isFilm = false;
					break;
				}
			}
			else if(StringUtils.containTag(s,false))
			{
				isFilm = true;
			}
		}
		if(isFilm)
			Film();
		
	}
	
	private void TVSeries()
	{
		type = MainType.SerieTV;
//		Log.log(Log.FILEINFO, name);
		paraseTVSeriesTitle();
		paraseTVSeriesEpisode();
	}
	
	private void Film()
	{
		type = MainType.Film;
		paraseFilmTitle();
		
	}
	
	
	
	private void Archive()
	{
		
	}

	
	//FUNCTION
	public void paraseFilmTitle()
	{
		title = "";
		String[] part = Filename.replace(".", " ").split(" ");
		int i = 0;
		for(String t: part)
		{
			i++;
			if(t.length() == 4 && (t.startsWith("20") || t.startsWith("19")))
			{
				boolean flag = true;
				char[] c = t.toCharArray();
				for(char g : c)
				{
					if(!Character.isDigit(g))
						flag = false;
				}
				if(flag)
				{
					if(Integer.parseInt(t) <= 2016)
						break;
				}
			}
			
			if(i > 1 && StringUtils.containTag(t,true))
			{
				break;
			}
			title += " "+t;
			
		}
		if(title.length() >0)
		title = title.substring(1);
		
		Log.log(Log.FILEINFO, "Film: "+title);
	}
	
	public void paraseTVSeriesTitle()
	{
		title = "";
		String[] part = Filename.replace(".", " ").split(" ");
		boolean flagTVSeries = false;
		int i = 0;
		for(String s: part)
		{
			i++;
			if(StringUtils.containsDigit(s) && (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				if(s.matches(".*(\\d+)x(\\d+).*") || s.matches(".*s(\\d+)ep(\\d+).*") || s.matches(".*s(\\d+)e(\\d+).*"))
				{
					break;
				}
				else if(s.matches(".*s(\\d+).*"))
				{
					flagTVSeries = true;
				}
				else if(flagTVSeries && (s.matches(".*e(\\d+).*") || s.matches(".*ep(\\d+).*")))
				{
					break;
				}
			}
			
			if(s.length() == 4 && (s.startsWith("20") || s.startsWith("19")))
			{
				boolean flag = true;
				char[] c = s.toCharArray();
				for(char g : c)
				{
					if(!Character.isDigit(g))
						flag = false;
				}
				if(flag)
				{
					if(Integer.parseInt(s) <= 2016)
						break;
				}
			}
			
			if(i > 1 && StringUtils.containTag(s,true))
			{
				break;
			}
			title += " "+s;
			
		}
		if(title.length() >0)
		title = title.substring(1);
	}
	
	public void paraseTVSeriesEpisode()
	{
		String[] part = Filename.replace(".", " ").split(" ");
		boolean flagTVSeries = false;
		
		for(String s: part)
		{
			if(StringUtils.containsDigit(s) && (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				if(s.matches(".*?(\\d+)x(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*?(\\d+)x(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						series = Integer.parseInt(m.group(1));
						episode = Integer.parseInt(m.group(2));
					}
					break;
				}
				else if(s.matches(".*s(\\d+)e(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*s(\\d+)e(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						series = Integer.parseInt(m.group(1));
						episode = Integer.parseInt(m.group(2));
					}
					break;
				}
				else if(s.matches(".*s(\\d+)ep(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*s(\\d+)ep(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						series = Integer.parseInt(m.group(1));
						episode = Integer.parseInt(m.group(2));
					}
					break;
				}
				else if(s.matches(".*s(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*s(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						series = Integer.parseInt(m.group(1));
					}
					flagTVSeries = true;
				}
				else if(flagTVSeries && s.matches(".*e(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*e(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						episode = Integer.parseInt(m.group(1));
					}
					break;
				}
				else if(flagTVSeries && s.matches(".*ep(\\d+).*"))
				{
					Pattern p = Pattern.compile(".*ep(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find()) 
					{
						episode = Integer.parseInt(m.group(1));
					}
					break;
				}
			}
			
		}
		
		Log.log(Log.FILEINFO, "TVSeries: "+title+ " Serie: "+series+" Episode:"+episode);
	}
	
}