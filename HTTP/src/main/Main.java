package main;

import tcp.Server;

public class Main 
{

	public static boolean m_Running;
	
	public static void main(String[] args) throws InterruptedException
	{
		m_Running = true;
		Server s = new Server();
		s.Start();
		
		while(m_Running)
			Thread.sleep(10);
		
		s.Close();
	}	
	
}
