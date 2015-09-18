package it.giara;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TEst
{
  public static void main(String[] arg)
  {
    String s = "252x25";

    if (s.matches(".*?(\\d+)x(\\d+).*"))
    {
      Pattern p = Pattern.compile(".*?(\\d+)x(\\d+).*");
      Matcher m = p.matcher(s);
      if (m.find())
      {
        int series = Integer.parseInt(m.group(1));
        int episode = Integer.parseInt(m.group(2));
        System.out.println(series);
        System.out.println(episode);
      }
    }
  }
}