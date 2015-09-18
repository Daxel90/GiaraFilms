package it.giara.utils;

import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.QualityAudio;
import it.giara.analyze.enums.QualityVideo;
import it.giara.analyze.enums.SyntaxTags;

public class StringUtils
{
  public static boolean containsDigit(String n)
  {
    for (char c : n.toCharArray())
    {
      if (Character.isDigit(c))
      {
        return true;
      }
    }
    return false;
  }

  public static boolean containTag(String s, boolean syn)
  {
    String t = s.toLowerCase();

    for (QualityVideo q : QualityVideo.values())
    {
      if (q == QualityVideo.NULL)
        continue;
      String tag = q.tag.toLowerCase();
      if (!t.contains(tag))
        continue;
      int index = t.indexOf(tag);
      char[] strdata = t.toCharArray();
      char pre = '.';
      char post = '.';
      if (strdata.length - 1 > index + tag.length())
        post = strdata[(index + tag.length())];
      if ((strdata.length - 1 > index - 1) && (index - 1 >= 0)) {
        pre = strdata[(index - 1)];
      }
      if ((!Character.isLetter(pre)) && (!Character.isLetter(post)))
      {
        return true;
      }

    }

    for (QualityAudio q : QualityAudio.values())
    {
      if (q == QualityAudio.NULL)
        continue;
      String tag = q.tag.toLowerCase();
      if (!t.contains(tag))
        continue;
      int index = t.indexOf(tag);
      char[] strdata = t.toCharArray();
      char pre = '.';
      char post = '.';
      if (strdata.length - 1 > index + tag.length())
        post = strdata[(index + tag.length())];
      if ((strdata.length - 1 > index - 1) && (index - 1 >= 0)) {
        pre = strdata[(index - 1)];
      }
      if ((!Character.isLetter(pre)) && (!Character.isLetter(post)))
      {
        return true;
      }

    }

    for (AddictionalTags q : AddictionalTags.values())
    {
      String tag = q.tag.toLowerCase();
      if (!t.contains(tag))
        continue;
      int index = t.indexOf(tag);
      char[] strdata = t.toCharArray();
      char pre = '.';
      char post = '.';
      if (strdata.length - 1 > index + tag.length())
        post = strdata[(index + tag.length())];
      if ((strdata.length - 1 > index - 1) && (index - 1 >= 0)) {
        pre = strdata[(index - 1)];
      }
      if ((!Character.isLetter(pre)) && (!Character.isLetter(post)))
      {
        return true;
      }

    }

    if (syn) {
      for (SyntaxTags q : SyntaxTags.values())
      {
        String tag = q.tag.toLowerCase();
        if (!t.contains(tag))
          continue;
        int index = t.indexOf(tag);
        char[] strdata = t.toCharArray();
        char pre = '.';
        char post = '.';
        if (strdata.length - 1 > index + tag.length())
          post = strdata[(index + tag.length())];
        if ((strdata.length - 1 > index - 1) && (index - 1 >= 0)) {
          pre = strdata[(index - 1)];
        }
        if ((!Character.isLetter(pre)) && (!Character.isLetter(post)))
        {
          return true;
        }
      }
    }

    return false;
  }
}