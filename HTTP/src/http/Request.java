package http;

public class Request extends Header
{
	public enum RequestType
	{
		GET,
		POST,
		HEAD,
		PUT,
		DELETE,
		OPTIONS,
		TRACE,
		CONNECT,
	}
	
	public static String[] aRequestTypes = { "GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "TRACE", "CONNECT" };
	
	public RequestType m_RequestType;
	public String m_Request;
	public String m_Version;
	public String m_Host;
	public String m_Connection;
	public String m_Pragma;
	public String m_CacheControl;
	public String m_Accept;
	public String m_UpgradeInsecureRequests;
	public String m_UserAgent;
	public String m_AcceptEncoding;
	public String m_AcceptLanguage;
	
	public Request(RequestType rt, String request, String version)
	{
		m_RequestType = rt;
		m_Request = request;
		m_Version = version;
	}
	
	public static RequestType GetRequestType(String s)
	{
		for(int i = 0; i < aRequestTypes.length; i++)
		{
			if(aRequestTypes[i].equalsIgnoreCase(s))
				return RequestType.values()[i];
		}
		
		return null;
	}
}
