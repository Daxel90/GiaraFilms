package it.giara.source;

import it.giara.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListLoader
{
  public static ArrayList<SourceChan> sources = new ArrayList();

  public static void loadSources()
  {
    InputStreamReader in = new InputStreamReader(ListLoader.class.getResourceAsStream("/Resource/DataBase.db"));
    BufferedReader input = new BufferedReader(in);
    String server = "";
    try
    {
      String line;
      while ((line = input.readLine()) != null)
      {
        String line = line.trim();
        if (line.startsWith("["))
        {
          continue;
        }

        String[] prt = line.split("=");
        String[] data = line.split("\\*");
        if (Integer.parseInt(prt[0]) == 0)
        {
          server = data[1];
        }
        else
        {
          sources.add(new SourceChan(server, data[0], data[1]));
          Log.log(Log.CONFIG, data[1]);
        }
      }

    }
    catch (IOException e)
    {
      Log.stack(Log.CONFIG, e);
    }
  }
}