
package com.ebay.yShop;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriesGrid
 extends BaseAdapter{
    private Context mContext;

    public ArrayList<Category> m_categories;
    private String m_occasion;
    private String m_keywords;
    private ImageLoader imageLoader;
    
    
    public CategoriesGrid(Context c, ArrayList<Category> categories, String occasion, String keywords) {
          mContext = c;
          m_categories = categories;
          m_occasion = occasion;
          m_keywords = keywords;
          
          imageLoader=new ImageLoader(c.getApplicationContext());

      }
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return m_categories.size();
    }
    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }
    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      View grid;
      LayoutInflater inflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          if (convertView == null) {
            grid = new View(mContext);
        grid = inflater.inflate(R.layout.single_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            
            Category c = m_categories.get(position);
            textView.setText(c.categoryName);                        
            
            LoadImageAsync task = new LoadImageAsync(imageView, m_occasion, m_keywords);
            
            task.execute(new String[] { c.categoryId });

            
          } else {
            grid = (View) convertView;
          }
      return grid;
    }
    
	private class LoadImageAsync extends AsyncTask<String, Void, String> {

		private ImageView holder = null;
		private String m_occasion;
		private String m_keywords;

		
		public LoadImageAsync(ImageView image, String occasion, String keywords){
			holder = image;
			m_occasion = occasion;
			m_keywords = keywords;
		}
		
		@Override
		protected String doInBackground(String... categories) {

			
        	try {
        	
        		String imageUri = RestUtil.getCategoryUri(m_occasion, m_keywords, categories[0]);
        		
        		return imageUri;      		  
      		  
      		} 
        	catch (Exception e)
        	{
      		  e.printStackTrace();
      		}		
        	
			return null;
					
		}
		
		@Override
	    protected void onPostExecute(String uri) {
			
			imageLoader.DisplayImage(uri, holder);
			
		}
		
	}
}

