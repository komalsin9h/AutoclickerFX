package com.andev.template;



//Needed
//import com.google.firebase.auth.FirebaseAuth;
import android.view.View;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.Gravity;
import android.graphics.PixelFormat;
import java.util.ArrayList;
import android.widget.Button;
import java.util.Random;
import android.content.Context;
import android.view.View.OnClickListener;

//import com.google.android.material.button.MaterialButton;
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
//import android.os.Looper;


import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;


/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
*/

public class RemoteForService extends Service
{
	public boolean RUNNING_STATUS=false;
	private Context ctx;
	
	
	public static final String CHANNEL_ID = "FRC"; //CONSTANT //FloatingRemoteChannel
	
	public int NOTIF_ID=2;
	public NotificationManager manager;
	public Notification notification;
	//public Handler handler;
	//public FirebaseDatabase database;
	//public String uid;
	public Random random;
	public int layout_parms;
	private ArrayList<View> btnArr=new ArrayList<View>();
	//private DatabaseReference myRef;
	private View testBtn;
	
	@Override
	public void onCreate()
	{
		super.onCreate();

		ctx = this;
		//handler = new Handler(Looper.getMainLooper());	//key = getText(R.string.key).toString();
		manager = getSystemService(NotificationManager.class);
		random = new Random();
		testBtn=newTestBtn(); //TEST BUTTON INTIALIZATION
		//database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
		//uid = StaticLib.getUidSettedToSendAns(ctx);
		notification = new NotificationCompat.Builder(ctx, CHANNEL_ID) 
			.setNotificationSilent() 
			.setPriority(NotificationCompat.PRIORITY_MIN)
			.setContentTitle("FLOATING REMOTE")
			.setContentText("Remote is active!")
			.setSmallIcon(R.drawable.floatin_remote_ico) 
			.build();
			
			/*
		if (StaticLib.checkForProperString(uid)==false)
		{
			// Setting Default Logined UID
			uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			print("No Proper Remote UID Is Specified, Using Default Logined UID For Sending Clicks");
		}
		myRef = database.getReference("ans/" + uid);	//keep it behind uid initialization else it will crash //myRef = database.getReference("ans/" + (StaticLib.get_String_data_from_shared_preference(ctx, "UID")));
		*/
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;}
		else
		{layout_parms = WindowManager.LayoutParams.TYPE_PHONE;}
		createNotificationChannel();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		if (RUNNING_STATUS == true)
		{
			RUNNING_STATUS = false;
			rmvFloatButtons(btnArr);
			stopForService();
		}
		else
		{
			RUNNING_STATUS = true;
			startForeground(NOTIF_ID, notification);
			//TODO Start Here
			int noOfBtns=StaticLib.get_int_data_from_shared_preference(ctx, "rmtBtns");
			btnArr=gen_btn(noOfBtns);
			addFloatButtons(btnArr);
			//sendAns(int i)
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
	
	final private void delayedQuite()
	{
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run()
				{
					stopForService();
				}
			}, 2000); //Timer
	}
	
	private void createNotificationChannel()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			NotificationChannel serviceChannel = new NotificationChannel(
				CHANNEL_ID,
				"floating_remote",
				NotificationManager.IMPORTANCE_MIN
			);
			//myNotificationChannelName:- floating_remote
			//NotificationManager.IMPORTANCE_DEFAULT
			//NotificationManager.IMPORTANCE_HIGH
			//serviceChannel.setBypassDnd(true);
			serviceChannel.setShowBadge(false);
			//serviceChannel.setSound(null, null);
			serviceChannel.enableLights(false);
			serviceChannel.enableVibration(false);
			manager.createNotificationChannel(serviceChannel);
		}
	}
	
	final private void addFloatButtons(ArrayList<View> arr){
		create_floating(testBtn, false); // Adding Test Btn
		
		final int arrSize=arr.size();
		for (int i=0; i<arrSize; i++)
		{
			create_floating(arr.get(i), false);
		}
	}
	
	final private void rmvFloatButtons(ArrayList<View> arr){
		create_floating(testBtn, true); // Removing Test Btn
		
		final int arrSize=arr.size();
		for (int i=0; i<arrSize; i++)
		{
			create_floating(arr.get(i), true);
		}
	}
	
	private View newTestBtn(){
		Button btn= new Button(ctx);
		btn.setText("TEST");
		btn.setId(50);
		return btn;
	}
	
	private View genNewBtn(int i)
	{
		Button btn = new Button(ctx);	//btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		btn.setText("POINT " + i);
		btn.setId(i + 100); // Constant Addition To Button ID
		return btn;
	}
	
	public ArrayList<View> gen_btn(int r){
		ArrayList<View> tmp=new ArrayList<View>();
		for (int i=0; i < r; i++)
		{
			tmp.add(genNewBtn(i + 1));
		}
		return tmp;
	}
	
	public void print(String str)
	{
		StaticLib.toast(ctx, str);
	}
	
	public void stopForService()
	{
		stopForeground(true);
		stopSelf();
		stopService(new Intent(ctx, ForegroundService.class));
	}
	
	final private void create_floating(View v, boolean remove)
	{
		final View myFloatingView = v;
		final int viewid=myFloatingView.getId();
		
		final WindowManager mWindowManager = (WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE);

		if (remove == true)
		{
			try
			{
				mWindowManager.removeView(myFloatingView); //mWindowManager.removeViewImmediate(myFloatingView);
			}
			catch (Exception e)
			{
				print("unable to remove view"); //StaticLib.broadcast_msg_to_print_everywhere(this, "exception.");
			}
			return;
		}
		if (remove == false)
		{
			final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				layout_parms,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
			 
			 params.gravity = Gravity.TOP | Gravity.LEFT; 
			 //params.x = random.nextInt((700-100)+1)+100;  //nextInt((max - min) + 1) + min;
			// params.y = random.nextInt((1500-100)+1)+100;
			// if (params.x < 0)
			// {params.x = 0;}
			// if (params.y < 0)
			// {params.y = 0;}
			
			Positions pos=recallPositions(viewid);
			params.x=pos.x;
			params.y=pos.y;
			
			mWindowManager.addView(myFloatingView, params);
			
			myFloatingView.setOnTouchListener(new View.OnTouchListener(){
					private int initialX;
					private int initialY;
					private float initialTouchX;
					private float initialTouchY;
					@Override
					public boolean onTouch(View v, MotionEvent event)
					{
						switch (event.getAction())
						{
							case MotionEvent.ACTION_DOWN:
								initialX = params.x;
								initialY = params.y;
								initialTouchX = event.getRawX();
								initialTouchY = event.getRawY();
								return true;
							case MotionEvent.ACTION_MOVE:
								//int difx=(int)(event.getRawX() - initialTouchX);
								//int dify=(int)(event.getRawY() - initialTouchY);
								//this code is helping the widget to move around the screen with fingers
								params.x = initialX + (int) (event.getRawX() - initialTouchX);
								params.y = initialY + (int) (event.getRawY() - initialTouchY);
								mWindowManager.updateViewLayout(myFloatingView, params);
								//if(difx>10 || dify>10){return true;}
								return true;
							case MotionEvent.ACTION_UP:
								//get_params(event.getRawX(), event.getRawY(), myFloatingView.getId());
								//get_params(params.x + x_correction, params.y + y_correction, i, ii);
								//print("t");
								sendAnsFromBtnId(viewid);
								rememberPositions(viewid, new Positions(params.x, params.y));
								return true;
						}
						return false;
					}
				});
		}
	}

final private void sendAnsFromBtnId(int i)
	{
		if (i==50) //Test Button Detection Having Constant ID 50
		{
			sendAns(-1);
		}
		else
		{
			sendAns(i-100); //100 is constant addition in btn ids
		}
	}
	
	
	
	final public void rememberPositions(int viewid, Positions p)
	{
		StaticLib.write_int_to_shared_preference(ctx, "rmt_float_pos_x_"+viewid, p.x);
		StaticLib.write_int_to_shared_preference(ctx, "rmt_float_pos_y_"+viewid, p.y);
	}
	
	final public Positions recallPositions(int viewid)
	{
		Positions p=new Positions(0, 0);
		p.x=StaticLib.get_int_data_from_shared_preference(ctx, "rmt_float_pos_x_"+viewid);
		p.y=StaticLib.get_int_data_from_shared_preference(ctx, "rmt_float_pos_y_"+viewid);
		
		if (p.x>0 && p.y>0)
		{
			return p;
		}
		else
		{
			p=new Positions(random.nextInt((700-100)+1)+100, random.nextInt((700-100)+1)+100);
			return p;
		}
	}
	
	
	final private void sendAns(int i)
	{
		StaticLib.sendAnswer(ctx, i);
	}
	
	
	// INNER CLASS
	final private class Positions
	{
		public int x=0;
		public int y=0;
		
		Positions()
		{}
		Positions(int xx, int yy)
		{
			this.x=xx;
			this.y=yy;
		}
	}
	
	
}















