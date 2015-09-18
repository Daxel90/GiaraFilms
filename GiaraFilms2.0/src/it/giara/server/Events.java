package it.giara.server;

import it.giara.analyze.enums.MainType;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;

public class Events
{
  public static void discoverNewFile(String name)
  {
  }

  public static void writeNewFilmPreScheda(PreSchedaFilm scheda)
  {
  }

  public static void writeNewSerieTVPreScheda(PreSchedaTVSerie scheda)
  {
  }

  public static void writeNewFileInfo(int idFile, MainType type, int idScheda)
  {
  }

  public static void writeNewEpisode(int idFile, int IdSerie, int episode, int serie)
  {
  }
}