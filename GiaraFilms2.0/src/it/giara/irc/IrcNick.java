package it.giara.irc;

import java.util.Random;

public class IrcNick
{
	private static final String[] NICKS = { "spidaboy", "slickerz", "dumpoli", "moeha", "catonia", "pipolipo",
			"omgsize", "toedter", "skyhigh", "rumsound", "mathboy", "shaderz", "poppp", "roofly", "ruloman", "seenthis",
			"tiptopi", "dreamoff", "supergaai", "appeltje", "izidor", "tantila", "artbox", "doedoe", "almari", "sikaru",
			"lodinka", "Giara" };
			
	public String generateRandomNick()
	{
		return NICKS[(int) (Math.random() * NICKS.length)] + randomString(3);
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();
	
	String randomString(int len)
	{
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
}
