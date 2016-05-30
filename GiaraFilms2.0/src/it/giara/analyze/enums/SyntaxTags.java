package it.giara.analyze.enums;

public enum SyntaxTags
{
	minus("-"), parenthesis1("("), parenthesis2(")"), parenthesis3("["), parenthesis4("]"), avi("avi"), mkv("mkv");
	
	public String tag;
	
	SyntaxTags(String s)
	{
		tag = s;
	}
	
}
