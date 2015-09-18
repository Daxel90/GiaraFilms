package it.giara.analyze;

import it.giara.analyze.enums.MainType;
import it.giara.utils.Log;
import it.giara.utils.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileInfo
{
  public MainType type = MainType.NULL;
  public String Filename;
  public String title;
  public int series = -1;
  public int episode = -1;

  public FileInfo(String s)
  {
    this.Filename = s.trim().toLowerCase();

    if ((this.Filename.endsWith(".avi")) || (this.Filename.endsWith(".mkv")) || (this.Filename.endsWith(".mp4")))
      Video();
    else if ((this.Filename.endsWith(".rar")) || (this.Filename.endsWith(".zip")) || (this.Filename.endsWith(".tar")))
      Archive();
  }

  private void Video()
  {
    Log.log(Log.FILEINFO, this.Filename);
    String n = this.Filename.replace(".", " ");
    while (n.contains("  "))
    {
      n = n.replace("  ", " ");
    }

    String[] prt = n.split(" ");

    boolean flagTVSeries = false;
    boolean isFilm = false;

    for (String s : prt)
    {
      if ((StringUtils.containsDigit(s)) && ((s.contains("x")) || (s.contains("e")) || (s.contains("s"))))
      {
        if ((s.matches(".*(\\d+)x(\\d+).*")) || (s.matches(".*s(\\d+)e(\\d+).*")) || (s.matches(".*s(\\d+)ep(\\d+).*")))
        {
          TVSeries();
          isFilm = false;
          break;
        }
        if (s.matches(".*s(\\d+).*"))
        {
          flagTVSeries = true;
        }

        if ((!flagTVSeries) || ((!s.matches(".*e(\\d+).*")) && (!s.matches(".*ep(\\d+).*"))))
          continue;
        TVSeries();
        isFilm = false;
        break;
      }

      if (!StringUtils.containTag(s, false))
        continue;
      isFilm = true;
    }

    if (isFilm)
      Film();
  }

  private void TVSeries()
  {
    this.type = MainType.SerieTV;

    paraseTVSeriesTitle();
    paraseTVSeriesEpisode();
  }

  private void Film()
  {
    this.type = MainType.Film;
    paraseFilmTitle();
  }

  private void Archive()
  {
  }

  public void paraseFilmTitle()
  {
    this.title = "";
    String[] part = this.Filename.replace(".", " ").split(" ");
    int i = 0;
    for (String t : part)
    {
      i++;
      if ((t.length() == 4) && ((t.startsWith("20")) || (t.startsWith("19"))))
      {
        boolean flag = true;
        char[] c = t.toCharArray();
        for (char g : c)
        {
          if (!Character.isDigit(g))
            flag = false;
        }
        if ((flag) && 
          (Integer.parseInt(t) <= 2016))
        {
          break;
        }
      }
      if ((i > 1) && (StringUtils.containTag(t, true)))
      {
        break;
      }
      this.title = (this.title + " " + t);
    }

    if (this.title.length() > 0) {
      this.title = this.title.substring(1);
    }
    Log.log(Log.FILEINFO, "Film: " + this.title);
  }

  public void paraseTVSeriesTitle()
  {
    this.title = "";
    String[] part = this.Filename.replace(".", " ").split(" ");
    boolean flagTVSeries = false;
    int i = 0;
    for (String s : part)
    {
      i++;
      if ((StringUtils.containsDigit(s)) && ((s.contains("x")) || (s.contains("e")) || (s.contains("s"))))
      {
        if ((s.matches(".*(\\d+)x(\\d+).*")) || (s.matches(".*s(\\d+)ep(\\d+).*")) || (s.matches(".*s(\\d+)e(\\d+).*")))
        {
          break;
        }
        if (s.matches(".*s(\\d+).*"))
        {
          flagTVSeries = true;
        }
        else if ((flagTVSeries) && ((s.matches(".*e(\\d+).*")) || (s.matches(".*ep(\\d+).*"))))
          {
            break;
          }
      }

      if ((s.length() == 4) && ((s.startsWith("20")) || (s.startsWith("19"))))
      {
        boolean flag = true;
        char[] c = s.toCharArray();
        for (char g : c)
        {
          if (!Character.isDigit(g))
            flag = false;
        }
        if ((flag) && 
          (Integer.parseInt(s) <= 2016))
        {
          break;
        }
      }
      if ((i > 1) && (StringUtils.containTag(s, true)))
      {
        break;
      }
      this.title = (this.title + " " + s);
    }

    if (this.title.length() > 0)
      this.title = this.title.substring(1);
  }

  public void paraseTVSeriesEpisode()
  {
    String[] part = this.Filename.replace(".", " ").split(" ");
    boolean flagTVSeries = false;

    for (String s : part)
    {
      if ((!StringUtils.containsDigit(s)) || ((!s.contains("x")) && (!s.contains("e")) && (!s.contains("s"))))
        continue;
      if (s.matches(".*?(\\d+)x(\\d+).*"))
      {
        Pattern p = Pattern.compile(".*?(\\d+)x(\\d+).*");
        Matcher m = p.matcher(s);
        if (!m.find())
          break;
        this.series = Integer.parseInt(m.group(1));
        this.episode = Integer.parseInt(m.group(2));

        break;
      }
      if (s.matches(".*s(\\d+)e(\\d+).*"))
      {
        Pattern p = Pattern.compile(".*s(\\d+)e(\\d+).*");
        Matcher m = p.matcher(s);
        if (!m.find())
          break;
        this.series = Integer.parseInt(m.group(1));
        this.episode = Integer.parseInt(m.group(2));

        break;
      }
      if (s.matches(".*s(\\d+)ep(\\d+).*"))
      {
        Pattern p = Pattern.compile(".*s(\\d+)ep(\\d+).*");
        Matcher m = p.matcher(s);
        if (!m.find())
          break;
        this.series = Integer.parseInt(m.group(1));
        this.episode = Integer.parseInt(m.group(2));

        break;
      }
      if (s.matches(".*s(\\d+).*"))
      {
        Pattern p = Pattern.compile(".*s(\\d+).*");
        Matcher m = p.matcher(s);
        if (m.find())
        {
          this.series = Integer.parseInt(m.group(1));
        }
        flagTVSeries = true;
      } else {
        if ((flagTVSeries) && (s.matches(".*e(\\d+).*")))
        {
          Pattern p = Pattern.compile(".*e(\\d+).*");
          Matcher m = p.matcher(s);
          if (!m.find())
            break;
          this.episode = Integer.parseInt(m.group(1));

          break;
        }
        if ((!flagTVSeries) || (!s.matches(".*ep(\\d+).*")))
          continue;
        Pattern p = Pattern.compile(".*ep(\\d+).*");
        Matcher m = p.matcher(s);
        if (!m.find())
          break;
        this.episode = Integer.parseInt(m.group(1));

        break;
      }

    }

    Log.log(Log.FILEINFO, "TVSeries: " + this.title + " Serie: " + this.series + " Episode:" + this.episode);
  }
}