
package com.ebay.yShop;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

 
 
public class GetSuggestedCategories {
 
 

    public ArrayList<Category> getCategories(String occasion, String keywords)
    {
        String devID = "********";
        String appID = "********";
        String certID = "*******";
        String userToken = "*******dj6wVnY+sEZ2PrA****WkIOWD9/Vg9FH5rWRzptd";

        String serverUrl = "https://api.ebay.com/ws/api.dll";
        String compatLevel = "551";
        String siteID = "0";
        String verb = "GetSuggestedCategories";
        ArrayList<Category> categories = new ArrayList<Category>();
        
        String xmlRequest =
        		"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                 "<GetSuggestedCategoriesRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">" +
            "<Query>" + occasion + "</Query>" +
         "<RequesterCredentials>" +
         "<eBayAuthToken>" + userToken + "</eBayAuthToken>" +
         "</RequesterCredentials>" +
        "</GetSuggestedCategoriesRequest>";
 
 
 
        try
        {

            URL server = new URL(serverUrl);
            HttpsURLConnection connection = (HttpsURLConnection) (server.openConnection());
 
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
 
            addeBayHTTPHeaders(connection, devID, appID, certID, compatLevel,verb,siteID);


            Document xmlDoc = SendRequest(connection, xmlRequest);

            if(xmlDoc == null)
                return null;
 
            //Check for errors
            NodeList errorsNode = xmlDoc.getElementsByTagName("Errors");
            if(errorsNode.getLength() > 0) //there are errors
            {
                //Output the error
                Element errElement = (Element) errorsNode.item(0);
 
                //Output the error code
                Node errCodeNode = errElement.getElementsByTagName("ErrorCode").item(0).getChildNodes().item(0);
                System.out.println("Response Error : " + errCodeNode.getNodeValue());
 
                //Output the short error message
                Node errShortNode = errElement.getElementsByTagName("ShortMessage").item(0).getChildNodes().item(0);
                System.out.println(errShortNode.getNodeValue());
 
                //Output the long error message if there is one
                try
                {
                    Node errLongNode = errElement.getElementsByTagName("LongMessage").item(0).getChildNodes().item(0);
                    System.out.println(errLongNode.getNodeValue());
                }
                catch(Exception e) {}
            }
            else //CALL SUCCESSFUL
            {
                //Output the Result
            	TransformerFactory tf = TransformerFactory.newInstance();
            	Transformer transformer = tf.newTransformer();
            	StringWriter writer = new StringWriter();
            	transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
            	String output = writer.getBuffer().toString();
            	
            	String expressionName = "/GetSuggestedCategoriesResponse/SuggestedCategoryArray/SuggestedCategory/Category/CategoryName";
            	String expressionId = "/GetSuggestedCategoriesResponse/SuggestedCategoryArray/SuggestedCategory/Category/CategoryID";
            	 
            	XPath xPath =  XPathFactory.newInstance().newXPath();
            	          	
            	NodeList nodeListName = (NodeList) xPath.compile(expressionName).evaluate(xmlDoc, XPathConstants.NODESET);
            	NodeList nodeListId = (NodeList) xPath.compile(expressionId).evaluate(xmlDoc, XPathConstants.NODESET);
            	
            	
            	for (int i = 0; i < nodeListName.getLength() - 1; i++) {
            		Category c = new Category();
            		
            		c.categoryName = nodeListName.item(i).getFirstChild().getNodeValue();
            		c.categoryId = nodeListId.item(i).getFirstChild().getNodeValue();
            		
            		
            		categories.add(c);
            	}
            	         	
            	return categories;          	

            }
 
 
        }
        catch(Exception e)
        {
            //Output error message
        	System.out.println("ERROR: \n" + e.toString());
        	e.printStackTrace();
        }
        
        return null;
 
    }
 
 
 
    /**
     * Sends the request to the Server and returns the Document (XML)
     * that is returned as the response.
     * @param connection The HttpsURLConnection object to be used to execute the request
     * @return The Document (Xml) returned from the server after the request was made.
     */
    private static Document SendRequest(HttpsURLConnection connection, String xmlRequest)
    {
 
        try
        {
            //Get the XML file into a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuild = factory.newDocumentBuilder();
            
;
            
            InputSource is = new InputSource(new StringReader(xmlRequest));
            
            Document xmlDoc = docBuild.parse(is);       
                         
            //Get the output stream of the connection
            OutputStream out = connection.getOutputStream();
            
 
            //Transform and write the Document to the stream
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer tr = tf.newTransformer();
            Source input = new DOMSource(xmlDoc);
            Result output = new StreamResult(out);
            tr.transform(input, output);
            out.flush();
            out.close();
 
            //Get the Input stream for the response
            InputStream in = connection.getInputStream();
            //Get the stream into a Document object
            Document response = docBuild.parse(in);
            //close the input stream and connection
            in.close();
            connection.disconnect();
 
            //return the response XML Document
            return response;
 
 
 
        }
        catch(IOException e)
        {
            System.out.println("SendRequest IO Error: " + e.toString());
            e.printStackTrace();
            return null;
        }
        catch(Exception e)
        {
            System.out.println("Error Sending Request: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }
 
 
    /**
     * Adds the necessary headers to the HttpsURLConnection passed in
     * order for the call to be successful
     * @param connection The HttpsURLConnection to add the headers to
     * @param devID Developer ID, as registered with the Developer's Program.
     * @param appID Application ID, as registered with the Developer's Program.
     * @param certID Certificate ID, as registered with the Developer's Program.
     * @param compatLevel Regulates versioning of the XML interface for the API.
     * @param verb Name of the function being called e.g. "GetItem"
     * @param siteID The Id of the eBay site the call should be executed on
     */
    private static void addeBayHTTPHeaders(HttpsURLConnection connection,
                                           String devID, String appID,
                                           String certID, String compatLevel,
                                           String verb, String siteID)
    {
 
        // Add the Compatibility Level Header
        connection.addRequestProperty("X-EBAY-API-COMPATIBILITY-LEVEL", compatLevel);
 
        // Add the Developer Name, Application Name, and Certification Name Headers
        connection.addRequestProperty("X-EBAY-API-DEV-NAME", devID);
        connection.addRequestProperty("X-EBAY-API-APP-NAME", appID);
        connection.addRequestProperty("X-EBAY-API-CERT-NAME", certID);
 
        // Add the API verb Header
        connection.addRequestProperty("X-EBAY-API-CALL-NAME", verb);
 
        // Add the Site Id Header
        connection.addRequestProperty("X-EBAY-API-SITEID", siteID);
 
        // Add the Content-Type Header
        connection.addRequestProperty("Content-Type", "text/xml");
    }
 
 
 
    /**
     * Allows the user to enter a line of text onto the standard
     * input (usually the console), and then return it as a string.
     * @return The string entered by the user
     */
    private static String readLine()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      try {
         return br.readLine();
      } catch (IOException ioe) {
         System.out.println("IO error trying to read your name!");
         ioe.printStackTrace();
         return "";
      }
    }
}
