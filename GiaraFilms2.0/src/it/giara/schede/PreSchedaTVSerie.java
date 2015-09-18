package it.giara.schede;

public class PreSchedaTVSerie
{
	public String Titolo;
	public String link;
	public String smallImage;
	public int anno;
	public String nazionalita;
	public String[] Generi;
	
	public String getGeneri()
	{
		String result = "";
		if (Generi != null)
			for (String s : Generi)
			{
				result += s.toUpperCase() + ",";
			}
		if (result.endsWith(","))
			result = result.substring(0, result.length() - 1);
		return result;
	}
	
}
