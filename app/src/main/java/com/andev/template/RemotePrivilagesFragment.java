package com.andev.template;

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

public class RemotePrivilagesFragment extends Fragment {

	public Context ctx;
	public FirebaseDatabase database;
	public DatabaseReference ref;
	public ValueEventListener prvlgChangeListener;
	public View rootView;
	public Handler handler;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx=getActivity();
		
		rootView = inflater.inflate(R.layout.fragment_layout_holder, container, false);
		database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
		ref = database.getReference().child(getText(R.string.prvlg).toString()).child(StaticLib.getUid(ctx));
		handler = new Handler(Looper.getMainLooper());
		prvlgChangeListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				//StaticLib.print(ctx, "null"+dataSnapshot); //testing only
				
				if(dataSnapshot!=null)
				{
				ArrayList<User> users = new ArrayList<User>();
				for(DataSnapshot ds : dataSnapshot.getChildren()) {
					if (ds != null)
					{
					User usr=new User((String) ds.getKey());
					usr.prvlg= (Privilages) ds.getValue(Privilages.class);				
					users.add(usr);
					//StaticLib.print(ctx, "event occured"); //testing only
					}
							synPrvlgs(users);
				}
				}// dataSnapshot if end
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
		ref.addValueEventListener(prvlgChangeListener);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO
		ref.removeEventListener(prvlgChangeListener);
		super.onPause();
	}
	
	final public void synPrvlgs(ArrayList<User> users)
	{
		final LinearLayout containerLayout = (LinearLayout) rootView.findViewById(R.id.holder);
		containerLayout.removeAllViews(); // Clearing container
		
		for(int i=0; i<users.size(); i++)
		{
			containerLayout.addView(generateUsrView(users.get(i), i));
		}
	}
	final public View generateUsrView(User usr, int totalUsrs)
	{
		final int delay=300;
		
		LinearLayout mainl=new LinearLayout(ctx);
		mainl.setBackgroundResource(R.drawable.border);
		mainl.setOrientation(LinearLayout.VERTICAL);
		
		MaterialTextView tv=new MaterialTextView(ctx);
		tv.setText(usr.uid);
		mainl.addView(tv);
		
		LinearLayout childl=new LinearLayout(ctx);
		childl.setOrientation(LinearLayout.HORIZONTAL);
		
		// Switch Read
		SwitchMaterial sread=new SwitchMaterial(ctx);
		sread.setText("Read:");
		sread.setChecked(false);
		sread.setTextSize(9);
		if(usr.prvlg.read!=null)
		{
			if(usr.prvlg.read==true)
			{
				sread.setChecked(true);
			}
		}
		sread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked==true)
					{
						// TODO Allow uid to read
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						StaticLib.permitUidToReadRmt(usr.uid, ctx);
						}}, delay);
					}
					else
					{
						// TODO Deny uid to read
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						StaticLib.denyUidToReadRmt(usr.uid, ctx);
						}}, delay);
					}
				}
			});
		
		// Switch Write
		SwitchMaterial swrite=new SwitchMaterial(ctx);
		swrite.setText("Write:");
		swrite.setChecked(false);
		swrite.setTextSize(9);
		if(usr.prvlg.write!=null)
		{
			if(usr.prvlg.write==true)
			{
				swrite.setChecked(true);
			}
		}
		swrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked==true)
					{
						// TODO Allow uid to read
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						StaticLib.permitUidToWriteRmt(usr.uid, ctx);
						}}, delay);
					}
					else
					{
						// TODO Deny uid to read
						handler.postDelayed(new Runnable() {
   			 @Override
    			public void run() {
						StaticLib.denyUidToWriteRmt(usr.uid, ctx);
						}}, delay);
					}
				}
			});
		
		MaterialButton delete = new MaterialButton(ctx);
		delete.setText("Delete");
		delete.setTextColor(Color.parseColor("#ff2211"));
		delete.setTextSize(9);
		delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete uid in prvlg
                StaticLib.rmvUidFrmPrvlgs(usr.uid, ctx);
                if (totalUsrs<1)
					{
						final LinearLayout containerLayout = (LinearLayout) rootView.findViewById(R.id.holder);
						containerLayout.removeAllViews(); 
					}
            }
        });
		childl.addView(sread);
		childl.addView(swrite);
		childl.addView(delete);
		mainl.addView(childl);

		return mainl;
		
		//layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
	}
	
	
}
