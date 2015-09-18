package it.giara.analyze.enums;

public enum QualityAudio 
{	
	NULL("Sconosciuta",0,"null"), MD("ascoltabile", 5, "MD"), LD("decente",6, "LD"), MP3("buona",7,"MP3"), AC3("ottima",9,"AC3"),AAC("ottima",9,"AAC"),DTS("perfetta",10,"DTS");

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