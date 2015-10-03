package it.giara;

import it.giara.http.HTTPFilmInfo;

public class TEst
{
	public static void main(String[] args)
	{
		HTTPFilmInfo finfo = new HTTPFilmInfo("http://www.comingsoon.it/film/iron-man/853/scheda/");
		finfo.getInfo();
		
	}
}