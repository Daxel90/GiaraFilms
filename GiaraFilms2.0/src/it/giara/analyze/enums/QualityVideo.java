package it.giara.analyze.enums;

public enum QualityVideo
{
  NULL("Sconosciuta", 0.0F, "null"), CAM("Registrato al cinema", 5.0F, "CAM"), TS("Registrato al cinema", 5.5F, "TS"), TELESYNC("Registrato al cinema", 5.5F, "TELESYNC"), 
  SCREENER("Guardabile", 6.0F, "SCREENER"), TC("Guardabile", 6.0F, "TC"), R5("Decente", 7.0F, "R5"), DVDSCR("Decente", 7.0F, "DVDSCR"), DVDRip("Buono", 8.0F, "DVDRip"), BDRip("Ottimo", 9.0F, "BDRip"), 
  BRRip("Ottimo", 9.0F, "BRRip"), DVDMux("DVDMux", 8.0F, "DVDMux"), PDTV("Buono", 8.0F, "PDTV"), SATRip("Buono", 8.0F, "SATRip"), DVBRip("Buono", 8.0F, "DVBRip"), DTTRip("Buono", 8.0F, "DTTRip"), 
  WEBRip("Decente", 7.0F, "WEBRip"), WEBDLRip("Decente", 7.0F, "WEBDLRip"), DLRip("Decente", 7.0F, "DLRip"), HD720("Ottimo HD", 10.0F, "720p"), HD1080("Ottimo Full HD", 10.0F, "1080p");

  public String descrizione;
  public float qualita;
  public String tag;

  private QualityVideo(String desc, float quality, String t) { this.tag = t;
    this.descrizione = desc;
    this.qualita = quality;
  }
}