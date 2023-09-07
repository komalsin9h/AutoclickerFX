package com.andev.template;

import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast; 
import java.util.Random;

import android.content.ComponentName;
import android.provider.Settings;
import android.text.TextUtils;

import android.os.Handler;
//import java.util.Date;
//import java.util.TimeZone;
//import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import java.io.BufferedReader;
import java.io.InputStreamReader;



public class StaticLib
{	

////////////////
////////////////
////////////////
////////////////
//////////////// 
public static int versionEpoch=1666693268; // Declare version to update
////////////////
////////////////
////////////////
////////////////
////////////////

	public static SharedPreferences sharedPref(Context context)
	{
		return context.getSharedPreferences("SS", Context.MODE_PRIVATE);
	}
	public static void broadcast_click(Context ctx, int x, int y)
	{
		Intent intent = new Intent();    
		intent.setAction("DISPATCH_CMD");
		intent.putExtra("x", x);
		intent.putExtra("y", y);
		ctx.sendBroadcast(intent);
	}
	public static void broadcast_disable(Context ctx)
	{
		Intent intent = new Intent();    
		intent.setAction("DISPATCH_CMD");
		intent.putExtra("DISABLE_SELF", true);
		ctx.sendBroadcast(intent);
	}
	
	public static void toast(Context ctx, String str)
	{
		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
	}
	public static void toastLong(Context ctx, String str)
	{
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}
	public static void broadcast_msg_to_print_everywhere(Context ctx, String string)
	{
		toast(ctx, string);
		///
	}
	public static void print(Context ctx, String string)
	{
		toast(ctx, string);
		///
	}
	public static void printException(Context ctx, Exception e)
	{
		toastLong(ctx, e.getMessage()+"\n"+e.toString());
	}
	
	// Shared Preference
	public static int get_int_data_from_shared_preference(Context context, String key)
	{
		return sharedPref(context).getInt(key, 0);
	}
	public static long get_long_data_from_shared_preference(Context context, String key)
	{
		return sharedPref(context).getLong(key, 0);
	}
	public static String get_String_data_from_shared_preference(Context context, String key)
	{		
		return sharedPref(context).getString(key, "");
	}
	public static boolean get_boolean_data_from_shared_preference(Context context, String key)
	{
		return sharedPref(context).getBoolean(key, false);
	}
	public static boolean shared_preference_contains(Context context, String key)
	{
		return sharedPref(context).contains(key);
	}
	public static void write_String_to_shared_preference(Context context, String key, String String_to_write)
	{
		SharedPreferences.Editor editor = sharedPref(context).edit();
		editor.putString(key, String_to_write);
		editor.apply();
	}
	public static void write_long_to_shared_preference(Context context, String key, long long_to_write)
	{
		SharedPreferences.Editor editor = sharedPref(context).edit();
		editor.putLong(key, long_to_write);
		editor.apply();
	}
	public static void write_int_to_shared_preference(Context context, String key, int int_to_write)
	{
		SharedPreferences.Editor editor = sharedPref(context).edit();
		editor.putInt(key, int_to_write);
		editor.apply();
	}
	public static void write_boolean_to_shared_preference(Context context, String key, boolean b)
	{
		SharedPreferences.Editor editor = sharedPref(context).edit();
		editor.putBoolean(key, b);
		editor.apply();
		//BIND_KEY
	}
	
	
	// Method to check for proper string
	final public static boolean checkForProperString(String str)
	{
		if (TextUtils.isEmpty(str)) {
    		return false;
		}
		else if (str == null || str.isEmpty() || str.equals("null") || str.equals("") || str.equals(" ") || str.length() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
/*
	public static void crash()
	{
		int k = 1;
		k = k / 0;
		k = 0 / 0;
		Integer i = null; // Null Pointer Exception
		i.byteValue();
		throw new RuntimeException("MODDING IS BORING JOB, TAKE A NAP!");
	}
	*/
	
	final public static boolean check_malformed_arr(ArrayList<ArrayList<ArrayList<Integer>>> ary)
	{
		if (ary == null)
		{return false;}
		else if (ary.size() == 0)
		{return false;}
		else if
		(ary.get(0).size() == 0)
		{return false;}
		else if
		(ary.get(1).size() == 0)
		{return false;}
		else if
		(ary.size() != 2)
		{return false;}
		else if
		(ary.get(0).size() != ary.get(1).size())
		{return false;}
		else
		{ 
			int aps=ary.get(0).size();
			ArrayList<ArrayList<Integer>> xb=ary.get(0);
			ArrayList<ArrayList<Integer>> yb=ary.get(1);
			for (int i=0; i < aps; i++)
			{
				if (xb.get(i).size() != yb.get(i).size())
				{return false;};
				int opt =xb.get(i).size();
				for (int ii=0; ii < opt; ii++)
				{
					if (xb.get(i).get(ii) == 0 || yb.get(i).get(ii) == 0)
					{return false;}
				}
			}
		}
		return true;
	}
	
	//
	final public static String update_to_json(UpdateInfo u)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new Gson();
		String json = gson.toJson(u);
		return json;
	}
	final public static UpdateInfo json_to_update(String str)
	{
		Type listType = new TypeToken<UpdateInfo>(){}.getType(); 
		Gson Gos = new Gson();
		UpdateInfo ob = Gos.fromJson(str, listType);
		return ob;
	}
	//
	
	final public static String str_to_json(String arr)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new Gson();
		String str = gson.toJson(arr);
		return str;
	}
	final public static String json_to_str(String str)
	{
		Type listType = new TypeToken<String>(){}.getType(); 
		Gson Gos = new Gson();
		String ob = Gos.fromJson(str, listType);
		return ob;
	}
	final public static String arr_to_json(ArrayList<ArrayList<ArrayList<Integer>>> arr)
	{
		//Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Gson gson = new Gson();
		String str = gson.toJson(arr);
		return str;
	}
	final public static ArrayList<ArrayList<ArrayList<Integer>>> json_to_arr(String str)
	{
		Type listType = new TypeToken<ArrayList<ArrayList<ArrayList<Integer>>>>(){}.getType(); 
		Gson Gos = new Gson();
		ArrayList<ArrayList<ArrayList<Integer>>> ob = Gos.fromJson(str, listType);
		return ob;
	}
	public static ArrayList<ArrayList<ArrayList<Integer>>> createArr(int x_y, int ap, int opt)
	{
		ArrayList<ArrayList<ArrayList<Integer>>> t = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for (int i = 0; i < x_y; i++)
		{
			ArrayList<ArrayList<Integer>> tt=new ArrayList<ArrayList<Integer>>();
			for (int ii = 0; ii < ap; ii++)
			{
				ArrayList<Integer> ttt=new ArrayList<Integer>();
				for (int iii = 0; iii < opt; iii++)
				{
					ttt.add(iii, new Integer(0));
				}
				tt.add(ii, ttt);
			}
			t.add(i, tt);
		}
		return t;
	}


	public static ArrayList<ArrayList<ArrayList<Integer>>> mk_rand_arr(int x_y, int ap, int opt)
	{
		Random random = new Random();
		ArrayList<ArrayList<ArrayList<Integer>>> t = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for (int i = 0; i < x_y; i++)
		{
			ArrayList<ArrayList<Integer>> tt=new ArrayList<ArrayList<Integer>>();
			for (int ii = 0; ii < ap; ii++)
			{
				ArrayList<Integer> ttt=new ArrayList<Integer>();
				for (int iii = 0; iii < opt; iii++)
				{
					int tmp=(random.nextInt((700 - 100) + 1) + 100);
					ttt.add(iii, tmp);
				}
				tt.add(ii, ttt);
			}
			t.add(i, tt);
		}
		return t;
	}
	
	// Validate 3D Array List Of Cord
	
	final public static boolean vlidateCord3dArrayLst(ArrayList<ArrayList<ArrayList<Integer>>> ary)
	{
		if (ary==null){return false;}
		if (ary.size()==0){return false;}
		if (ary.get(0).size()==0){return false;}
		if (ary.get(1).size()==0){return false;}
		if (ary.get(0).get(0).size()==0){return false;}
		if (ary.get(1).get(0).size()==0){return false;}
		if(ary.get(0).get(0).get(0)==null){return false;}
		if(ary.get(1).get(0).get(0)==null){return false;}
		if(ary.get(0).get(0).get(0)==0){return false;}
		if(ary.get(1).get(0).get(0)==0){return false;}

		return true;
	}
	
	// Requirements to start clicking forground service
	final public static String forSerChkReq(Context ctx)
	{
		ArrayList<String> arr=requirementsChkForForService(ctx);
		String s="";
		final int arrSize=arr.size();
		for (int i=0; i < arrSize; i++)
		{
			s = s + arr.get(i);
			if (i != arrSize - 1 && i != arrSize - 2)
			{
				s = s + ", " ;
			}
			else
			{s = s + " ";}
		}
		return s;
	}
	final private static ArrayList<String> requirementsChkForForService(Context ctx)
	{
		ArrayList<String> arr=new ArrayList<String>();

		//
		if (StaticLib.shared_preference_contains(ctx, "LOGINED") == true)
		{
			if (StaticLib.get_boolean_data_from_shared_preference(ctx, "LOGINED") == false)
			{
				arr.add("Please Login Again");
			}
		}
		else
		{
			arr.add("Account Login Required");
		}

		//
		if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
		{
			if (get_String_data_from_shared_preference(ctx, "CORD").equals(""))
			{
				arr.add("Empty click locations");
			}
		}
		else
		{
			arr.add("Set click locations");
		}

		//
		if (StaticLib.shared_preference_contains(ctx, "ANS_RECEIVE_UID") == true)
		{
			if (get_String_data_from_shared_preference(ctx, "ANS_RECEIVE_UID").equals(""))
			{
				arr.add("Empty remote uid");
			}
		}
		else
		{
			arr.add("Set remote UID");
		}

		arr.add("requirements");
		return arr;
	}

	
	
	// Get Logged In User Id
	final public static String getUid(Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true)
		{
			try
			{
				return FirebaseAuth.getInstance().getCurrentUser().getUid();
			}
			catch(Exception e)
			{
				printException(ctx, e);
				
				try
				{
					return StaticLib.get_String_data_from_shared_preference(ctx, "UID");
				}
				catch(Exception e1)
				{
					printException(ctx, e1);
					return "";
				}
			}
		}
		else
		{
			return "";
		}
	}
	
	
	// Get and set uid to receive answers
	final public static void setAnsReceiveUid(Context ctx, String uid)
	{
		StaticLib.write_String_to_shared_preference(ctx, "ANS_RECEIVE_UID", uid);
	}
	
	final public static String getUidSettedToReceiveAns(Context ctx)
	{
		return StaticLib.get_String_data_from_shared_preference(ctx, "ANS_SEND_UID");
	}
	
	// Get and set uid to send answers
	final public static void setAnsSendUid(Context ctx, String uid)
	{
		StaticLib.write_String_to_shared_preference(ctx, "ANS_SEND_UID", uid);
	}
	
	final public static String getUidSettedToSendAns(Context ctx)
	{
		return StaticLib.get_String_data_from_shared_preference(ctx, "ANS_SEND_UID");
	}
	
	// Send Answer
	final public static void sendAnswer(Context ctx, int i)
	{
		final String uidToSendAns=StaticLib.getUidSettedToSendAns(ctx);
		if (StaticLib.checkForProperString(uidToSendAns)==false)
		{
			StaticLib.print(ctx, "Invalid Remote's Ans UID");
			StaticLib.setAnsSendUid(ctx, "");
			return;
		}
		final FirebaseDatabase database=FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString());
		final DatabaseReference ref=database.getReference("ans/" + uidToSendAns);
		
		ref.setValue(0);
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ref.setValue(i);
					
					if (i >0)
					{
						StaticLib.print(ctx, String.valueOf(i));
					}
					if(i<0)
					{
						StaticLib.print(ctx, "TEST");
					}
					
				}
			}, 120); //Timer is in ms here. //IMPORTANT
}

	
	// Accept Request
	final public static void acceptRequest(Privilages prvlg, String uid, Context ctx)
	{
		//print(ctx, "test"+usr.prvlg.write+usr.prvlg.read);
		if(prvlg!=null){
		if(prvlg.read!=null){
		if (prvlg.read==true)
		{
			StaticLib.permitUidToReadRmt(uid, ctx);
		}
		else
		{
			StaticLib.denyUidToReadRmt(uid, ctx);
		}}
		
		if(prvlg.write!=null){
		if (prvlg.write==true)
		{
			StaticLib.permitUidToWriteRmt(uid, ctx);
		}
		else
		{
			StaticLib.denyUidToWriteRmt(uid, ctx);
		}}
		StaticLib.rmvThisUidFrmRqs(uid, ctx);
		}
	}
	
	// Reject Request
	final public static void rejectAllRequests(String uid, Context ctx)
	{
		StaticLib.denyUidToReadRmt(uid, ctx);
		StaticLib.denyUidToWriteRmt(uid, ctx);
		StaticLib.rmvThisUidFrmRqs(uid, ctx);
		StaticLib.rmvThisUidFrmPrvlg(uid, ctx);
	}
		
	final public static void rmvThisUidFrmRqs(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.rqs).toString()).child(StaticLib.getUid(ctx)).child(uid).setValue(null);
		}
	}
	
	final public static void rmvThisUidFrmPrvlg(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).setValue(null);
		}
	}
	
	// Permission granter
	final public static void rmvUidFrmPrvlgs(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).setValue(null);
		}
	}
	final public static void permitUidToReadRmt(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).child(ctx.getText(R.string.read).toString()).setValue(true);
		}
	}
	final public static void denyUidToReadRmt(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).child(ctx.getText(R.string.read).toString()).setValue(false);
		}
	}

	final public static void permitUidToWriteRmt(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).child(ctx.getText(R.string.write).toString()).setValue(true);
		}
	}
	final public static void denyUidToWriteRmt(String uid, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !uid.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx)).child(uid).child(ctx.getText(R.string.write).toString()).setValue(false);
		}
	}
	
	// Request Senders firebase
	final public static void requestReadPermissionOfRemote(String rmtKey, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !rmtKey.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.rqs).toString())
			.child(rmtKey)
			.child(StaticLib.getUid(ctx))
			.child(ctx.getText(R.string.read).toString())
			.setValue(true);
		}
	}
	final public static void requestWritePermissionOfRemote(String rmtKey, Context ctx)
	{
		if(StaticLib.isLogined(ctx)==true && !rmtKey.equals(""))
		{
			DatabaseReference ref = FirebaseDatabase.getInstance(ctx.getText(R.string.db_uri).toString()).getReference();
			ref.child(ctx.getText(R.string.rqs).toString())
			.child(rmtKey)
			.child(StaticLib.getUid(ctx))
			.child(ctx.getText(R.string.write).toString())
			.setValue(true);
		}
	}
	
	// login checker needed implementation
	final public static boolean isLogined(Context ctx)
	{
		if(StaticLib.get_boolean_data_from_shared_preference(ctx, "LOGINED")==true)
		{
			try
			{
			if (FirebaseAuth.getInstance().getCurrentUser() == null)
			{
				return false;
			}
			else
			{
				return true;
			}
			}
			catch (Exception e)
			{
				StaticLib.printException(ctx, e);
			}
		}
		
		return false;
	}
	
	public static Integer txtToInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
		ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

		String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(),  Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
		if (enabledServicesSetting == null)
			return false;

		TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
		colonSplitter.setString(enabledServicesSetting);

		while (colonSplitter.hasNext()) {
			String componentNameString = colonSplitter.next();
			ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

			if (enabledService != null && enabledService.equals(expectedComponentName))
				return true;
		}

		return false;
		/*
		 //Make a callback:
		 
		 ContentObserver observer = new ContentObserver() {
		 @Override
		 public void onChange(boolean selfChange) {
		 super.onChange(selfChange);
		 boolean accessibilityServiceEnabled = isAccessibilityServiceEnabled(context, MyAccessibilityService.class);
		 //Do something here
		 }
		 };
		 
		 //Subscribe:
		 
		 Uri uri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
		 context.getContentResolver().registerContentObserver(uri, false, observer);
		
		 //Unsubscribe when you're done:
		 
		 context.getContentResolver().unregisterContentObserver(observer);
		*/
	}
	
	
	private static String uriConnect(String uri) throws MalformedURLException, IOException, SocketTimeoutException
	{
		String fullString = "";
		URL url = new URL(uri);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		while ((line = reader.readLine()) != null)
		{
			fullString += line;
		}
		reader.close();
		return fullString;
		
		/*public static String getTextFromUrl(String uri){
		 String fullString = "";
		 try {
		 URL yahoo = new URL(uri);
		 BufferedReader in = new BufferedReader(
		 new InputStreamReader(
		 yahoo.openStream()));
		 String inputLine;
		 while ((inputLine = in.readLine()) != null)
		 fullString += inputLine;
		 in.close();
		 }
		 catch (Exception e) {
		 //  e.printStackTrace();
		 return fullString;
		 }
		 return fullString;
		 }*/
	}
	public static long getEpoch(){
		// Dont update ui this will run on worker thread
		long ll=0;
		String val="";
		
	    String url="https://currentmillis.com/time/minutes-since-unix-epoch.php";
		
		try
		{
			val=uriConnect(url) ;
		}
		catch (MalformedURLException e)
		{
			//e.printStackTrace();
		}
		catch (SocketTimeoutException e1)
		{
			//e1.printStackTrace();
		}
		catch (IOException e2)
		{
			//e2.printStackTrace();
		}
		//val=getTextFromUrl(url); 
		
		if (!val.equals("")){
		try
		{
		ll= Long.valueOf(val);
		}
		catch (NumberFormatException nfe)
		{
			return 0;
	    }
	}
	return ll;
  }
  
 
}
