package edu.virginia.cs.ArtApp1;

import android.app.Application;

/*
import edu.virginia.cs.ArtApp.R;
import edu.virginia.cs.ArtApp.R.id;
import edu.virginia.cs.ArtApp.R.layout;
import edu.virginia.cs.ArtApp.R.menu;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
*/

public class Globals extends Application{
  private String username="chun";
  private ImageLoader imgLoader;
 
  public String getUser(){
    return this.username;
  }
 
  public void setUser(String d){
    this.username=d;
  }
}
