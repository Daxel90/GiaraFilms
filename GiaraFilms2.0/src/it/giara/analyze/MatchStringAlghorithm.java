package it.giara.analyze;

import java.util.ArrayList;

public class MatchStringAlghorithm
{
	public static double compareStrings(String str1, String str2)
	{
		ArrayList<String> pairs1 = wordLetterPairs(str1.toUpperCase());
		ArrayList<String> pairs2 = wordLetterPairs(str2.toUpperCase());
		int intersection = 0;
		int union = pairs1.size() + pairs2.size();
		for (int i = 0; i < pairs1.size(); i++)
		{
			Object pair1 = pairs1.get(i);
			for (int j = 0; j < pairs2.size(); j++)
			{
				Object pair2 = pairs2.get(j);
				if (pair1.equals(pair2))
				{
					intersection++;
					pairs2.remove(j);
					break;
				}
			}
		}
		return (2.0 * intersection) / union;
	}
	
	private static ArrayList<String> wordLetterPairs(String str)
	{
		ArrayList<String> allPairs = new ArrayList<String>();
		// Tokenize the string and put the tokens/words into an array
		String[] words = str.split("\\s");
		// For each word
		for (int w = 0; w < words.length; w++)
		{
			// Find the pairs of characters
			String[] pairsInWord = letterPairs(words[w]);
			for (int p = 0; p < pairsInWord.length; p++)
			{
				allPairs.add(pairsInWord[p]);
			}
		}
		return allPairs;
	}
	
	private static String[] letterPairs(String str)
	{
		int numPairs = str.length() - 1;
		
		if (numPairs < 0)
			numPairs = 0;
			
		String[] pairs = new String[numPairs];
		for (int i = 0; i < numPairs; i++)
		{
			pairs[i] = str.substring(i, i + 2);
		}
		return pairs;
	}
	
}
