package com.andev.template;

import android.view.Gravity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import android.widget.LinearLayout;
//import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.widget.CompoundButton;
//import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot; 
import com.google.firebase.database.DatabaseError; 
import com.google.firebase.database.ValueEventListener; 

public class RemoteRequestsFragment extends Fragment {

	public Context ctx;
	public FirebaseDatabase database;
	public DatabaseReference ref;
	public ValueEventListener rqsChangeListener;
	public View rootView;
	public Handler handler;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx=getActivity();
		
		rootView = inflater.inflate(R.layout.fragment_layout_holder, container, false);
		database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
		ref = database.getReference().child(getText(R.string.rqs).toString()).child(StaticLib.getUid(ctx));
		handler = new Handler(Looper.getMainLooper());
		rqsChangeListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				ArrayList<User> users = new ArrayList<User>();

				for(DataSnapshot ds : dataSnapshot.getChildren()) {
					if (ds != null)
					{
					User usr=new User((String) ds.getKey());
					try
					{
						usr.prvlg = (Privilages) ds.getValue(Privilages.class);
					}
					catch (Exception e)
					{
						//StaticLib.printException(ctx, e);
					}				
					users.add(usr);
					//StaticLib.print(ctx, "event occured"+usr.prvlg.read); //testing only
					}
       			 	synRqs(users);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				StaticLib.print(ctx, databaseError.getMessage());
			}
		};
		return rootView;
    }
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		}
	@Override
	public void onResume() {
		// TODO
		ref.addValueEventListener(rqsChangeListener);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO
		ref.removeEventListener(rqsChangeListener);
		super.onPause();
	}
	
	final public void synRqs(ArrayList<User> users)
	{
		if(users.size()>0){
		final LinearLayout containerLayout = (LinearLayout) rootView.findViewById(R.id.holder);
		containerLayout.setGravity(Gravity.CENTER);
		containerLayout.removeAllViews(); // Clearing container
		MaterialTextView header=new MaterialTextView(ctx);
		header.setText("YOUR ALL PENDING REQUESTS");
		header.setTextColor(Color.parseColor("#dd0000"));
		header.setTextSize(20);
		containerLayout.addView(header);
		
		for(int i=0; i<users.size(); i++)
		{
			containerLayout.addView(generateUsrView(users.get(i)));
		}
containerLayout.setVisibility(View.VISIBLE);
		}
	}
	
	final public View generateUsrView(User usr)
	{
		final LinearLayout containerLayout = (LinearLayout) rootView.findViewById(R.id.holder);
		containerLayout.setGravity(Gravity.CENTER);
		final int delay=300;
		final LinearLayout mainl=new LinearLayout(ctx);
		mainl.setBackgroundResource(R.drawable.border);
		mainl.setOrientation(LinearLayout.VERTICAL);
		mainl.setGravity(Gravity.CENTER);
		MaterialTextView tv=new MaterialTextView(ctx);
		tv.setText(usr.uid);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(10);
		mainl.addView(tv);
		LinearLayout childl=new LinearLayout(ctx);
		childl.setOrientation(LinearLayout.HORIZONTAL);
		childl.setGravity(Gravity.CENTER);
		
		// Resetting Switch 
		switchState.read=false;
		switchState.write=false;
		
		// Read Switch
		final SwitchMaterial sread=new SwitchMaterial(ctx);
		sread.setText("Read:");
		sread.setTextSize(9);
		if(usr.prvlg.read!=null)
		{
			if(usr.prvlg.read==true)
			{
				switchState.read=true;
			}
	}
	sread.setChecked(switchState.read);
	sread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked==true)
						{
							switchState.read=true;
						}
						else
						{
							switchState.read=false;
						}
				}
			});
		childl.addView(sread);
		
	
		// Write Switch
		final SwitchMaterial swrite=new SwitchMaterial(ctx);
		swrite.setText("Write:");
		swrite.setTextSize(9);
		if(usr.prvlg.write!=null)
		{
			if(usr.prvlg.write==true)
			{
				switchState.write=true;
			}
	}
	swrite.setChecked(switchState.write);
	swrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked==true)
						{
							switchState.write=true;
						}
						else
						{
							switchState.write=false;
						}
				}
			});
		childl.addView(swrite);
		
		/*
		if(usr.prvlg.write!=null)
		{
			if(usr.prvlg.write==true)
			{
		SwitchMaterial swrite=new SwitchMaterial(ctx);
		swrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked==true)
					{
						// TODO Allow uid to write in rqs
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						//
						}}, delay);
					}
					else
					{
						// TODO Deny uid to write in rqs
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						//
						}}, delay);
					}
				}
			});
		swrite.setText("Write:");
		swrite.setChecked(false);
		swrite.setTextSize(9);
		swrite.setChecked(true);
		childl.addView(swrite);
			}
		}
		*/
		
		
		
		
		
		
		MaterialButton accept= new MaterialButton(ctx);
		accept.setText("Accept");
		accept.setTextColor(Color.parseColor("#00ff00"));
		accept.setTextSize(10);
		accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accept rqs
                handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
                StaticLib.acceptRequest(switchState, usr.uid, ctx);
                containerLayout.removeView(mainl);
                if (containerLayout.getChildCount() == 1) {
          			  containerLayout.removeAllViews();
          containerLayout.setVisibility(View.GONE);
        		   } 
						}}, delay);
            }
        });
		childl.addView(accept);
		MaterialButton reject = new MaterialButton(ctx);
		reject.setText("Reject");
		reject.setTextColor(Color.parseColor("#ff2211"));
		reject.setTextSize(10);
		reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reject rqs
                handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
                StaticLib.rejectAllRequests(usr.uid, ctx);
                containerLayout.removeView(mainl);
                if (containerLayout.getChildCount()==1) {
          			  containerLayout.removeAllViews();
          containerLayout.setVisibility(View.GONE);
        		   } 
						}}, delay);
            }
        });
		childl.addView(reject);
		mainl.addView(childl);
		return mainl;
		//layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	Privilages switchState=new Privilages(false, false);
}
