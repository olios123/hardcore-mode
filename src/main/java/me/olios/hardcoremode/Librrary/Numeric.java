/**
 * CC Creative Commons 2022
 *  Attribution-NoDerivatives 4.0 International
 *  Author olios
 **/

package me.olios.hardcoremode.Librrary;

public class Numeric {

	public static boolean check(String msg)
	{
		try
		{
			if (msg.contains("."))
			{
				String[] split = msg.split("\\.");
				if (split.length > 1)
				{
					Integer.parseInt(split[0]);
					Integer.parseInt(split[1]);
					return true;
				}
			}
			Integer.parseInt(msg);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	public static int get(String msg)
	{
		try
		{
			return Integer.parseInt(msg);
		}
		catch (NumberFormatException e)
		{
			return -1;
		}
	}
}
