package com.andev.template;
import androidx.annotation.Keep;

@Keep
public class Privilages
{
	public Boolean read=null;
	public Boolean write=null;
	
	public Privilages()
	{
	}
	
	public Privilages(boolean read, boolean write)
	{
		this.read=read;
		this.write=write;
	}
}
