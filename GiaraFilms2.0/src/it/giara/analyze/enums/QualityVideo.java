package it.giara.analyze.enums;

public enum QualityVideo
{
	NULL("Sconosciuta", 0, "null"), CAM("Registrato al cinema", 5, "CAM"), TS("Registrato al cinema", 5.5f, "TS"),
	TELESYNC("Registrato al cinema", 5.5f, "TELESYNC"), SCREENER("Guardabile", 6, "SCREENER"),
	TC("Guardabile", 6, "TC"), R5("Decente", 7, "R5"), DVDSCR("Decente", 7, "DVDSCR"), DVDRip("Buono", 8, "DVDRip"),
	BDRip("Ottimo", 9, "BDRip"), BRRip("Ottimo", 9, "BRRip"), DVDMux("DVDMux", 8, "DVDMux"), PDTV("Buono", 8, "PDTV"),
	SATRip("Buono", 8, "SATRip"), DVBRip("Buono", 8, "DVBRip"), DTTRip("Buono", 8, "DTTRip"),
	WEBRip("Decente", 7, "WEBRip"), WEBDLRip("Decente", 7, "WEBDLRip"), DLRip("Decente", 7, "DLRip"),
	HD720("Ottimo HD", 10, "720p"), HD1080("Ottimo Full HD", 10, "1080p"), HD1080i("Ottimo Full HD", 10, "1080i"), SBS("Ottimo 3D", 10, "SBS");
	
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