package it.giara.analyze.enums;

public enum QualityAudio
{
  NULL("Sconosciuta", 0.0F, "null"), MD("ascoltabile", 5.0F, "MD"), LD("decente", 6.0F, "LD"), MP3("buona", 7.0F, "MP3"), AC3("ottima", 9.0F, "AC3"), AAC("ottima", 9.0F, "AAC"), DTS("perfetta", 10.0F, "DTS");

  public String descrizione;
  public float qualita;
  public String tag;

  private QualityAudio(String desc, float quality, String t) { this.tag = t;
    this.descrizione = desc;
    this.qualita = quality;
  }
}