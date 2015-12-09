package it.giara.analyze.enums;

public enum MainType
{
	Film(1), SerieTV(2), Archivio(3), Altro(4), NULL(-1);
	
	public int ID;
	
	private MainType(int a)
	{
		ID = a;
	}
	
	public static MainType getMainTypeByID(int ID)
	{
		switch (ID)
		{
			case -1:
				return NULL;
			case 1:
				return Film;
			case 2:
				return SerieTV;
			case 3:
				return Archivio;
			case 5:
				return Altro;
		}
		return NULL;
	}
}
