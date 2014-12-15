package listem;

import java.io.File;
import java.util.regex.Pattern;

public abstract class FileProcessor 
{
	public String directoryName;
	public String filePattern;
	public boolean recursion;
	public FileProcessor()
	{
		
	}
	
	public void processFile(File file)
	{
		//to be overriden
	}
	
	public void processDir(File directoryName, Pattern fileP, boolean recursion)
	{
		if(directoryName == null)
		{
			return;
		}
		if(!directoryName.isDirectory() || !directoryName.canRead())
		{
			return;
		}
		for(File file : directoryName.listFiles())
		{
			if(file.isDirectory() & recursion)
			{
				processDir(file, fileP, recursion);
			}
			if(file.isFile())
			{
				if(file.canRead())
				{
					if(fileP.matcher(file.getName()).matches())
					{
						processFile(file);
					}
				}
			}
		}
	}
}
