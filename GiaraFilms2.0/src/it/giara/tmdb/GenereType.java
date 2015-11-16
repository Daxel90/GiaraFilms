package it.giara.tmdb;

import java.util.HashMap;

public enum GenereType
{
	Animation(16, "Animazione"), Adventure(12, "Avventura"), Action(28, "Azione"), Comedy(35, "Commedia"),
	Crime(80, "Crime"), Documentary(99, "Documentario"), Drama(18, "Drammatico"), Family(10751, "Famiglia"),
	Science_Fiction(878, "Fantascienza"), Fantasy(14, "Fantasy"), Foreign(10769, "Foreign"), War(10752, "Guerra"),
	Horror(27, "Horror"), Mystery(9648, "Mistero"), Musical(10402, "Musica"), Romance(10749, "Romance"),
	History(36, "Storico"), Western(37, "Western"), Thriller(53, "Thriller"), TV_Movie(10770, "TV Movie");
	
	private GenereType(int id, String name)
	{
		Id = id;
		Name = name;
	}
	
	public int Id;
	public String Name;
	public static HashMap<Integer, GenereType> map = new HashMap<Integer, GenereType>();
	
	static
	{
		for (GenereType t : GenereType.values())
		{
			map.put(t.Id, t);
		}
	}
	
	public static String getGenereByID(int ID)
	{
		GenereType t = map.get(ID);
		if (t == null)
			return "" + ID;
			
		return t.Name;
	}
	
}
