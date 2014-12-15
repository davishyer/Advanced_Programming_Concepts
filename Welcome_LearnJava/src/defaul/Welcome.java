package defaul;

public class Welcome 
{
	public static void main(String[] args)
	{
		String[] greeting = new String[3];
		greeting[0] = "Welcome to Java!";
		greeting[1] = "Random Stuff";
		greeting[2] = "By: Davis";
		
		for(String g : greeting)
			System.out.println(g);
	}
}
