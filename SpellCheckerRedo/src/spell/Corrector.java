package spell;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Corrector implements SpellCorrector
{
	public MyTrie Dictionary;
	public Corrector()
	{
		Dictionary = new MyTrie();
	}
	@Override
	public void useDictionary(String dictionaryFileName) throws IOException 
	{
		Scanner readIn = new Scanner(new FileReader(dictionaryFileName));
		String word;
		while(readIn.hasNext())
		{
			word = readIn.next();
			if(word.matches("[a-zA-Z]+"));
			{
				Dictionary.add(word);
			}
		}
		readIn.close();
	}

	@Override
	public String suggestSimilarWord(String inputWord)throws NoSimilarWordFoundException
	{
		if(!inputWord.matches("[a-zA-Z]+"))
		{
			throw new NoSimilarWordFoundException();
		}
		if(Dictionary.find(inputWord) != null)
		{
			return inputWord;
		}
		
		ArrayList<String> edits = new ArrayList<String>();
		edits.add(inputWord);
		edit(edits);
		Word winner = new Word("", 0);
		for(int i = 0; i < edits.size(); i++)
		{
			if(Dictionary.find(edits.get(i)) != null)
			{
				MyTrie.MyNode n = (MyTrie.MyNode)Dictionary.find(edits.get(i));
				if(winner.count < n.frequency)
				{
					winner = new Word(edits.get(i), n.frequency);
				}
				else if(winner.count == n.frequency)
				{
					if(winner.word.compareTo(n.toString()) < 0)
					{
						winner = new Word(edits.get(i), n.getValue());
					}
				}
			}
		}
		if(winner.count > 0)
		{
			return winner.word;
		}
		
		edit(edits);
		for(int i = 0; i < edits.size(); i++)
		{
			if(Dictionary.find(edits.get(i)) != null)
			{
				MyTrie.MyNode n = (MyTrie.MyNode)Dictionary.find(edits.get(i));
				if(winner.count < n.frequency)
				{
					winner = new Word(edits.get(i), n.frequency);
				}
				else if(winner.count == n.frequency)
				{
					if(winner.word.compareTo(n.toString()) < 0)
					{
						winner = new Word(edits.get(i), n.getValue());
					}
				}
			}
		}
		if(winner.count > 0)
		{
			return winner.word;
		}
		
		throw new NoSimilarWordFoundException();
	}
	
	private void edit(ArrayList<String> edits)
	{
		int k = edits.size();
		for(int i = 0; i < k; i++)
		{
			deletion(edits.get(i), edits);
			transpostion(edits.get(i), edits);
			alteration(edits.get(i), edits);
			addition(edits.get(i), edits);
		}
	}
	
	private void deletion(String word, ArrayList<String> edits)
	{
		for(int i = 0; i < word.length(); i++)
		{
			StringBuilder sb = new StringBuilder(word);
			sb.deleteCharAt(i);
			edits.add(sb.toString());
		}
	}
	
	private void transpostion(String word, ArrayList<String> edits)
	{
		for(int i = 0; i < word.length() - 1; i++)
		{
			StringBuilder sb = new StringBuilder(word);
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			sb.insert(i + 1, c);
			edits.add(sb.toString());
		}
	}
	
	private void alteration(String word, ArrayList<String> edits)
	{
		for(int i = 0; i < 26; i++)
		{
			char c = 'a';
			c += i;
			for(int j = 0; j < word.length(); j++)
			{
				StringBuilder sb = new StringBuilder(word);
				sb.deleteCharAt(j);
				sb.insert(j, c);
				edits.add(sb.toString());
			}
		}
	}
	
	private void addition(String word, ArrayList<String> edits)
	{
		for(int i = 0; i < 26; i++)
		{
			char c = 'a';
			c += i;
			for(int j = 0; j <= word.length(); j++)
			{
				StringBuilder sb = new StringBuilder(word);
				sb.insert(j, c);
				edits.add(sb.toString());
			}
		}
	}
	
	private class Word
	{
		public String word;
		public int count;
		Word(String w, int c)
		{
			word = w;
			count = c;
		}
	}

}
