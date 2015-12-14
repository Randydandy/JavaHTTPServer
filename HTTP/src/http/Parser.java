package http;

import http.Request.RequestType;

public class Parser
{
	public static Request ParseRequest(String s)
	{
		Request r = null;		
		String[] aParams = null;
		String[] aSplit = s.split("\r\n");
		if(aSplit.length < 1) return null;
		
		String FirstLine = aSplit[0];
		aParams = FirstLine.split(" ");
		if(aParams.length < 3) return null;
		
		RequestType type = Request.GetRequestType(aParams[0]);
		if(type == null) return null;
		
		r = new Request(type, aParams[1], aParams[2]);
				
		for(String split : aSplit)
		{
			aParams = split.split(" ");
			if(aParams.length < 2) continue;
			
			String key = aParams[0];
			String val = aParams[1];
			
			if(key.equalsIgnoreCase("Host: "))
				r.m_Host = val;
			else if(key.equalsIgnoreCase("Connection: "))
				r.m_Connection = val;
			else if(key.equalsIgnoreCase("Pragma: "))
				r.m_Pragma = val;
			else if(key.equalsIgnoreCase("Cache-Control: "))
				r.m_CacheControl = val;
			else if(key.equalsIgnoreCase("Accept: "))
				r.m_Accept = val;
			else if(key.equalsIgnoreCase("User-Agent: "))
				r.m_UserAgent = val;
			else if(key.equalsIgnoreCase("Accept-Encoding: "))
				r.m_AcceptEncoding = val;
			else if(key.equalsIgnoreCase("Accept-Language: "))
				r.m_AcceptLanguage = val;
		}
		
		return r;
	}
}
