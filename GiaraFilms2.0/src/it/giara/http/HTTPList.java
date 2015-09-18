package it.giara.http;

import it.giara.utils.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HTTPList
{
  public ArrayList<String> file = new ArrayList();

  static
  {
    System.setProperty("http.agent", "PoWeR-Script");
  }

  public HTTPList(String site, String search)
  {
    getFileList(site + "?m=1&q=(search)", search.replace(" ", "+"));
  }

  private ArrayList<String> getFileList(String site, String search)
  {
    Log.log(Log.NET, site.replace("(search)", search));
    try {
      URL url = new URL(site.replace("(search)", search));
      URLConnection conn = url.openConnection();
      conn.setRequestProperty("User-Agent", "PoWeR-Script");
      conn.connect();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
      String line;
      while ((line = rd.readLine()) != null)
      {
        if (!line.startsWith("#")) {
          continue;
        }
        String line = line.trim();
        while (line.contains("  ")) {
          line = line.replace("  ", " ");
        }
        String[] info = line.split(" ");
        if (info.length < 4)
          continue;
        String name = info[3];

        if (info.length > 4) {
          for (int j = 4; j < info.length; j++)
          {
            if (info[j].contains("/")) {
              break;
            }
            name = name + " " + info[j];
          }
        }
        if (this.file.contains(name))
          continue;
        this.file.add(name);
        Log.log(Log.NET, name);
        Log.log(Log.NET, Integer.valueOf(this.file.size()));
      }

      rd.close();
    }
    catch (Exception e)
    {
      Log.stack(Log.NET, e);
    }

    return this.file;
  }
}