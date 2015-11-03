package it.giara.http;

import it.giara.schede.PreSchedaTVSerie;
import it.giara.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPSearchTVSerie
{
	// http://www.animeclick.it/
	
	static
	{
		System.setProperty("http.agent", "PoWeR-Script");
	}
	
	public PreSchedaTVSerie scheda;
	
	boolean advanceSearch = false;
	
	public HTTPSearchTVSerie(String search)
	{
		Log.log(Log.FILMINFO, "cerco: -" + search + "-");
		scheda = new PreSchedaTVSerie();
		try
		{
			getFileInfo(search);
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		
		if (scheda == null)
			Log.log(Log.FILMINFO, search + " NON TROVATO");
			
	}
	
	private void getFileInfo(String title) throws IOException, JSONException
	{
		URL url = new URL("http://www.comingsoon.it/serietv/ricerca/?titolo=" + title.replace(" ", "%20"));
		final URLConnection conn = url.openConnection();
		conn.connect();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
		String line;
		int l = 0;
		int find = -1;
		while ((line = rd.readLine()) != null)
		{
			l++;
			if (line.contains("<div class=\"box-lista-cinema\">"))
			{
				find = l;
			}
			if (find != -1 && l == find + 1)
			{
				if (!line.contains("<a href="))
				{
					if (!advanceSearch)
						getFileInfoAdvanced(title);
					return;
				}
				scheda.link = "http://www.comingsoon.it" + line.trim().split("\"")[1]; // link
				Log.log(Log.FILMINFO, scheda.link);
			}
			if (find != -1 && l == find + 3)
			{
				scheda.smallImage = line.trim().split("\"")[1];
				Log.log(Log.FILMINFO, scheda.smallImage); // small image
			}
			if (find != -1 && l == find + 6)
			{
				scheda.Titolo = StringEscapeUtils.unescapeHtml4(line.trim().replace("<div class=\"h5 titolo cat-hover-color anim25\">", "").replace("</div>", ""));
				Log.log(Log.FILMINFO, scheda.Titolo); // Titolo
			}
			if (find != -1 && l == find + 10)
			{
				scheda.Generi = line.trim().replace("<li><span>GENERE</span>:", "").replace("</li>", "").trim()
						.split(", ");
				Log.log(Log.FILMINFO, scheda.Generi[0]); // Genere
			}
			if (find != -1 && l == find + 11)
			{
				scheda.anno = Integer
						.parseInt(line.trim().replace("<li><span>ANNO</span>:", "").replace("</li>", "").trim());
				Log.log(Log.FILMINFO, scheda.anno); // Anno
			}
			if (find != -1 && l == find + 12)
			{
				scheda.nazionalita = StringEscapeUtils.unescapeHtml4(
						line.trim().replace("<li><span>NAZIONALITA'</span>:", "").replace("</li>", "").trim());
				Log.log(Log.FILMINFO, scheda.nazionalita); // Nazionalità
			}
			
		}
		rd.close();
		
	}
	
	public void getFileInfoAdvanced(String title) throws IOException, JSONException
	{
		advanceSearch = true;
		String result = "";
		URL url = new URL(
				"https://www.googleapis.com/customsearch/v1element?key=AIzaSyCVAXiUzRYsML1Pv6RwSG1gunmMikTzQqY&num=10&hl=it5&cx=001228879720830619245:WMX2023538107&q="
						+ title.replace(" ", "%20"));
		final URLConnection conn = url.openConnection();
		conn.connect();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
		String line;
		while ((line = rd.readLine()) != null)
		{
			result += line;
		}
		rd.close();
		JSONObject json = new JSONObject(result);
		
		if (json.getJSONArray("results").length() < 1)
		{
			scheda = null;
			return;
		}
		
		int x = -1;
		for (int k = 0; k < json.getJSONArray("results").length() - 1; k++)
		{
			if (!json.getJSONArray("results").getJSONObject(k).has("perResultLabels"))
				continue;
				
			if (json.getJSONArray("results").getJSONObject(k).getJSONArray("perResultLabels").getJSONObject(0)
					.getString("label").trim().equals("serie_tv"))
			{
				x = k;
				break;
			}
		}
		if (x == -1)
		{
			scheda = null;
			return;
		}
		
		String Title = "";
		String[] pt = json.getJSONArray("results").getJSONObject(x).getString("titleNoFormatting").split("-");
		
		for (int l = 0; l < pt.length - 1; l++)
		{
			Title += pt[l] + "-";
		}
		
		if (Title.length() < 1)
		{
			scheda = null;
			return;
		}
		Title = Title.substring(0, Title.length() - 1).trim();
		if (Title.length() < 1)
		{
			scheda = null;
			return;
		}
		
		getFileInfo(Title);
		
	}
	
}
