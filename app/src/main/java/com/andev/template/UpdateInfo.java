package com.andev.template;

import androidx.annotation.Keep;
//Update Payload Information
@Keep
public class UpdateInfo{
	
	
	public String uri=null; //url of update apk
	public String md5sum=null; //md5sum for update 
	public int version=0; //version number of 
	public double size=0.0; //Download size of update in MB
	public boolean required=false; //compulsorily update required.
	public boolean push=false; //true if update should be downloaded and installed
	
	
	}
	
	

	
	/* Equibrilium Json
{ 	
"uri":"https://filebin.net/pew4uqfik4kppe8d/app-debug.apk" 
,
"md5sum":"92ada87d1d06d23aeafaa6256a6c9fdb"
, 	
"version":2 	
, 	
"size":9
, 	
"required":false 	
, 
"push":true
}

*/




/*
{
  "rules": 
  {
"update":
{
  ".read": true
  ,
  ".write": false
}
,
 "ans": 
    {
      "$uid": 
      {
        ".read": "root.child('rmt').child($uid).child(auth.uid).child('read').val() == true"
        ,
         ".write": "auth != null && auth.uid === $uid"
      }
    }
,
     "rmt":
    {
      "$ruid": 
      {
        ".read": "auth != null"
        ,
        ".write": "auth != null && auth.uid === $ruid"
      }
    }
    ,
      "rqs":
    {
      
        ".read": true
        ,
        ".write": true
      


    }
  }
}

*/

	
