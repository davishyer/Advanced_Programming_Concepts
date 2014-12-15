package spell;

import java.io.IOException;

import spell.SpellCorrector.NoSimilarWordFoundException;


public class Driver 
{
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException
	{
		String file;
		if(args.length > 0)
		{
			file = args[0];
		}
		else
		{
			file = "/users/guest/d/dhyer3/SpellChecker2/src/test.txt";
		}
		Corrector c = new Corrector();
		c.useDictionary(file);
		System.out.println(c.Dictionary.toString());
		String input;
		if(args.length > 1)
		{
			input = args[1];
		}
		else
		{
			input = "Davis";
		}
		System.out.println("\nSearching for: " + input);
		String output = c.suggestSimilarWord(input);
		System.out.println("\nSuggested Word: " + output);
	}
}