package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LineCounterSub extends FileProcessor implements LineCounter{
	public Map<File, Integer> myMap;
	public LineCounterSub() 
	{
		super();
		myMap = new HashMap<File, Integer>();
	}

	@Override
	public Map<File, Integer> countLines(File directory, String fileSelectionPattern, boolean recursive)
	{
		myMap.clear();
		Pattern p = Pattern.compile(fileSelectionPattern);
		super.processDir(directory, p, recursive);
		return myMap;
	}
	
	@Override
	public void processFile(File file)
	{
		int number = 0;
		try 
		{
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine())
			{
				number++;
				scan.nextLine();
			}
			scan.close();
			myMap.put(file, number);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

}
