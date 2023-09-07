package com.andev.template;

import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.Button;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class AccessibilityPermissionActivity extends AppCompatActivity {
	public Context ctx;	
	public Button btn;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_accessibilty_permission);
	ctx = this;
	btn = (Button) findViewById(R.id.btn);
	btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
			}
		});
	refreshPermissionStatus();
	}
		
	@Override
	protected void onPause(){
		super.onPause();

	}
	@Override
	protected void onResume(){
		super.onResume();
		refreshPermissionStatus();
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();

	}
		
		public void refreshPermissionStatus()
		{
			if(StaticLib.isAccessibilityServiceEnabled(ctx, AccessibilityClick.class)==true)
			{setEnabled();}
			else
			{setDisabled();}
		}
		public void setEnabled(){
			btn.setTextColor(0xFF00FF00);
			btn.setText("DISABLE ACCESSIBILTY SERVICE");
		}
		public void setDisabled(){
			btn.setTextColor(0xFFFF0000);
			btn.setText("ENABLE ACCESSIBILTY SERVICE");
		}
}
