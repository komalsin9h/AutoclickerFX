package com.andev.template;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.content.Intent;
import 	android.graphics.Color;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.view.View;
import android.provider.Settings;
import android.net.Uri;
import android.view.Gravity;
import android.view.ContextThemeWrapper;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import java.util.ArrayList;
//import android.os.Handler;
import android.os.Build;

//import com.google.firebase.auth.FirebaseAuth;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DataSnapshot; 
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.OnCompleteListener;

public class RemoteActivity extends AppCompatActivity
{
	private Context ctx;
	private ArrayList<Button> btnArr=new ArrayList<Button>();
	private LinearLayout layout;
	private TextView rmtUid;
	
	//private FirebaseDatabase database;
	//private DatabaseReference myRef;
  // private String uid;
  
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		// Note: start only when logoned true
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote);
		ctx = this;
		//database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
		//uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		//myRef = database.getReference("ans/" + (StaticLib.get_String_data_from_shared_preference(ctx, "UID")));
		//myRef = database.getReference("ans/" + uid);
		layout = (LinearLayout) findViewById(R.id.remot);
		
		//View to show remote UID
		rmtUid = (TextView) findViewById(R.id.rmtUid); 
		rmtUid.setText(StaticLib.getUidSettedToSendAns(ctx));
		
		
		final EditText ruid = (EditText) findViewById(R.id.ruid);
		Button requestToRemote = (Button) findViewById(R.id.requestToRemoteBtn);
		requestToRemote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ruid.getText().toString().matches("")) {
					print("Null UID value to send write request!");
					return;
				} else {
					StaticLib.requestWritePermissionOfRemote(ruid.getText().toString(), ctx);
					StaticLib.setAnsSendUid(ctx, ruid.getText().toString());
					rmtUid.setText(StaticLib.getUidSettedToSendAns(ctx));
				}
			}
		});
		
		
		// Manually Permissions
		final EditText clientUid = (EditText)findViewById(R.id.clientUid);
		final Button addRemoteClient = (Button) findViewById(R.id.addRemoteClient);
		addRemoteClient.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (clientUid.getText().toString() != null)
					{
						StaticLib.permitUidToReadRmt(clientUid.getText().toString(), ctx);
					}
					else
					{
						print("null value in clientUid");
					}
				}
			});
		final Button rmvRemoteClient = (Button) findViewById(R.id.rmvRemoteClient);
		rmvRemoteClient.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (clientUid.getText().toString() != null)
					{
						StaticLib.denyUidToReadRmt(clientUid.getText().toString(), ctx);
					}
					else
					{
						print("null value in clientUid");
					}
				}
			});
			
		final EditText clientUidw = (EditText)findViewById(R.id.clientUidw);
		final Button addRemoteClientw = (Button) findViewById(R.id.addRemoteClientw);
		addRemoteClientw.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (clientUidw.getText().toString() != null)
					{
						StaticLib.permitUidToWriteRmt(clientUidw.getText().toString(), ctx);
					}
					else
					{
						print("null value in clientUid");
					}
				}
			});
		final Button rmvRemoteClientw = (Button) findViewById(R.id.rmvRemoteClientw);
		rmvRemoteClientw.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (clientUidw.getText().toString() != null)
					{
						StaticLib.denyUidToWriteRmt(clientUidw.getText().toString(), ctx);
					}
					else
					{
						print("null value in clientUid");
					}
				}
			});
			
			
			// Remote Request Sender
			final EditText sendRqsEdtxt= (EditText)findViewById(R.id.sendRqsEdtxt);
			final Button sendRqsBtn = (Button) findViewById(R.id.sendRqsBtn);
			sendRqsBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (sendRqsEdtxt.getText().toString() != null)
					{
						StaticLib.requestWritePermissionOfRemote(sendRqsEdtxt.getText().toString(), ctx);
					}
					else
					{
						print("null value UID");
					}
				}
			});
			
			
		final Button gen_float_btn = (Button) findViewById(R.id.gen_float_btn);
		gen_float_btn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final EditText noOfBtn = (EditText)findViewById(R.id.noOfBtn);

					if (noOfBtn.getText().toString().matches(""))
					{
						print("You did not entered");
						return;
					}
					final int tmp = Integer.parseInt(noOfBtn.getText().toString());

					if (tmp > 0)
					{
						gen_all_float_btn(tmp);
					}
					else
					{
						print("Enter number of buttons you want to generate.");
					}
				}
			});

		final Button makeBtns = (Button) findViewById(R.id.makeBtns);
		makeBtns.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final EditText noOfBtn = (EditText)findViewById(R.id.noOfBtn);

					if (noOfBtn.getText().toString().matches(""))
					{
						print("You did not entered");
						return;
					}
					final int tmp = Integer.parseInt(noOfBtn.getText().toString());
					
					if (tmp > 0)
					{
						//rmvBtnFromLayout(btnArr);
						genBtnArr(tmp);
						addBtnToLayoutFromArr(btnArr);
					}
					else
					{
						print("Enter number of buttons you want to generate.");
					}
				}
			});


		final Button test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					sendAns(-1);
				}
			});
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view_prvlgs, RemotePrivilagesFragment.class, null)
                .commit();
        }
         /*   getFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, RemotePrivilagesFragment.class, null)
                .commit();*/
	}
	
	private void genBtnArr(int r)
	{
		ArrayList<Button> tmp=new ArrayList<Button>();
		for (int i=0; i < r; i++)
		{
			tmp.add(newBtn(i + 1));
		}
		btnArr = tmp;
	}
	private void addBtnToLayoutFromArr(ArrayList<Button> arr)
	{
		for (int i=0; i < arr.size(); i++)
		{
			final int r=i;
			layout.addView(arr.get(r));
			arr.get(r).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						sendAns(r + 1);
					}
				});
		}
	}
	private Button newBtn(int i)
	{
		Button btnTag = new Button(ctx);	//btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		btnTag.setText("POINT " + i);
		btnTag.setId(i + 100);
		return btnTag;
	}
	
	final private void gen_all_float_btn(int i)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
		start_gen_float_btn_service(i);
		//finishAffinity();
			moveTaskToBack(true); //activity.moveTaskToBack(true);
		}
		else if (Settings.canDrawOverlays(this))
		{
		start_gen_float_btn_service(i);
		//finishAffinity();
			moveTaskToBack(true); //activity.moveTaskToBack(true);
		}
		else
		{
		askPermission();
		print("Enable OVERLAY permisaion required..");
		}
	}
	
	final private void askPermission()
	{
	final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
								   Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }
    
	final public void start_gen_float_btn_service(int i)
	{
		StaticLib.write_int_to_shared_preference(ctx, "rmtBtns", i);
		
		Context context = getApplicationContext();
		Intent intent = new Intent(context, RemoteForService.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			context.startForegroundService(intent);
		} else {
		context.startService(intent);
		}
	}
	
	final private void sendAns(int i)
	{
		StaticLib.sendAnswer(ctx, i);
	}
	
	public void print(String str)
	{
		StaticLib.toast(ctx, str);
	}
	
	
}















/*
	final public void refreshCurrentUidPrevilages()
	{
		//String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		//DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
		//DatabaseReference orderRef = rootRef.child("Users").child("Customer").child(uid).child("Order");
		
		
		private void sendAns(int i)
	{
		StaticLib.sendRemoteAnswer(ctx, i);
		
		myRef.setValue(0);
		
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					myRef.setValue(i);
				}
			}, 10); //Timer is in ms here. //IMPORTANT
			
	}
		
		DatabaseReference ref = database.getReference().child("privlg").child(uid);
		ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
				@Override
				public void onComplete(@NonNull Task<DataSnapshot> task) 
				{
				if(task!=null)
			    {
					 if (!task.isSuccessful())
				   {
					  print(String.valueOf(task.getException().getMessage())); //Don't ignore potential errors!
				   }
				   else
				   {
					  //final ContextThemeWrapper newCtx = new ContextThemeWrapper(ctx, R.style.textViewWithPadding);
					  final LinearLayout uidPrvlgStatusLayout = (LinearLayout) findViewById(R.id.allowedUids);
					//  final LinearLayout allowedWriteUidsLayout = (LinearLayout) findViewById(R.id.allowedWriteUids);
					   
					 // final LinearLayout denyUidsLayout = (LinearLayout) findViewById(R.id.denyedUids);
					//  allowedUidsLayout.removeAllViews();
					 // denyUidsLayout.removeAllViews();
					  for (DataSnapshot ds : task.getResult().getChildren())
					   {
						   User usr=new User((String) ds.getKey(), (Privilages) ds.getValue(Privilages.class));
						   //uidPrvlgStatusLayout.addView();
						   
						 
						if (ds.hasChild(ctx.getText(R.string.read).toString()))
						{
						 	final Boolean value = ds.child(ctx.getText(R.string.read).toString()).getValue(Boolean.class);
							
						 	if(key!=null && value!=null)
						 	{
								 if(value==true)
								 {
									 allowedReadUidsLayout.addView(makeLayoutForPermissionLst(key, true));
								 }
								 else
								 {
									denyUidsLayout.addView(makeLayoutForPermissionLst(key, false));
								 }
							 }
							 else
						 	{
								 print("Null Pointer Exception");
							}
						 }
						 
						   if (ds.hasChild(ctx.getText(R.string.read).toString()))
						   {
							   final Boolean value = ds.child(ctx.getText(R.string.write).toString()).getValue(Boolean.class);

							   if(key!=null && value!=null)
							   {
								   if(value==true)
								   {
									   allowedWriteUidsLayout.addView(makeLayoutForPermissionLst(key, true));
								   }
								   else
								   {
									   denyUidsLayout.addView(makeLayoutForPermissionLst(key, false));
								   }
							   }
							   else
							   {
								   print("Null Pointer Exception");
							   }
						   }
						 
					  }
				   }
			 }
			 else
			 {
			   print("Task received is null");
			  }
			 }});
	}

	final public void refreshPendingRequests(){
		DatabaseReference ref = database.getReference().child("rqs").child(uid);
		ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
				@Override
				public void onComplete(@NonNull Task<DataSnapshot> task) 
				{
					if(task!=null)
					{ 
						if (!task.isSuccessful())
						{
							print(String.valueOf(task.getException().getMessage())); //Don't ignore potential errors!
						}
						else
						{
							final LinearLayout rql = (LinearLayout) findViewById(R.id.requestsLayout);
							rql.removeAllViews();
							
							for (DataSnapshot ds : task.getResult().getChildren())
							{
								final String key = ds.getKey();
								final Boolean value = ds.getValue(Boolean.class);

								if(key!=null && value!=null)
								{
									//print( key + ":" + value);
									if(value==true)
									{
								rql.addView(makeLayoutForPendingRequests(key));
									}
								}
								else
								{print("Null Pointer Exception");}
							}
						}
					}else{print("Task is null");}
				}});
	}
	
	
	final public LinearLayout makeLayoutForPendingRequests(String key)
	{
		LinearLayout ll=newHorzontalLinearLayout();
		ll.setGravity(Gravity.RIGHT);
		ll.setBackgroundResource(R.drawable.border);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)ll.getLayoutParams();
		params.setMargins(10, 10, 10, 10); 
		ll.setLayoutParams(params);

		TextView tv = new TextView(ctx);
		tv.setText(key);
		tv.setTextIsSelectable(true);
		tv.setTextSize(10);
		tv.setGravity(Gravity.CENTER);
	
			tv.setTextColor(Color.parseColor("#1070ff"));
		
		//	 tv.setBackgroundResource(R.color.white);

		TextView tvv = new TextView(ctx);
		tvv.setBackgroundResource(R.drawable.border);
			tvv.setTextColor(Color.parseColor("#00e000"));
			tvv.setText("Accept Request");
		
		tvv.setGravity(Gravity.CENTER);
		tvv.setBackgroundResource(R.drawable.border);
		tvv.setTextSize(12);
		// tvv.setBackgroundResource(R.color.white);

			tvv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						print("Accepted");
						// TODO
						//StaticLib.addRemoteClient(key, ctx);
						StaticLib.permitUidToReadRmt(key, ctx);
						removePendRequestPath(key);
						refreshRemoteLists();
					}
				});
		
		TextView tvvv = new TextView(ctx);
		tvvv.setTextColor(Color.parseColor("#e00000"));
		tvvv.setText("Delete");
		tvvv.setGravity(Gravity.CENTER);
		tvvv.setBackgroundResource(R.drawable.border);
		tvvv.setTextSize(12);
		tvvv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					print("Deleted");
					removePendRequestPath(key);
					refreshRemoteLists();
				}
			});
			
		ll.addView(tv); ll.addView(tvv); ll.addView(tvvv);
		return ll;
	}
	final public LinearLayout makeLayoutForPermissionLst(String key, boolean makeForAllowed)
	{
		LinearLayout ll=newHorzontalLinearLayout();
		ll.setGravity(Gravity.RIGHT);
		ll.setBackgroundResource(R.drawable.border);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)ll.getLayoutParams();
		params.setMargins(10, 10, 10, 10); 
		ll.setLayoutParams(params);

		TextView tv = new TextView(ctx);
		tv.setText(key);
		tv.setTextIsSelectable(true);
		tv.setTextSize(10);
		tv.setGravity(Gravity.CENTER);
		if (makeForAllowed==true)
		{
		tv.setTextColor(Color.parseColor("#00e000"));
		}else{
			tv.setTextColor(Color.parseColor("#e00000"));
		}
		//	 tv.setBackgroundResource(R.color.white);

		TextView tvv = new TextView(ctx);
		if(makeForAllowed==true)
			{
		tvv.setTextColor(Color.parseColor("#e00000"));
		tvv.setText("Deny Access");
		}else{
			tvv.setTextColor(Color.parseColor("#00e000"));
			tvv.setText("Allow Access");
		}
		tvv.setGravity(Gravity.CENTER);
		tvv.setBackgroundResource(R.drawable.border);
		tvv.setTextSize(12);
		// tvv.setBackgroundResource(R.color.white);

		if(makeForAllowed==true)
		{
		tvv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					print("denied");
					//StaticLib.rmvRemoteClient(key, ctx);
					StaticLib.denyUidToReadRmt(key, ctx);
					refreshRemoteLists();
				}
			});
			}else{
		tvv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v)
						{
							print("allowed");
							//StaticLib.addRemoteClient(key, ctx);
							StaticLib.permitUidToReadRmt(key, ctx);
							refreshRemoteLists();
						}
					});
			}
		
		TextView tvvv = new TextView(ctx);
		tvvv.setTextColor(Color.parseColor("#e00000"));
		tvvv.setText("Delete");
		tvvv.setGravity(Gravity.CENTER);
		tvvv.setBackgroundResource(R.drawable.border);
		tvvv.setTextSize(12);
		tvvv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					print("Deleted");
					removeRmtPath(key);
					refreshRemoteLists();
				}
			});
		
		ll.addView(tv); ll.addView(tvv); ll.addView(tvvv);
		return ll;
	}
	final public void removeRmtPath(String key){
		DatabaseReference reff = database.getReference();
		reff.child("rmt").child(uid).child(key).setValue(null);
	}
	final public void removePendRequestPath(String key){
	DatabaseReference reff = database.getReference();
	reff.child("rqs").child(uid).child(key).setValue(null);
	}
   final public void refreshRemoteLists(){
	refreshCurrentUidPrevilages();
	refreshPendingRequests();
   }
final public TextView newTextView(String txt)
	{
		TextView tv = new TextView(ctx);
		//tv.setText(txt);
		return tv;
	}
	final public LinearLayout newHorzontalLinearLayout()
	{
		LinearLayout ll = new LinearLayout(ctx);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}
	*/