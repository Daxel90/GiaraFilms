package it.giara.analyze.enums;

public enum SyntaxTags
{
	minus("-"), parenthesis1("("), parenthesis2(")"), avi("avi"), mkv("mkv");
	
	public String tag;
	
	SyntaxTags(String s)
	{
		tag = s;
	}
	
}
