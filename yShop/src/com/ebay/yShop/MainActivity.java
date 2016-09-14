package com.ebay.yShop;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String OCCASION = "com.ebay.yShop.OCCASION";
	public static final String KEYWORDS = "com.ebay.yShop.KEYWORDS";
	
	private ArrayList<Occasion> occasions = new ArrayList<Occasion>();
	
	GridView grid;
	  
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loadOccasions();
        
        OccasionsGrid adapter = new OccasionsGrid(MainActivity.this, occasions);
        grid=(GridView)findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						
						openCategoryActivity(position);
						
					}
                });
      }
    
    
	private void openCategoryActivity(int position) {
		Intent intent = new Intent(this, CategoryActivity.class);
		intent.putExtra(OCCASION, occasions.get(position).occasionName);
		intent.putExtra(KEYWORDS, occasions.get(position).keywords);
		startActivity (intent);		
	}
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void loadOccasions() {
		try
		{
	        //Get the XML file into a Document object
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuild = factory.newDocumentBuilder();
	        
	        Context context = getApplicationContext();
	        InputStream istream = context.getResources().openRawResource(R.raw.occasions);
	        
	        Document xmlDoc = docBuild.parse(istream);
	          	
	        
	    	String expressionName = "/occasions/occasion/name";
	    	String expressionId = "/occasions/occasion/image";
	    	String expressionKeywords = "/occasions/occasion/keywords";
	    	 
	    	XPath xPath =  XPathFactory.newInstance().newXPath();
	    	          	
	    	NodeList nodeListName = (NodeList) xPath.compile(expressionName).evaluate(xmlDoc, XPathConstants.NODESET);
	    	NodeList nodeListId = (NodeList) xPath.compile(expressionId).evaluate(xmlDoc, XPathConstants.NODESET);
	    	NodeList nodeListKeywords = (NodeList) xPath.compile(expressionKeywords).evaluate(xmlDoc, XPathConstants.NODESET);
	    	
	    	System.out.println(nodeListName.getLength());
	    	System.out.println(nodeListId.getLength());
	    	
	    	for (int i = 0; i < nodeListName.getLength(); i++) {
	    		Occasion o = new Occasion();
	    		
	    		o.occasionName = nodeListName.item(i).getFirstChild().getNodeValue();
	    		Node n = nodeListId.item(i).getFirstChild();
	    		
	    		if(n != null)
	    		{        		
	    			o.occasionUri = nodeListId.item(i).getFirstChild().getNodeValue();
	    		}
	    		
	    		n = nodeListKeywords.item(i).getFirstChild();
	    		
	    		if(n != null)
	    		{        		
	    			o.keywords = nodeListKeywords.item(i).getFirstChild().getNodeValue();
	    		}
	    		
	    		occasions.add(o);
	    	}
	    	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
    }
}
    

