package hangman;

import hangman.EvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangman implements EvilHangmanGame
{	
	public EvilHangman()
	{
		
	}
	
	public Set<String> Dictionary;
	public Map<String, Set<String>> Partition;
	public Set<String> guesses;
	public String gameBoard;
	public boolean correctGuess;
	public int numbers;
	
	/**
	 * Starts a new game of evil hangman using words from <code>dictionary</code>
	 * with length <code>wordLength</code>
	 * 
	 * @param dictionary Dictionary of words to use for the game
	 * @param wordLength Number of characters in the word to guess
	 */
	@Override
	public void startGame(File dictionary, int wordLength)
	{
		Dictionary = new HashSet<String>();
		Partition = new HashMap<String, Set<String>>();
		guesses = new TreeSet<String>();
		StringBuilder sb = new StringBuilder();
		numbers = 0;
		for(int i = 0; i < wordLength; i++)
		{
			sb.append("-");
		}
		gameBoard = sb.toString();
		try 
		{
			Scanner scan = new Scanner(dictionary);
			while(scan.hasNext())
			{
				String word;
				word = scan.next();
				if(word.length() == wordLength & word.matches("[a-zA-Z]+"))
				{
					Dictionary.add(word);
				}
			}
			scan.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	

	/**
	 * Make a guess in the current game.
	 * 
	 * @param guess The character being guessed
	 * @return The set of strings that satisfy all the guesses made so far
	 * in the game, including the guess made in this call. The game could claim
	 * that any of these words had been the secret word for the whole game. 
	 * 
	 * @throws GuessAlreadyMadeException If the character <code>guess</code> 
	 * has already been guessed in this game.
	 */
	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
	{
		correctGuess = false;
		String check = Character.toString(guess);
		if(guesses.contains(check))
		{
			throw new GuessAlreadyMadeException();
		}
		guesses.add(check);
		partition(guess);
		return Dictionary;
	}
	
	private void partition(char guess)
	{
		for(String word : Dictionary)
		{
			String result = matcher(word, guess);
			if(Partition.containsKey(result))
			{
				Partition.get(result).add(word);
			}
			else
			{
				Set<String> words = new HashSet<String>();
				words.add(word);
				Partition.put(result, words);
			}
		}
		replace();
	}
	
	private void replace()
	{
		Set<String> value = new HashSet<String>();
		String key = "";
		for(Map.Entry<String, Set<String>> temp : Partition.entrySet())
		{
			if(temp.getValue().size() >= value.size())
			{
				if(temp.getValue().size() == value.size())
				{
					if(tieBreaker(key, temp.getKey()))
					{
						key = temp.getKey();
						value = temp.getValue();
					}
				}
				else
				{
					key = temp.getKey();
					value = temp.getValue();
				}
			}
		}
		if(newChar(key))
		{
			correctGuess = true;
			update(key);
		}
		Dictionary = value;
		Partition.clear();
	}
	
	private boolean tieBreaker(String Old, String New)
	{
		if(letters(Old) < letters(New))
		{
			return false;
		}
		if(letters(Old) > letters(New))
		{
			return true;
		}
		if(furthest(Old, New))
		{
			return true;
		}
		return false;
	}
	
	private boolean furthest(String one, String two)
	{
		for(int i = one.length() - 1; i >= 0; i--)
		{
			if(one.charAt(i) != '-')
			{
				if(two.charAt(i) == '-')
				{
					return false;
				}
			}
			else if(two.charAt(i) != '-')
			{
				return true;
			}
		}
		return false;
	}
	
	private int letters(String word)
	{
		int number = 0;
		for(int i = 0; i < word.length(); i++)
		{
			if(word.charAt(i) != '-')
			{
				number++;
			}
		}
		return number;
	}
	
	private void update(String word)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < word.length(); i++)
		{
			if(word.charAt(i) != '-')
			{
				sb.append(word.charAt(i));
			}
			else
			{
				sb.append(gameBoard.charAt(i));
			}
		}
		gameBoard = sb.toString();
	}
	
	private boolean newChar(String word)
	{
		boolean temp = false;
		for(int i = 0; i < word.length(); i++)
		{
			if(word.charAt(i) != '-')
			{
				numbers++;
				temp = true;
			}
		}
		return temp;
	}
	
	private String matcher(String word, char guess)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < word.length(); i++)
		{
			if(word.charAt(i) == guess)
			{
				sb.append(guess);
			}
			else
			{
				sb.append("-");
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.out.println("Not Enough Input");
			return;
		}
		File file = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int chances = Integer.parseInt(args[2]);
		EvilHangman game = new EvilHangman();
		game.startGame(file, wordLength);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int remaining = wordLength;
		while(chances != 0)
		{
			System.out.println("You have " + chances + " guesses left");
			StringBuilder sb = new StringBuilder();
			for(String letter : game.guesses)
			{
				sb.append(letter + " ");
			}
			System.out.println("Used Letters: " + sb.toString());
			System.out.println("Word: " + game.gameBoard);
			System.out.println("Enter guess: ");
			try 
			{
				String guess = br.readLine();
				while(!guess.matches("[a-zA-Z]"))
				{
					System.out.println("\nInvalid Input\n");
					System.out.println("Enter Guess: ");
					guess = br.readLine();
				}
				guess = guess.toLowerCase();
				char c = guess.charAt(0);
				if(game.guesses.contains(guess))
				{
					System.out.println(guess + " was alread guessed");
				}
				else
				{
					try 
					{
						game.makeGuess(c);
						if(!game.correctGuess)
						{
							chances--;
							System.out.println("Sorry, there are no " + guess + "\'s");
						}
						else
						{
							remaining -= game.numbers;
							System.out.println("Yes, there are " + game.numbers + " " + guess);
						}
						game.numbers = 0;
					} 
					catch (GuessAlreadyMadeException e) 
					{
						e.printStackTrace();
					}
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if(remaining == 0)
			{
				System.out.println("You win!!");
				System.out.println("The word was: " + game.gameBoard);
				return;
			}
		}
		System.out.println("You Lose!");
		Iterator<String> iter = game.Dictionary.iterator();
		String word = iter.next();
		System.out.println("The word was: " + word);
	}
}
