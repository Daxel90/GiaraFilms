package it.giara.analyze.enums;

public enum MainType 
{
	Film(1), SerieTV(2), Cartoni(3), Musica(4), Altro(5), NULL(-1);
	
	public int ID;
	
	private MainType(int a)
	{
		ID = a;
	}
}
