package it.giara.analyze.enums;

public enum SyntaxTags
{
  minus("-"), avi("avi"), mkv("mkv");

  public String tag;

  private SyntaxTags(String s)
  {
    this.tag = s;
  }
}