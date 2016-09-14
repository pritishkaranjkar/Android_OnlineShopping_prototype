package com.ebay.yShop;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListingItemsActivity extends Activity {
	
	private ListView m_listView;
	private ArrayList<Item> m_itemList = new ArrayList<Item>();
	
	public static final String VIEW_ITEM_URL = "com.ebay.yShop.VIEW_ITEM_URL";
	public static final String VIEW_ITEM_TITLE = "com.ebay.yShop.VIEW_ITEM_TITLE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing_items);
		
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listing_items, menu);
		return true;
	}
	
	
    private void initView() {
    	
		Intent intent = getIntent();
		String category = intent.getStringExtra(CategoryActivity.CATEGORY);
		String occasion = intent.getStringExtra(CategoryActivity.OCCASION);
		String keywords = intent.getStringExtra(CategoryActivity.KEYWORDS);
		
		this.setTitle(occasion);
		
    	LoadItemsAsync task = new LoadItemsAsync(this, occasion, keywords);
	    task.execute(new String[] { category });
    }
    
	
	private void openListingItemActivity(String url, String title)
	{
		Intent intent = new Intent(this, ItemDetailActivity.class);
		intent.putExtra(VIEW_ITEM_URL, url);
		intent.putExtra(VIEW_ITEM_TITLE, title);
		
		startActivity(intent);
	}

		
		
    
	private class LoadItemsAsync extends AsyncTask<String, Void, ArrayList<Item>> {
		
		private ListingItemsActivity holder = null;
		private String m_occasion;
		private String m_keywords;


		
		public LoadItemsAsync(ListingItemsActivity parent, String occasion, String keywords){
			holder = parent;
			
			m_occasion = occasion;
			m_keywords = keywords;
		}
		

		@Override
		protected ArrayList<Item> doInBackground(String... categories) {
			// TODO Auto-generated method stub
			
			RestUtil util = new RestUtil();
			
			return util.getItemsForCategory(m_occasion, m_keywords, categories[0]);			
		}
		
		@Override
	    protected void onPostExecute(ArrayList<Item> result) {
			
			if(null == result) return;
			
			m_listView = (ListView)findViewById(R.id.listview);
	    
			m_itemList.clear();
			
	    	for(Item item : result)
	    	{	    			    	
	    		AddItem(item.imageUri, item.title, item.desc, item.price, item.viewItemUri);
	    	}
	        
	    	m_listView.setAdapter(new ItemAdapter(ListingItemsActivity.this, m_itemList));
	    
				

	    	m_listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) 
	            {
	            	ItemAdapter adapter = (ItemAdapter)parent.getAdapter();
	            	
	            	Item item = (Item)adapter.getItem(position);
	            	String uri = item.viewItemUri;
	            	
	            	if(null != uri)
	            	{	            	
	            		openListingItemActivity(uri, item.title);
	            	}	            	
	                
	            }
	        });
	    	
		}
		
		private void AddItem(String imageUri, String title, String desc, String price, String viewItemUri) {
			Item item = new Item();
	        item.imageUri = imageUri;  
	        item.viewItemUri = viewItemUri;
	        item.title = title;
	        item.desc = desc;  
	        item.price = price + " $";
	        
	        m_itemList.add(item);
		}
		
	}
	
	private class ItemAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<Item> m_items;
        private Activity activity;
        
        private ImageLoader imageLoader;

        ItemAdapter(Activity a, ArrayList<Item> items) {
        	super();
            mInflater = getLayoutInflater();
            m_items = items;
            activity = a;
            
            imageLoader=new ImageLoader(activity.getApplicationContext());
        }

        @Override
        public int getCount() {
            return m_items.size();
        }

        @Override
        public Object getItem(int position) {
            return m_items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	 View rowView =convertView;
        	 
             if(convertView==null)
             {
                 rowView =  mInflater.inflate(R.layout.listting_listview, null);
             }
             
             Item item = m_items.get(position);
             
             ViewHolder holder = new ViewHolder(rowView);
             
        	holder.title.setText(item.title);
        	holder.desc.setText(item.desc);
        	holder.price.setText(item.price);
        	
        	imageLoader.DisplayImage(item.imageUri, holder.image);             
             
            return rowView;
        }

    }
	
    private class ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView desc;
        public TextView price;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            price = (TextView) view.findViewById(R.id.price);
        }
    }

}
