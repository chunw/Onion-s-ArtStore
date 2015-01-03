package edu.virginia.cs.ArtApp1;

import java.util.Arrays;
import edu.virginia.cs.ArtApp.R;

public class artItem {

	public int ID;
	public String Title;
	public String Seller;
	public String Description;
	public String DateCreated;
	public String Category;
	public float Price;
	public String ImageURL;
	
	public artItem() {
		super();
	}
	
	public int getID() {
		return ID;
	}

	/*	@Override
	public String toString() {
		return "Title: " + Title + "\n" + "Price: $" + Price
				+ "\nSeller: " + Seller + "\nDescription: " +  Description + "\nDateCreated: " + DateCreated
				+ "\nCategory: " + Category + "\nImage: " + ImageURL + "\n";
	}*/
	
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}

	public String getSeller() {
		return Seller;
	}

	public void setSeller(String seller) {
		this.Seller = seller;
	}
}