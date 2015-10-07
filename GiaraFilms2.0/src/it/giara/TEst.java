package it.giara;

import it.giara.http.HTTPTVSerieInfo;

public class TEst
{
	public static void main(String[] args)
	{
		HTTPTVSerieInfo finfo = new HTTPTVSerieInfo("http://www.comingsoon.it/serietv/empire/450/scheda/");
		finfo.getInfo();
		
	}
}