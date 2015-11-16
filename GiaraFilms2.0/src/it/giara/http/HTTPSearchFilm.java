package it.giara.http;

import it.giara.schede.PreSchedaFilm;
import it.giara.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class HTTPSearchFilm
{
	// http://www.animeclick.it/
	
	static
	{
		System.setProperty("http.agent", "PoWeR-Script");
	}
	
	public PreSchedaFilm scheda;
	
	boolean advanceSearch = false;
	
	public HTTPSearchFilm(String search)
	{
		Log.log(Log.FILMINFO, search);
		scheda = new PreSchedaFilm();
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
		URL url = new URL("http://www.comingsoon.it/film/?titolo=" + title.replace(" ", "%20"));
		final URLConnection conn = url.openConnection();
		conn.connect();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
		String line;
		int l = 0;
		int find = -1; // row number of result table in HTML Page
		while ((line = rd.readLine()) != null)
		{
//			Log.log(Log.FILMINFO, line);
			line = line.trim();
			l++;
			if (line.startsWith("<div class=\"box-lista-cinema\">"))
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
			if (find != -1 && l == find + 13)
			{
				scheda.regia = StringEscapeUtils
						.unescapeHtml4(line.trim().replace("<li><span>REGIA</span>:", "").replace("</li>", "").trim());
				Log.log(Log.FILMINFO, scheda.regia); // Regia
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
			// Log.log(Log.FILMINFO,
			// json.getJSONArray("results").getJSONObject(k).getJSONArray("perResultLabels").getJSONObject(0).getString("label"));
			if (json.getJSONArray("results").getJSONObject(k).getJSONArray("perResultLabels").getJSONObject(0)
					.getString("label").trim().equals("film"))
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
		String Titolo = json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet")
				.getJSONObject("movie").getString("name");
				
		getFileInfo(Titolo);
		
		// scheda.Titolo =
		// json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet").getJSONObject("movie").getString("name");
		// scheda.link =
		// json.getJSONArray("results").getJSONObject(x).getString("url");
		// scheda.smallImage =
		// json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet").getJSONObject("movie").getString("image");
		// scheda.regia =
		// json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet").getJSONObject("movie").getString("director");
		// String[] prt =
		// json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet").getJSONObject("movie").getString("datepublished").split("
		// ");
		// String genere =
		// json.getJSONArray("results").getJSONObject(x).getJSONObject("richSnippet").getJSONObject("movie").getString("genre");
		// scheda.anno = 0;
		// try
		// {
		// scheda.anno = Integer.parseInt(prt[prt.length-1]);
		// }catch(Exception e)
		// {
		// Log.stack(Log.FILMINFO, e);
		// }
		// Log.log(Log.FILMINFO, scheda.Titolo);
		// Log.log(Log.FILMINFO, scheda.link);
		// Log.log(Log.FILMINFO, scheda.smallImage);
		// Log.log(Log.FILMINFO, scheda.anno);
		// Log.log(Log.FILMINFO, scheda.regia);
		// Log.log(Log.FILMINFO, genere);
		
	}
	
}
