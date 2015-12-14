package main;

import java.util.InputMismatchException;

public class Parser 
{
	public static int ParseInt(String s)
	{
		try
		{
			int i = Integer.parseInt(s);
			return i;
		} catch(NumberFormatException | InputMismatchException ex)
		{
			return -1;
		}
	}
}
