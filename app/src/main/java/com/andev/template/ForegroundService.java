package com.andev.template;

import androidx.annotation.Nullable;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
//import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.annotation.NonNull;
import android.os.Handler;
import android.os.Looper;
import android.content.Context;

import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForegroundService extends Service
{
	private Context ctx;
	public static final String CHANNEL_ID = "FSC"; //CONSTANT //ForegroundServiceChannel
	public boolean RUNNING_STATUS=false;
	public int NOTIF_ID=1;

	public NotificationManager manager;
	public Notification notification;
	public Handler handler;

	public FirebaseDatabase database;
	public DatabaseReference db_ans_uid;
	public ValueEventListener listener;

	
	public boolean clicking_currently=false;
	final public boolean clicking_currently_get(){return clicking_currently;}
	final public void clicking_currently_set(boolean b){clicking_currently=b;}
	
	public int clickValue=0;
	final public int clickValue_get(){return clickValue;}
	final public void clickValue_set(int b){clickValue=b;}
	
	@Override
	public void onCreate()
	{
		super.onCreate();

		ctx = this;
		handler = new Handler(Looper.getMainLooper());	//key = getText(R.string.key).toString();
		manager = getSystemService(NotificationManager.class);

		createNotificationChannel();
		//Intent notificationIntent = new Intent(getApplicationContext(), ForegroundService.class); //MainActivity.class
		//PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
		//setContentIntent(pendingIntent)													PendingIntent.FLAG_IMMUTABLE);
	
		/*	notification = new NotificationCompat.Builder(ctx, CHANNEL_ID)
			.setContentTitle(getText(R.string.not_title))
			.setContentText(getText(R.string.not_text))
		    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setOnlyAlertOnce(true)
		    .setSmallIcon(R.drawable.n_r) 
			.build();*/
			
		notification = new_not(getText(R.string.not_title).toString(), getText(R.string.not_text).toString(), R.drawable.n_r,false);
	
		database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
		db_ans_uid = null;
		listener = null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		if (RUNNING_STATUS == true)
		{
			RUNNING_STATUS = false;
			stopForService();
		}
		else
		{
			RUNNING_STATUS = true;
			startForeground(NOTIF_ID, notification);
			database.goOnline();

			final String rq=StaticLib.forSerChkReq(ctx);
			if (rq.replaceAll("\\s+", "").equals("requirements"))
			{
				db_ans_uid = database.getReference("ans/" + (StaticLib.get_String_data_from_shared_preference(ctx, "ANS_RECEIVE_UID")));

				listener = new ValueEventListener()
				{
					@Override
					public void onCancelled(@NonNull DatabaseError error)
					{
						print("" + error);
						delayedQuite();
					}
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshot)
					{
						Integer value = snapshot.getValue(Integer.class);
						//Integer value = snapshot.getValue();
						
						 parse_data(value);
					
						//print("" + value);
					}
				};
				db_ans_uid.addValueEventListener(listener);
			}
			else
			{
				print(rq);
				delayedQuite();
			}
		}
		return START_NOT_STICKY;
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		stopForService();
	}
	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private void delayedQuite()
	{
		handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					stopForService();
				}
			}, 2000);
	}
	private void createNotificationChannel()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			NotificationChannel serviceChannel = new NotificationChannel(
				CHANNEL_ID,
				"mNghCN",
				NotificationManager.IMPORTANCE_DEFAULT
			);
			//myNotificationChannelName:- mNCN
			//NotificationManager.IMPORTANCE_DEFAULT
			//NotificationManager.IMPORTANCE_HIGH

			//serviceChannel.setBypassDnd(true);
			serviceChannel.setShowBadge(false);
			serviceChannel.setSound(null, null);
			serviceChannel.enableLights(false);
			serviceChannel.enableVibration(false);
			manager.createNotificationChannel(serviceChannel);
		}
	}
	private Notification new_not(String title, String content, int rid, boolean silent)
	{

		if (silent == true)
		{
			return new NotificationCompat.Builder(ctx, CHANNEL_ID) 
		    	.setNotificationSilent() 
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentTitle(title.toString())
				.setContentText(content.toString())
				.setSmallIcon(rid) 
				.build();
		}
		else
		{
			return new NotificationCompat.Builder(ctx, CHANNEL_ID)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentTitle(title.toString())
				.setContentText(content.toString())
				.setSmallIcon(rid) 
				.build();}
	}
	public void updateNotification(String title, String content, int rid)
	{
		if (rid == R.drawable.n_r)
		{
			Notification notification = new_not(title, content, rid, false);
			manager.notify(NOTIF_ID, notification);
		}
		else
		{
			Notification notification = new_not(title, content, rid, true);
			manager.notify(NOTIF_ID, notification);
		}
	}
	public void parse_data(Integer value)
	{
		handler.post(new Runnable() {
				@Override
				public void run()
				{
					//Do something
					clickValue_set(value);
					
					//print(""+clicking_currently_get());
					
					if(clicking_currently_get()==false)
					{
						//clicking_currently_set(false);
						parse_click();
					}
					else
					{if(clicking_currently_get()==true){
						set_repeatClicking(true);
				    }}
			
				

					handler.postDelayed(new Runnable() {
							@Override
							public void run()
							{
								//Do something
								updateNotification(getText(R.string.not_title).toString(), getText(R.string.not_text).toString(), R.drawable.n_r);
							}
						}, 2000);

					updateNotification(getText(R.string.not_title).toString(), "Response: " + value, R.drawable.n_g);
				}
			});
		//Toast.makeText(this, "" + value, Toast.LENGTH_SHORT).show();
	}


	public void parse_click()
	{
		clicking_currently_set(true);
		
		int v=clickValue_get();
		//Integer v=clickValue_get();
		
		final ArrayList<ArrayList<ArrayList<Integer>>> arr = arr();
		final int optLength=arr.get(0).get(0).size();
		
		if (v<0)
		{
			print("Running: Yes");
			clicking_currently_set(false);
			return;
		}
		else if(v==0)
		{
			//print(v+" ");
			clicking_currently_set(false);
			return;
		}
		else if(optLength<v)
		{
			print("This Received Click Point Not Enabled: "+optLength+"\nReceived Click Point From Server: "+v );
			clicking_currently_set(false);
			return;
		}
		else
		{
			if(v>0 && v<=optLength){
				
				/*final int sd=get_s_id(); //incrementing clicking session id
				set_s_id(sd+1);
				final int sid=get_s_id();*/
				
			int time=StaticLib.get_int_data_from_shared_preference(ctx, "SPEED");
			final int cycle=StaticLib.get_int_data_from_shared_preference(ctx, "CYCLES");
			
			if(time<1){time=1; print("Very Small Delay");}
			
			final int winSize=arr.get(0).size()+1;
			
			delay_setter(time); //initializing delay global var
				
				
			for (int i=1; i<winSize; i++)
			{
			
				final int r=i; //win loop counter val
				
				    for (int c=1; c<cycle+1; c++)
				    {
						
					int dd=delay_getter();
					
				    handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								int ans=clickValue_get();
								
								if(clicking_currently_get()==true && ans>0)
									{
									//print("winNo."+r+" optNo."+ans+" delay"+dd);
									click(getArrInt(1, r, ans, arr), getArrInt(2, r, ans, arr)); //first strike												
							        }
							}}, dd); //clicking soeed on same opt
							
					
					delay_setter(time+dd);
					
				    }
	
				  
				if ((winSize-r)<2)
					{
					final int delayToSetTrue = delay_getter()+15;
						handler.postDelayed(new Runnable()
						    {
								@Override
								public void run()
								{
						           clicking_currently_set(false);
								
								   if(get_repeatClicking()==true)
								{
									set_repeatClicking(false);
									parse_click();
								}
									//print("trued: "+delayToSetTrue);
								   
								}}, delayToSetTrue);
					}
					
			}	//for end
			
			}  //if end
			
		} //else end
		

	}
	
	private boolean repeatClicking=false;
	final public boolean get_repeatClicking()
	{
		return repeatClicking;
	}
	final public void set_repeatClicking(boolean b)
	{
		repeatClicking=b;
	}
	
	
	/*
	private int click_session_id=0;
	final public void set_s_id(int id){
		click_session_id=id;
	}
	final public int get_s_id(){
		return click_session_id;
	}*/
	
	
	
	private int delayIncremental=0;
	final public void delay_setter(int i){delayIncremental=i;}
	final public int delay_getter(){return delayIncremental;}
	
	public void print(String str)
	{
		StaticLib.toast(ctx, str);
	}
	public void click(int x, int y)
	{
		//print("X: " + x + " Y:" + y);
		StaticLib.broadcast_click(getApplicationContext(), x, y);
	}

	final static Type listType = new TypeToken<ArrayList<ArrayList<ArrayList<Integer>>>>(){}.getType(); 
	final static Gson Gos = new Gson();
	public static ArrayList<ArrayList<ArrayList<Integer>>> json_to_arr(String str)
	{
		ArrayList<ArrayList<ArrayList<Integer>>> ob = Gos.fromJson(str, listType);
		return ob;
	}
	private ArrayList<ArrayList<ArrayList<Integer>>> arr()
	{
		final String json=StaticLib.get_String_data_from_shared_preference(ctx, "CORD");
		final ArrayList<ArrayList<ArrayList<Integer>>> arr=json_to_arr(json);
		return arr;
	}
	private int getArrInt(int x_y, int ap, int opt, ArrayList<ArrayList<ArrayList<Integer>>> arr)
	{
		return arr.get(x_y - 1).get(ap - 1).get(opt - 1);
	}

	public void stopForService()
	{
		if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
		{

			database.goOffline();

			if (db_ans_uid != null)
			{
				db_ans_uid.removeEventListener(listener);
				listener = null;
				db_ans_uid = null;
			}
			database.goOffline();
			stopForeground(true);
		}
		stopSelf();
		stopService(new Intent(ctx, ForegroundService.class));
	}
}















