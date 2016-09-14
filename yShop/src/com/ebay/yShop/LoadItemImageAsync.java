package com.ebay.yShop;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadItemImageAsync extends AsyncTask<String, Void, Bitmap> {

	private ImageView holder = null;

	
	public LoadItemImageAsync(ImageView image){
		holder = image;
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {

		
    	try {
    		
    		URL url = new URL(urls[0]);
    		InputStream is = (InputStream)url.getContent();
    		
    		Bitmap bitmap = BitmapFactory.decodeStream(is);
  		  
    		return bitmap;	
  		  
  		} 
    	catch (Exception e)
    	{
  		  e.printStackTrace();
  		}		
    	
		return null;
				
	}
	
	@Override
    protected void onPostExecute(Bitmap bitmap) {
		
		holder.setImageBitmap(bitmap); 
	}
	
}
