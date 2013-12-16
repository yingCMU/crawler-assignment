/*---------------------------------------------------------------------------------------------
 *Linkedin.java
 *Written By: Kevin Takacs and Philip Zisner
 *This class uses the OAuth Signpost library and Linkedin's REST API to authenticate a user's
 *account and retrieve their friend list and statuses of these friends for OpenSocialPortlet. 
 *--------------------------------------------------------------------------------------------*/
package edu.asu.east.osportlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import oauth.signpost.*;
import oauth.signpost.basic.*;
import oauth.signpost.exception.*;

public class Linkedin 
{	
	//API key obtained from Linkedin
	protected final String APIKEY = "3rguDEeREa3hPLxMwuTblTIehY7XMKzrs7FT6MHX_49sk0j4S9rzeFJzygCVtxtS";
	//API secret obtained from Linkedin
	protected final String SECRETKEY = "BnnCQmiRJmuCy946k1RnLU074QnJxK-n_lZt2gLf0XYEW4SAsZ3-V5lBgYEndXxm";
	//Linkedin request url
	protected final String LINKEDINREQURL = "https://api.linkedin.com/uas/oauth/requestToken";
	//Linkedin Autorization URL
	protected final String LINKEDINAUTHURL = "https://api.linkedin.com/uas/oauth/authorize";
	//Linkedin Access token URL
	protected final String LINKEDINACCESSURL = "https://api.linkedin.com/uas/oauth/accessToken";
	//Linkedin REST URL for members of a user's network
	protected final String LINKEDINSTATUSURL = "http://api.linkedin.com/v1/people/~/network?type=STAT&count=50";
	protected String token;
	protected String tokenSecret;
	private String userid;
	List<String> infoList;
	
	//Create a new OAuthConsumer from the signpost library with the Linkedin API and secret keys
	OAuthConsumer consumer = new DefaultOAuthConsumer(APIKEY, SECRETKEY);
	//Use the REST urls to create an OAuthProvider object for Linkedin
	OAuthProvider provider = new DefaultOAuthProvider(LINKEDINREQURL, LINKEDINACCESSURL, LINKEDINAUTHURL);
	
	public Linkedin(String userid)
	{
		this.userid = userid;	
	}

	public void useExistingTokens(String token, String tokenSecret) 
	{
		//Create a new instance of the OAuth consumer and add the stored token and tokenSecret to this instance
		consumer = new DefaultOAuthConsumer(APIKEY, SECRETKEY);
		consumer.setTokenWithSecret(token, tokenSecret);
	}

	public String authenticate()
	{
		//Create and return the request URL to authenticate with Linkedin
		String url = "";
		try 
		{
			url = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
			return url;
		}
		catch (OAuthMessageSignerException e) 
		{	
			e.printStackTrace();
		}
		catch (OAuthNotAuthorizedException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthExpectationFailedException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthCommunicationException e) 
		{
			e.printStackTrace();
		}
		return url;
	}

	public String[] getTokens(String pin)
	{
		/* Retrieve the access token using the OAuth verifier and return the access token
		 * in a String array containing the token and token secret. 
		 */
		String[] tokenSecretPair = new String[2];
		
		try 
		{
			provider.retrieveAccessToken(consumer, pin);
			token = consumer.getToken();
			tokenSecret = consumer.getTokenSecret();
			tokenSecretPair[0] = token;
			tokenSecretPair[1] = tokenSecret;
		} 
		catch (OAuthMessageSignerException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthNotAuthorizedException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthExpectationFailedException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthCommunicationException e) 
		{
			e.printStackTrace();
		}
		return tokenSecretPair;
	}
	
	public List<String> requestStatus()
	{
		/* Create an HTTP connection to the Linkedin REST URL for the friends and statuses of a user's
		 * network, parse the response xml, and return it as a List
		 */
		List<String> friendStatusList = new ArrayList<String>();
		try 
		{
			URL url2 = new URL(LINKEDINSTATUSURL);
			HttpURLConnection request = (HttpURLConnection) url2.openConnection();
			consumer.sign(request);
			request.setDoInput(true);
			request.setDoOutput(true);
			request.connect();
			friendStatusList = parseInput(request.getInputStream());
			request.disconnect();
		}
		catch (MalformedURLException e) 
		{	
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthMessageSignerException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthExpectationFailedException e) 
		{
			e.printStackTrace();
		}
		catch (OAuthCommunicationException e) 
		{
			e.printStackTrace();
		}
		return friendStatusList;
	}

	public List<String> parseInput(InputStream inputStream) 
	{
		//Parse the first name, last name, and status for each of the user's friends from the response xml
		List<String> returnList = new ArrayList<String>();
		try 
		{
			DocumentBuilderFactory readerFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder inputReader = readerFactory.newDocumentBuilder();
			Document inputDoc = inputReader.parse(inputStream);
			inputDoc.getDocumentElement().normalize();
			
			NodeList nodeList = inputDoc.getElementsByTagName("update");
			returnList.add("<b>Linkedin:</b> <br/>");
			for (int i = 0; i < nodeList.getLength() ;i++) 
			{
				Node firstNode = nodeList.item(i);
			    
			    if (firstNode.getNodeType() == Node.ELEMENT_NODE) 
			    {
			  
			      Element firstElement = (Element) firstNode;
			      
			      NodeList firstNames = firstElement.getElementsByTagName("first-name");
			      Element firstNameElement = (Element) firstNames.item(0);
			      NodeList firstName = firstNameElement.getChildNodes();
			      String fName = ((Node) firstName.item(0)).getNodeValue(); 
			      
			      NodeList lastNames = firstElement.getElementsByTagName("last-name");
			      Element lastNameElement = (Element) lastNames.item(0);
			      NodeList lastName = lastNameElement.getChildNodes();
			      String lName = ((Node) lastName.item(0)).getNodeValue();
			      
			      NodeList statusUpdates = firstElement.getElementsByTagName("current-status");
			      Element statusElement = (Element) statusUpdates.item(0);
			      NodeList status = statusElement.getChildNodes();
			      String stat = ((Node) status.item(0)).getNodeValue();
			      
			      returnList.add("<u>" + fName + " " + lName + ":</u> " + stat + "<br/><br/>");
			    }
			}
		}
		catch (ParserConfigurationException e) 
		{	
			e.printStackTrace();
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return returnList;
	}
}