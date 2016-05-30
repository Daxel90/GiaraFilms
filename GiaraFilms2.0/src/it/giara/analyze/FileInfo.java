package it.giara.analyze;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.MainType;
import it.giara.analyze.enums.QualityAudio;
import it.giara.analyze.enums.QualityVideo;
import it.giara.utils.Log;
import it.giara.utils.StringUtils;

public class FileInfo
{
	public MainType type = MainType.NULL;
	public String Filename;
	public String title;
	public int year = -1;
	
	// SerieTv
	public int series = 1;
	public int episode = -1;
	
	// Tags
	public QualityVideo video = QualityVideo.NULL;
	public QualityAudio audio = QualityAudio.NULL;
	public ArrayList<AddictionalTags> tags = new ArrayList<AddictionalTags>();
	
	public FileInfo(String s, boolean autoParse)
	{
		Filename = s.trim().toLowerCase();
		
		if (autoParse)
		{
			if (Filename.endsWith(".avi") || Filename.endsWith(".mkv") || Filename.endsWith(".mp4"))
				Video();
			else if (Filename.endsWith(".rar") || Filename.endsWith(".zip") || Filename.endsWith(".tar"))
				Archive();
		}
	}
	
	private void Video()
	{
		Log.log(Log.FILEINFO, Filename);
		String n = Filename.replace(".", " ").replace("-", " ").replace("_", " ");
		while (n.contains("  "))
		{
			n = n.replace("  ", " ");
		}
		
		String[] prt = n.split(" ");
		
		boolean markTvSerie = false;
		boolean serieTVAnalized = false;
		for (String s : prt)
		{
			Log.log(Log.FILEINFO, "part:" + s);
			
			// ANALISI PER SERIE TV
			if (StringUtils.containsDigit(s) || (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				Log.log(Log.FILEINFO, "tvserie analisi");
				if (s.matches(".*(\\d+)x(\\d+).*") || s.matches(".*s(\\d+)e(\\d+).*")
						|| s.matches(".*s(\\d+)ep(\\d+).*"))
				{
					Log.log(Log.FILEINFO, "Match1");
					TVSeries();
					serieTVAnalized = true;
					break;
				}
				if ((s.matches(".*e(\\d+).*") || s.matches(".*ep(\\d+).*")) && StringUtils.countAlphabet(s) <= 3)
				{
					Log.log(Log.FILEINFO, "Match2");
					TVSeries();
					serieTVAnalized = true;
					break;
				}
				else if (!markTvSerie && s.matches("(.*[^a-zA-Z]ep[^a-zA-Z].*)|(^ep[^a-zA-Z].*)|(.*[^a-zA-Z]ep$)"))
				{
					Log.log(Log.FILEINFO, "Match3");
					markTvSerie = true;
				}
				
				else if (markTvSerie && s.matches(".*(\\d+).*"))
				{
					Log.log(Log.FILEINFO, "Match4");
					TVSeries();
					serieTVAnalized = true;
					break;
				}
				else
				{
					Log.log(Log.FILEINFO, "Match5");
					markTvSerie = false;
				}
			}
			
		}
		if (!serieTVAnalized)
			Film();
			
	}
	
	private void TVSeries()
	{
		type = MainType.SerieTV;
		// Log.log(Log.FILEINFO, name);
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
		// TODO Features
	}
	
	// FUNCTION
	public void paraseFilmTitle()
	{
		title = "";
		String[] part = Filename.replace(".", " ").replace("-", " ").replace("_", " ").split(" ");
		int i = 0;
		for (String t : part)
		{
			i++;
			if (t.length() == 4 && (t.startsWith("20") || t.startsWith("19")))
			{
				boolean flag = true;
				char[] c = t.toCharArray();
				for (char g : c)
				{
					if (!Character.isDigit(g))
						flag = false;
				}
				if (flag)
				{
					year = Integer.parseInt(t);
					if (year <= 2018)
						break;
				}
			}
			
			if (i > 1 && StringUtils.containTag(t, true))
			{
				break;
			}
			
			if (i > 1 && t.contains("("))
			{
				break;
			}
			
			title += " " + t;
			
		}
		if (title.length() > 0)
			title = title.substring(1);
			
		Log.log(Log.FILEINFO, "Film: " + title);
	}
	
	public void paraseTVSeriesTitle()
	{
		title = "";
		String[] part = Filename.replace(".", " ").replace("-", " ").replace("_", " ").split(" ");
		int i = 0;
		boolean markTvSerie = false;
		int lastPartLength = 0;
		
		for (String s : part)
		{
			i++;
			if (StringUtils.containsDigit(s) || (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				
				if (s.matches(".*(\\d+)x(\\d+).*") || s.matches(".*s(\\d+)ep(\\d+).*")
						|| s.matches(".*s(\\d+)e(\\d+).*"))
				{
					break;
				}
				else if ((s.matches(".*e(\\d+).*") || s.matches(".*ep(\\d+).*")) && StringUtils.countAlphabet(s) <= 3)
				{
					break;
				}
				else if (!markTvSerie && s.matches("(.*[^a-zA-Z]ep[^a-zA-Z].*)|(^ep[^a-zA-Z].*)|(.*[^a-zA-Z]ep$)"))
				{
					markTvSerie = true;
					lastPartLength = s.length();
				}
				else if (markTvSerie && s.matches(".*(\\d+).*"))
				{
					title = title.substring(0, title.length() - lastPartLength - 1);
					break;
				}
				else
				{
					markTvSerie = false;
				}
			}
			
			if (s.length() == 4 && (s.startsWith("20") || s.startsWith("19")))
			{
				boolean flag = true;
				char[] c = s.toCharArray();
				for (char g : c)
				{
					if (!Character.isDigit(g))
						flag = false;
				}
				if (flag)
				{
					year = Integer.parseInt(s);
					if (year <= 2017)
						break;
				}
			}
			
			if (i > 1 && StringUtils.containTag(s, true))
			{
				break;
			}
			
			if (i > 1 && s.contains("("))
			{
				break;
			}
			
			title += " " + s;
			
		}
		if (title.length() > 0)
			title = title.substring(1);
	}
	
	public void paraseTVSeriesEpisode()
	{
		String[] part = Filename.replace(".", " ").replace("-", " ").replace("_", " ").split(" ");
		
		boolean markTvSerie = false;
		
		for (String s : part)
		{
			if (StringUtils.containsDigit(s) || (s.contains("x") || s.contains("e") || s.contains("s")))
			{
				if (s.matches(".*?(\\d+)x(\\d+).*"))
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
				else if (s.matches(".*s(\\d+)e(\\d+).*"))
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
				else if (s.matches(".*s(\\d+)ep(\\d+).*"))
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
				else if (s.matches(".*s(\\d+).*") && StringUtils.countAlphabet(s) <= 2)
				{
					Pattern p = Pattern.compile(".*s(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find())
					{
						series = Integer.parseInt(m.group(1));
					}
				}
				else if (s.matches(".*e(\\d+).*") && StringUtils.countAlphabet(s) <= 2)
				{
					Pattern p = Pattern.compile(".*e(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find())
					{
						episode = Integer.parseInt(m.group(1));
					}
					break;
				}
				else if (s.matches(".*ep(\\d+).*") && StringUtils.countAlphabet(s) <= 3)
				{
					Pattern p = Pattern.compile(".*ep(\\d+).*");
					Matcher m = p.matcher(s);
					if (m.find())
					{
						episode = Integer.parseInt(m.group(1));
					}
					break;
				}
				else if (!markTvSerie && s.matches("(.*[^a-zA-Z]ep[^a-zA-Z].*)|(^ep[^a-zA-Z].*)|(.*[^a-zA-Z]ep$)"))
				{
					markTvSerie = true;
				}
				else if (markTvSerie && s.matches("(\\d+)"))
				{
					Pattern p = Pattern.compile("(\\d+)");
					Matcher m = p.matcher(s);
					if (m.find())
					{
						episode = Integer.parseInt(m.group(1));
					}
					break;
				}
				else
				{
					markTvSerie = false;
				}
			}
		}
		Log.log(Log.FILEINFO, "TVSeries: " + title + " Serie: " + series + " Episode:" + episode);
	}
	
	public void parseTags()
	{
		String[] part = Filename.replace(".", " ").replace("-", " ").replace("_", " ").split(" ");
		float topVideoQuality = 0;
		float topAudioQuality = 0;
		for (String s : part)
		{
			String t = s.toLowerCase();
			
			for (QualityVideo q : QualityVideo.values())
			{
				if (q == QualityVideo.NULL)
					continue;
				String tag = q.tag.toLowerCase();
				if (t.contains(tag))
				{
					int index = t.indexOf(tag);
					char[] strdata = t.toCharArray();
					char pre = '.';
					char post = '.';
					if ((strdata.length - 1) > index + tag.length())
						post = strdata[index + tag.length()];
					if ((strdata.length - 1) > index - 1 && (index - 1) >= 0)
						pre = strdata[index - 1];
						
					if (!Character.isLetter(pre) && !Character.isLetter(post))
					{
						if (topVideoQuality < q.qualita)
						{
							topVideoQuality = q.qualita;
							video = q;
						}
					}
				}
			}
			
			for (QualityAudio q : QualityAudio.values())
			{
				if (q == QualityAudio.NULL)
					continue;
				String tag = q.tag.toLowerCase();
				if (t.contains(tag))
				{
					int index = t.indexOf(tag);
					char[] strdata = t.toCharArray();
					char pre = '.';
					char post = '.';
					if ((strdata.length - 1) > index + tag.length())
						post = strdata[index + tag.length()];
					if ((strdata.length - 1) > index - 1 && (index - 1) >= 0)
						pre = strdata[index - 1];
						
					if (!Character.isLetter(pre) && !Character.isLetter(post))
					{
						if (topAudioQuality < q.qualita)
						{
							topAudioQuality = q.qualita;
							audio = q;
						}
					}
				}
			}
			
			for (AddictionalTags q : AddictionalTags.values())
			{
				String tag = q.tag.toLowerCase();
				if (t.contains(tag))
				{
					int index = t.indexOf(tag);
					char[] strdata = t.toCharArray();
					char pre = '.';
					char post = '.';
					if ((strdata.length - 1) > index + tag.length())
						post = strdata[index + tag.length()];
					if ((strdata.length - 1) > index - 1 && (index - 1) >= 0)
						pre = strdata[index - 1];
						
					if (!Character.isLetter(pre) && !Character.isLetter(post))
					{
						tags.add(q);
					}
				}
			}
		}
	}
}
