package com.ebay.yShop;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class CategoryActivity extends Activity {

    private GridView grid;
    
    public static final String CATEGORY = "com.ebay.yShop.CATEGORY";
    public static final String OCCASION = "com.ebay.yShop.OCCASION";
    public static final String KEYWORDS = "com.ebay.yShop.KEYWORDS";
    
    private String m_occasion;
    private String m_keywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}
	
	private class LoadCategoriesAsync extends AsyncTask<String, Void, Integer> {

		private CategoryActivity holder = null;
		private String m_occasion;
		private String m_keywords;
		private ArrayList<Category> m_categories = new ArrayList<Category>();

		
		public LoadCategoriesAsync(CategoryActivity parent, String occasion, String keywords){
			holder = parent;
			m_occasion = occasion;
			m_keywords = keywords;
		}
		
		@Override
		protected Integer doInBackground(String... occasions) {
			// TODO Auto-generated method stub
			GetSuggestedCategories cat = new GetSuggestedCategories();
			
			
			m_categories =  cat.getCategories(occasions[0], m_keywords);
			
			return 1;
			
		}
		
		@Override
	    protected void onPostExecute(Integer value) {
			
	        CategoriesGrid adapter = new CategoriesGrid(CategoryActivity.this, m_categories, m_occasion, m_keywords);
	        grid=(GridView)findViewById(R.id.gridCat);
	            grid.setAdapter(adapter);
	            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stubfd
														
							openListingItemActivity(m_categories.get(position).categoryId);
						}
	                });
		}
		
	}
	
	private void openListingItemActivity(String category) {
		Intent intent = new Intent(this, ListingItemsActivity.class);
		intent.putExtra(OCCASION, m_occasion);
		intent.putExtra(CATEGORY, category);
		intent.putExtra(KEYWORDS, this.m_keywords);
		
		startActivity (intent);		
	}
	
    private void initView() {
    	
		Intent intent = getIntent();
		String occasion = intent.getStringExtra(MainActivity.OCCASION);
		String keywords = intent.getStringExtra(MainActivity.KEYWORDS);
		
		m_occasion = occasion;
		m_keywords = keywords;
		
		this.setTitle(occasion);
		
		
		LoadCategoriesAsync task = new LoadCategoriesAsync(this, occasion, keywords);
		
	    task.execute(new String[] { occasion });
    }

}
