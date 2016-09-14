package com.ebay.yShop;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OccasionsGrid extends BaseAdapter{
    private Context mContext;

    private ArrayList<Occasion> m_occasions;
    
    public OccasionsGrid(Context c, ArrayList<Occasion> occasions) {
          mContext = c;
          m_occasions = occasions;

      }
    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return m_occasions.size();
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
            
            String occasionName = m_occasions.get(position).occasionName;
            
            textView.setText(occasionName);
            
            imageView.setImageResource(getImageId(occasionName));
            
          } else {
            grid = (View) convertView;
          }
      return grid;
    }
    
    private int getImageId(String occasionName)
    {
    	if(occasionName.equalsIgnoreCase("Birthday"))
    	{
    		return R.drawable.birthday;
    	}
    	else if(occasionName.equalsIgnoreCase("Christmas"))
    	{
    		return R.drawable.christmas;
    	}
    	else if(occasionName.equalsIgnoreCase("Back To School"))
    	{
    		return R.drawable.backtoschool;
    	}
    	else if(occasionName.equalsIgnoreCase("Father Day"))
    	{
    		return R.drawable.fatherday;
    	}
    	else if(occasionName.equalsIgnoreCase("Mother Day"))
    	{
    		return R.drawable.motherday;
    	}
    	else if(occasionName.equalsIgnoreCase("Memorial Day"))
    	{
    		return R.drawable.memorial;
    	}
    	else if(occasionName.equalsIgnoreCase("4th July"))
    	{
    		return R.drawable.independence;
    	}
    	else if(occasionName.equalsIgnoreCase("New Year"))
    	{
    		return R.drawable.newyear;
    	}
    	else if(occasionName.equalsIgnoreCase("Thanksgiving"))
    	{
    		return R.drawable.thanksgiving;
    	}
    	
    	
    	return R.drawable.birthday;
    	
    }
}
