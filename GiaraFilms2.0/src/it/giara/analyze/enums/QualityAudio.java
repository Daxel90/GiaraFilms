package it.giara.analyze.enums;

public enum QualityAudio
{
	NULL("Sconosciuta", 0, "null"), MD("registrato al cinema", 5, "MD"), LD("decente", 6, "LD"), MP3("buona", 7, "MP3"),
	AC3("ottima", 10, "AC3"), AAC("ottima", 9, "AAC"), DTS("perfetta", 10, "DTS"), DD5("perfetta", 10, "DD5");
	
	public String descrizione;
	public float qualita;
	public String tag;
	
	QualityAudio(String desc, float quality, String t)
	{
		tag = t;
		descrizione = desc;
		qualita = quality;
	}
	
}