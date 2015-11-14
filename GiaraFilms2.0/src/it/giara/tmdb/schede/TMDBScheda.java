package it.giara.tmdb.schede;

public class TMDBScheda
{
	public int ID;
	public String title;
	public String relese;
	public String poster;
	public String back;
	public String desc;
	public int[] genre_ids;
	public double vote;
	
	@Override
	public String toString()
	{
		String result = "ID: " + ID + " title: " + title + " relese: " + relese + " poster: " + poster + " back: "
				+ back +" desc: "+desc+ " vote: "+vote+" genre_ids: ";
				
		for(int a : genre_ids)
		{
			result+= a+",";
		}
		return result;
	}
}
