package edu.virginia.cs.ArtApp1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import edu.virginia.cs.ArtApp.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class artItemAdapter extends ArrayAdapter<artItem>{

    Context context; 
    int layoutResourceId;    
    ArrayList<artItem> data = null;
//    String url = "http://plato.cs.virginia.edu/~cs4720s14onion/img/images.jpeg";
    
    public artItemAdapter(Context context, int layoutResourceId, ArrayList<artItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ArtHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ArtHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.artitem_img);
            holder.txtDesc = (TextView)row.findViewById(R.id.artitem_text);
            holder.txtTitle = (TextView)row.findViewById(R.id.artitem_title);
            holder.txtPrice = (TextView)row.findViewById(R.id.artitem_price);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ArtHolder)row.getTag();
        }
        
        artItem newArt = data.get(position);
        holder.txtDesc.setText("Category: " + newArt.getCategory() + "\nDescription: " +  newArt.getDescription() + "\nUploaded at " + newArt.getDateCreated()
				+ "\n");
        holder.txtTitle.setText(newArt.getTitle());
        holder.txtPrice.setText("Price: $" + newArt.getPrice() + "\nBy " + newArt.getSeller());
    
        
        // Loader image - will be shown before loading image
        int loader = R.drawable.loadingimg;
         
        // Imageview to show
//        ImageView image = (ImageView) findViewById(R.id.image);
                
        // ImageLoader class instance
        ImageLoader imgLoader = new ImageLoader(context);
         
        // whenever you want to load an image from url
        // call DisplayImage function
        // url - image url to load
        // loader - loader image, will be displayed before getting image
        // image - ImageView 
        imgLoader.DisplayImage(newArt.getImageURL(), loader, holder.imgIcon);

 
        return row;
    }
    
    static class ArtHolder
    {
        ImageView imgIcon;
        TextView txtDesc;
        TextView txtTitle;
        TextView txtPrice;
    }
    
}