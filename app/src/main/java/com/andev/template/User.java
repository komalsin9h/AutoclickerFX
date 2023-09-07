package com.andev.template;
import androidx.annotation.Keep;

@Keep
public class User
{
	public String uid=null; //Unique user specific key
	public Privilages prvlg=new Privilages();
	
	public User(String uid)
	{
		this.uid=uid;
	}	

	
}
