package it.giara.analyze.enums;

public enum QualityVideo
{
	SBS("Ottimo 3D", 10, "SBS"),
	HD1080("Ottimo Full HD", 10, "1080p"),
	HD1080i("Ottimo Full HD", 10, "1080i"),
	HD720("Ottimo HD", 10, "720p"),
	BDRip("Ottimo", 9, "BDRip"),
	BRRip("Ottimo", 9, "BRRip"),
	HDRip("Ottimo", 9, "HDRip"),
	HDTV("Ottimo", 9, "HDTV"),
	BDMux("Ottimo", 9, "BDMux"),
	DVDRip("Buono", 8, "DVDRip"),
	DVDMux("Buono", 8, "DVDMux"),
	SATRip("Buono", 8, "SATRip"),
	DVBRip("Buono", 8, "DVBRip"),
	DTTRip("Buono", 8, "DTTRip"),
	WEBDLRip("Buono", 8, "WEBDLRip"),
	WEBRip("Buono", 8, "WEBRip"),
	DLRip("Buono", 8, "DLRip"),
	PDTV("Buono", 8, "PDTV"),
	DVDSCR("Decente", 7, "DVDSCR"),
	R5("Decente", 7, "R5"),
	TC("Guardabile", 6, "TC"),
	SCREENER("Guardabile", 6, "SCREENER"),
	HDTS("Registrato al cinema", 5.5f, "HDTS"), 
	TELESYNC("Registrato al cinema", 5.5f, "TELESYNC"),
	TS("Registrato al cinema", 5.5f, "TS"),
	HDCAM("Registrato al cinema", 5, "HDCAM"),
	CAM("Registrato al cinema", 4, "CAM"),
	NULL("Sconosciuta", 0, "null");
	


	
	
	public String descrizione;
	public float qualita;
	public String tag;
	
	QualityVideo(String desc, float quality, String t)
	{
		tag = t;
		descrizione = desc;
		qualita = quality;
	}
	
}