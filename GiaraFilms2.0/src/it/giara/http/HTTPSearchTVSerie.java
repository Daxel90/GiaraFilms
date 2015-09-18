package it.giara.http;

import it.giara.schede.PreSchedaTVSerie;
import it.giara.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPSearchTVSerie
{
  public PreSchedaTVSerie scheda;
  boolean advanceSearch = false;

  static
  {
    System.setProperty("http.agent", "PoWeR-Script");
  }

  public HTTPSearchTVSerie(String search)
  {
    Log.log(Log.FILMINFO, "cerco: -" + search + "-");
    this.scheda = new PreSchedaTVSerie();
    try {
      getFileInfo(search);
    }
    catch (Exception e) {
      Log.stack(Log.NET, e);
    }

    if (this.scheda == null)
      Log.log(Log.FILMINFO, search + " NON TROVATO");
  }

  private void getFileInfo(String title)
    throws IOException, JSONException
  {
    URL url = new URL("http://www.comingsoon.it/serietv/ricerca/?titolo=" + title.replace(" ", "%20"));
    URLConnection conn = url.openConnection();
    conn.connect();
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));

    int l = 0;
    int find = -1;
    String line;
    while ((line = rd.readLine()) != null)
    {
      String line;
      l++;
      if (line.contains("<!-- /Component SectionContentsbox: MOVIE list -->"))
      {
        find = l;
      }
      if ((find != -1) && (l == find + 6))
      {
        if (!line.contains("<a href="))
        {
          if (!this.advanceSearch)
            getFileInfoAdvanced(title);
          return;
        }
        this.scheda.link = ("http://www.comingsoon.it" + line.trim().split("'")[1]);
        Log.log(Log.FILMINFO, this.scheda.link);
      }
      if ((find != -1) && (l == find + 7))
      {
        this.scheda.smallImage = line.trim().split("'")[3];
        Log.log(Log.FILMINFO, this.scheda.smallImage);
      }
      if ((find != -1) && (l == find + 10))
      {
        this.scheda.Titolo = StringEscapeUtils.unescapeHtml4(line.trim().replace("<h3>", "").replace("</h3>", ""));
        Log.log(Log.FILMINFO, this.scheda.Titolo);
      }
      if ((find != -1) && (l == find + 13))
      {
        this.scheda.Generi = line.trim().replace("<li><span>GENERE</span>:", "").replace("</li>", "").trim().split(", ");
        Log.log(Log.FILMINFO, this.scheda.Generi[0]);
      }
      if ((find != -1) && (l == find + 14))
      {
        this.scheda.anno = Integer.parseInt(line.trim().replace("<li><span>ANNO</span>:", "").replace("</li>", "").trim());
        Log.log(Log.FILMINFO, Integer.valueOf(this.scheda.anno));
      }
      if ((find == -1) || (l != find + 15))
        continue;
      this.scheda.nazionalita = StringEscapeUtils.unescapeHtml4(line.trim().replace("<li><span>NAZIONALITA'</span>:", "").replace("</li>", "").trim());
      Log.log(Log.FILMINFO, this.scheda.nazionalita);
    }

    rd.close();
  }

  public void getFileInfoAdvanced(String title)
    throws IOException, JSONException
  {
    this.advanceSearch = true;
    String result = "";
    URL url = new URL("https://www.googleapis.com/customsearch/v1element?key=AIzaSyCVAXiUzRYsML1Pv6RwSG1gunmMikTzQqY&num=10&hl=it5&cx=001228879720830619245:WMX2023538107&q=" + title.replace(" ", "%20"));
    URLConnection conn = url.openConnection();
    conn.connect();
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
    String line;
    while ((line = rd.readLine()) != null)
    {
      String line;
      result = result + line;
    }
    rd.close();
    JSONObject json = new JSONObject(result);

    if (json.getJSONArray("results").length() < 1)
    {
      this.scheda = null;
      return;
    }

    int x = -1;
    for (int k = 0; k < json.getJSONArray("results").length() - 1; k++)
    {
      if (!json.getJSONArray("results").getJSONObject(k).has("perResultLabels")) {
        continue;
      }
      if (!json.getJSONArray("results").getJSONObject(k).getJSONArray("perResultLabels").getJSONObject(0).getString("label").trim().equals("serie_tv"))
        continue;
      x = k;
      break;
    }

    if (x == -1)
    {
      this.scheda = null;
      return;
    }

    String Title = "";
    String[] pt = json.getJSONArray("results").getJSONObject(x).getString("titleNoFormatting").split("-");

    for (int l = 0; l < pt.length - 1; l++)
    {
      Title = Title + pt[l] + "-";
    }
    Title = Title.substring(0, Title.length() - 1).trim();
    if (Title.length() < 1)
    {
      this.scheda = null;
      return;
    }

    getFileInfo(Title);
  }
}