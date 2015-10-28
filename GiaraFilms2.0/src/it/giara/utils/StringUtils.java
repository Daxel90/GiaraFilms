package it.giara.utils;

import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.QualityAudio;
import it.giara.analyze.enums.QualityVideo;
import it.giara.analyze.enums.SyntaxTags;

public class StringUtils
{
	
	public static boolean containsDigit(String n)
	{
		for (char c : n.toCharArray())
		{
			if (Character.isDigit(c))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean containTag(String s, boolean syn)
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
					return true;
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
					return true;
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
					return true;
				}
			}
		}
		
		if (syn)
			for (SyntaxTags q : SyntaxTags.values())
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
						
					if (!Character.isLetter(pre) && !Character.isDigit(pre) && !Character.isLetter(post)
							&& !Character.isDigit(post))
					{
						return true;
					}
				}
			}
			
		return false;
	}
	
	public static String humanReadableByteCount(long bytes, int decimal)
	{
		int unit = 1000;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("kMGTPE").charAt(exp - 1) + "";
		return String.format("%." + decimal + "f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static String humanReadableSecondLeft(int second)
	{
		String result = "";
		if (second <= 0)
			return result;
			
		if (second >= 60 * 60)
		{
			result += (second / (60 * 60)) + "h ";
			result += (second % (60 * 60)) / 60 + "min ";
		}
		else if (second >= 60)
		{
			result += (second / 60) + "min ";
			result += (second % 60) + "sec ";
		}
		else
		{
			result += second + "sec ";
		}
		return result;
	}
	
}
