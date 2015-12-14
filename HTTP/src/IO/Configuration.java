package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import main.Parser;

public class Configuration 
{

	public String m_ConfigFile;
	public String m_RootDirectory;
	public int m_BindPort;
	
	public Configuration()
	{
		LoadDefaults();
	}
	
	public Configuration(String conf)
	{
		m_ConfigFile = conf;
	}
	
	private void LoadDefaults()
	{
		m_ConfigFile = "http.conf";
		m_RootDirectory = System.getProperty("user.dir");
		m_BindPort = 80;
	}
	
	public boolean Save()
	{
		try 
		{
			File f = new File(m_ConfigFile);
			PrintWriter pw = new PrintWriter(new FileWriter(f));		
			pw.println("rootdir=" + m_RootDirectory);	
			pw.println("bindport=" + m_BindPort);	
			pw.flush();
			pw.close();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean Load()
	{
		File f = new File(m_ConfigFile);
		if(!f.exists() || !f.canRead())
			return false;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String line;
			while((line = br.readLine()) != null)
			{
				ParseLine(line);
			}
			
			br.close();
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void ParseLine(String line)
	{
		line = line.trim();
		
		if(!line.contains("="))
			return;
		
		String key = line.substring(0, line.indexOf('='));
		String val = line.substring(line.indexOf('=') + 1);
		
		if(key.equalsIgnoreCase("rootdir"))
			m_RootDirectory = val;
		else if(key.equalsIgnoreCase("bindport"))
		{
			int i = Parser.ParseInt(val);
			if(i > 0) m_BindPort = i;
		}
	}
}
