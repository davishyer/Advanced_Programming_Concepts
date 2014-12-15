package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GrepSub extends FileProcessor implements Grep{
	public Map<File, List<String>> myMap;
	public Pattern subString;
	public GrepSub()
	{
		super();
		myMap = new HashMap<File, List<String>>();
	}

	@Override
	public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) 
	{
		myMap.clear();
		Pattern p = Pattern.compile(fileSelectionPattern);
		subString = subString.compile(substringSelectionPattern);
		super.processDir(directory, p, recursive);
		return myMap;
	}
	
	@Override
	public void processFile(File file)
	{
		List<String> list = new ArrayList<String>();
		try 
		{
			Scanner scan = new Scanner(file);
			while(scan.hasNext())
			{
				String s = scan.nextLine();
				if(subString.matcher(s).find())
				{
					list.add(s);
				}
			}
			scan.close();
			if(list.size() > 0)
			{
				myMap.put(file, list);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

}
