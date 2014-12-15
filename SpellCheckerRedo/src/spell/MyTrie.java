package spell;

import java.util.ArrayList;

public class MyTrie implements Trie
{
	private int wordCount;
	private int nodeCount;
	private MyNode root;
	
	public MyTrie()
	{
		wordCount = 0;
		nodeCount = 1;
		root = new MyNode();
	}
	@Override
	public void add(String word) 
	{
		MyNode current = root;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int index = c - 'a';
			if(current.children[index] == null)
			{
				current.children[index] = new MyNode();
				nodeCount++;
				current = current.children[index];
			}
			else
			{
				current = current.children[index];
			}
		}
		if(current.frequency == 0)
		{
			wordCount++;
		}
		current.frequency++;
	}

	@Override
	public Node find(String word) 
	{
		MyNode current = root;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			int index = c - 'a';
			if(current.children[index] == null)
			{
				return null;
			}
			else
			{
				current = current.children[index];
			}
		}
		if(current.frequency != 0)
		{
			return current;
		}
		return null;
	}

	@Override
	public int getWordCount() 
	{
		return wordCount;
	}

	@Override
	public int getNodeCount()
	{
		return nodeCount;
	}
	@Override
	public String toString()
	{
		ArrayList<Word> myList = new ArrayList<Word>();
		StringBuilder sb = new StringBuilder();
		MyNode current = root;
		wordGetter(sb, current, myList);
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < myList.size(); i++)
		{
			output.append(myList.get(i).word + " " + myList.get(i).count);
			if(i != myList.size() - 1)
			{
				output.append("\n");
			}
		}
		return output.toString();
	}
	private void wordGetter(StringBuilder sb, MyNode current, ArrayList<Word> myList)
	{
		if(current.frequency != 0)
		{
			String temp = sb.toString();
			Word now = new Word(temp, current.frequency);
			myList.add(now);
		}
		for(int i = 0; i < 26; i++)
		{
			if(current.children[i] != null)
			{
				char c = 'a';
				c += i;
				sb.append(c);
				wordGetter(sb, current.children[i], myList);
			}
		}
		if(sb.length() > 0)
		{
			sb.deleteCharAt(sb.length() - 1);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 27;
		int result = 1;
		result = prime * result + nodeCount;
		result = prime * result + wordCount;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		MyTrie other = (MyTrie) obj;
		if (nodeCount != other.nodeCount)
		{
			return false;
		}
		if (wordCount != other.wordCount)
		{
			return false;
		}
		if(toString().equals(other.toString()))
		{
			return true;
		}
		return false;
	}

	/**
	 * Your trie node class should implement the Trie.Node interface
	 */
	public class MyNode implements Trie.Node
	{
		public int frequency;
		public MyNode[] children;
		
		public MyNode()
		{
			frequency = 0;
			children = new MyNode[26];
		}
		/**
		 * Returns the frequency count for the word represented by the node
		 * 
		 * @return The frequency count for the word represented by the node
		 */
		public int getValue()
		{
			return frequency;
		}
	}
	
	public class Word
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
