package com.andev.template;

import androidx.annotation.RequiresApi;

import android.accessibilityservice.*;
//import android.accessibilityservice.AccessibilityService;
//import android.accessibilityservice.GestureDescription;

import android.view.accessibility.*;
//import android.view.accessibility.AccessibilityEvent;

import android.content.*;
//import android.content.Intent;

import android.widget.*;

import android.graphics.*;
//import android.graphics.Path;

import android.view.KeyEvent;

import android.os.Build;


public class AccessibilityClick extends AccessibilityService
{

// Accessibility's PlayTap Broadcast Receiver Class
	public class AccessibilityBroadcastReceiver extends BroadcastReceiver
	{
		public boolean isRegistered = false; 

public AccessibilityBroadcastReceiver()
{
}
		final public void register()
		{
			if (this.isRegistered == false)
			{
					registerReceiver(this, new IntentFilter("DISPATCH_CMD"));
					isRegistered = true;
			}
		}

		final public void unregister()
		{
			if (this.isRegistered==true)
			{
					unregisterReceiver(this);
					isRegistered = false;
			}
		}
		@Override    
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getBooleanExtra("DISABLE_SELF", false)==true)
			{
				disableService();
			}
			else
			{
				playTap(intent.getIntExtra("x", 0), intent.getIntExtra("y", 0));
			}
		}    

	}

	public static AccessibilityBroadcastReceiver myAccessibilityBroadcastReceiver;
	public Context ctx;
	private static boolean serviceRunningStatus= false;
	
	// INHERITED METHODS
	@Override
	public void onCreate()
	{
		ctx=this;
		myAccessibilityBroadcastReceiver = new AccessibilityBroadcastReceiver();
		registerBroadcast();
		serviceRunningStatus = true; 
		
		super.onCreate();
	}

	@Override
	protected void onServiceConnected()
	{
		registerBroadcast();
		serviceRunningStatus = true; 
		
		super.onServiceConnected();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
		//return START_NOT_STICKY;
	}
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event){}

	@Override
	public void onInterrupt(){}

	@Override
	public boolean onUnbind(Intent intent)
	{
		serviceRunningStatus= false;
		unregisterBroadcast();
		
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy()
	{
		unregisterBroadcast();
		serviceRunningStatus= false;
		myAccessibilityBroadcastReceiver = null;
		
		super.onDestroy();
	}
	
	
	//Register Broadcast
	private void registerBroadcast()
	{
		if (myAccessibilityBroadcastReceiver == null)
		{
			myAccessibilityBroadcastReceiver = new AccessibilityBroadcastReceiver();
			if(myAccessibilityBroadcastReceiver.isRegistered==false)
			{
				myAccessibilityBroadcastReceiver.register();
			}
		}
		else
		{
			if(myAccessibilityBroadcastReceiver.isRegistered==false)
			{
				myAccessibilityBroadcastReceiver.register();
			}
		}
	}

	//Unregister Broadcast
	private void unregisterBroadcast()
	{
		if (myAccessibilityBroadcastReceiver == null)
		{
			return;
		}
		else
		{
			if(myAccessibilityBroadcastReceiver.isRegistered==true)
			{
				myAccessibilityBroadcastReceiver.unregister();
			}
		}
	}
	
	//Click Dispatcher Method
@RequiresApi(api = Build.VERSION_CODES.N)
  final  private void playTap(int x, int y)
	{
        Path swipePath = new Path();
        swipePath.moveTo(x, y);
        swipePath.lineTo(x, y);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 10));
        dispatchGesture(gestureBuilder.build(), new GestureResultCallback() {
				@Override
				public void onCompleted(GestureDescription gestureDescription)
				{
					super.onCompleted(gestureDescription);
				}
				@Override
				public void onCancelled(GestureDescription gestureDescription)
				{
					super.onCancelled(gestureDescription);
				}
			}, null);
    }

final void disableService()
{
disableSelf();
}
	
	final public static boolean getAccessibiltyRunningStatus(Context ctx)
	{
		if (myAccessibilityBroadcastReceiver==null)
		{
			StaticLib.print(ctx, "broadcast reciever is null");
			return false;
		}
		else if (myAccessibilityBroadcastReceiver.isRegistered==false)
		{
			StaticLib.print(ctx, "broadcast receiver not registered");
			return false;
		}
		else if (serviceRunningStatus==false)
		{
			StaticLib.print(ctx, "Accessibility status not running");
			return false;
		}
		else
		{
		return serviceRunningStatus;
		}
	}
	
	/*
	@Override
	protected boolean onKeyEvent(KeyEvent event)
	{
		if(false){
		if (StaticLib.get_boolean_data_from_shared_preference(this, "BIND_KEY")==true)
		{
			int action = event.getAction();
			int keyCode = event.getKeyCode();
			switch (keyCode)
			{
				case KeyEvent.KEYCODE_VOLUME_DOWN:
					if (action == KeyEvent.ACTION_DOWN)
					{
						if (StaticLib.get_boolean_data_from_shared_preference(this, "BIND_VOL_DOWN_KEY")==true)
						{
							//TODO
							start_for_service();
							registerBroadcast();
							print_everywhere("SERVICE_STATUS_ACTIVE_VOL_DOWN");
							
						}
					}
					break;

				case KeyEvent.KEYCODE_VOLUME_UP:
					if (action == KeyEvent.ACTION_DOWN)
					{
						if (StaticLib.get_boolean_data_from_shared_preference(this, "BIND_VOL_UP_KEY")==true)
						{
							//TODO
							start_for_service();
							registerBroadcast();
							print_everywhere("SERVICE_STATUS_ACTIVE_VOLUM_UP");
							
						}
					}
					break;
			}
		}
		}//false
		return super.onKeyEvent(event);
		//return false;
	}*/

}

