package com.andev.template;

///////////..///// file
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import android.provider.Settings;
import android.Manifest;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.content.ActivityNotFoundException;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.View.OnClickListener;
import androidx.core.content.FileProvider;
import android.os.Handler;
import android.os.Looper;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot; 
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

import android.os.PowerManager;

import android.app.DownloadManager;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.widget.CompoundButton;
import com.google.android.material.switchmaterial.SwitchMaterial;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.IOException;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
	public Context ctx;
	public Button accbtn;
	public Handler mainHandler;
	public Button rmt; //Remote button
	public Button forbtn; //Forground Clicker Service Button
	public Button requestToRemote; //Join Request Sender Btn
	public ProgressBar progressBar; // update download progress shower
	ExecutorService executor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		isActivityActive=true;
		executor = Executors.newSingleThreadExecutor();
		mainHandler = new Handler(Looper.getMainLooper());
		StaticLib.write_int_to_shared_preference(ctx,"rmtBtns", 3);
		
		
		mainHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Do something after 100ms
					checkForUpdate();
				}
			}, 3000);
		
		print(""+AccessibilityClick.getAccessibiltyRunningStatus(ctx));
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Intent intentB = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intentB.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            startActivity(intentB);
        }
    }
		
		/*if (StaticLib.get_boolean_data_from_shared_preference(ctx, "uu"))
		 {;}else{
		 Intent intent = new Intent(MainActivity.this, AuthActivity.class);
		 startActivity(intent);
		 }*/
		
		//String json="[[[1,1,1,1]],[[1,1,1,1]]]";

		/*StaticLib.write_int_to_shared_preference(this, "SPEED", 1000);
		 StaticLib.write_int_to_shared_preference(this, "CYCLES", 1);
		
		 StaticLib.write_String_to_shared_preference(this, "OPSTR", "T");
		 StaticLib.write_boolean_to_shared_preference(this, "BIND_KEY", true);
		 StaticLib.write_boolean_to_shared_preference(this, "DOWN_KEY", true);
		 StaticLib.write_boolean_to_shared_preference(this, "UP_KEY", true);
		 */
		 
		SwitchMaterial vup = (SwitchMaterial)  findViewById(R.id.vup);  // Volume up binding switch
		SwitchMaterial vdn = (SwitchMaterial)  findViewById(R.id.vdn);  // Volume down binder switch
		
		vup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // do something, the isChecked will be
        // true if the switch is in the On position
        if(isChecked==true)
		{
			StaticLib.write_boolean_to_shared_preference(ctx, "BIND_KEY", true);
			StaticLib.write_boolean_to_shared_preference(ctx, "BIND_VOL_UP_KEY", true);
		}
        else
        {
        	StaticLib.write_boolean_to_shared_preference(ctx, "BIND_KEY", false);
        	StaticLib.write_boolean_to_shared_preference(ctx, "BIND_VOL_UP_KEY", false);
		}
    }
});

vdn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // do something, the isChecked will be
        // true if the switch is in the On position
        if(isChecked==true)
		{
			StaticLib.write_boolean_to_shared_preference(ctx, "BIND_KEY", true);
			StaticLib.write_boolean_to_shared_preference(ctx, "BIND_VOL_DOWN_KEY", true);
		 }
        else
        {
        	StaticLib.write_boolean_to_shared_preference(ctx, "BIND_KEY", false);
        	StaticLib.write_boolean_to_shared_preference(ctx, "BIND_VOL_DOWN_KEY", false);
		 }
    }
});
		
		
		 
		Button floatbtn = (Button) findViewById(R.id.floatbtn);
		floatbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startFloatingActivity();
			}
		});
		rmt = (Button) findViewById(R.id.rmt);
		rmt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StaticLib.shared_preference_contains(ctx, "LOGINED") == true)
				{
					if (StaticLib.get_boolean_data_from_shared_preference(ctx, "LOGINED") == true)
					{
						Intent intent = new Intent(MainActivity.this, RemoteActivity.class);
						startActivity(intent);
					}else{print("Login Again!");}
				}
				else
				{print("Login Required!");}
			}
		});

		Button macc = (Button) findViewById(R.id.macc);
		macc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AuthActivity.class);
				startActivity(intent);
			}
		});

		// Forground Main Service
		if (StaticLib.shared_preference_contains(ctx, "SPEED") == false)
		{
			StaticLib.write_int_to_shared_preference(ctx, "SPEED", 27);
		}
		if (StaticLib.shared_preference_contains(ctx, "CYCLES") == false)
		{
			StaticLib.write_int_to_shared_preference(ctx, "CYCLES", 2);
		}
		
		final EditText speed = (EditText) findViewById(R.id.speed);
		final Integer spd = StaticLib.get_int_data_from_shared_preference(ctx, "SPEED");
		final EditText cycles = (EditText) findViewById(R.id.cycles);
		final Integer cyc = StaticLib.get_int_data_from_shared_preference(ctx, "CYCLES");
		if (spd != 0) {
			speed.setText(String.valueOf(spd));
		} else {
			speed.setText(String.valueOf(27));
		}
		if (cyc != 0) {
			cycles.setText(String.valueOf(cyc));
		} else {
			cycles.setText(String.valueOf(2));
		}
		speed.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
											  int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
										  int before, int count) {
					if(s.length() != 0)
					{
						final int speed_val = StaticLib.txtToInt(speed.getText().toString());
						StaticLib.write_int_to_shared_preference(ctx, "SPEED", speed_val);
					}
				}
			});
		cycles.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
											  int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
										  int before, int count) {
					if(s.length() != 0)
					{
						final int cycles_val = StaticLib.txtToInt(cycles.getText().toString());
						StaticLib.write_int_to_shared_preference(ctx, "CYCLES", cycles_val);
					}
				}
			});

	 forbtn = (Button) findViewById(R.id.forbtn);
		forbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Integer speed_val = StaticLib.txtToInt(speed.getText().toString());
				final Integer cycle_val = StaticLib.txtToInt(cycles.getText().toString());

				if (cycle_val != null) {
					StaticLib.write_int_to_shared_preference(ctx, "CYCLES", cycle_val);

					if (speed_val != null) {
						StaticLib.write_int_to_shared_preference(ctx, "SPEED", speed_val);
						start_for_service();
					} else {
						print("Invalid Click Delay Value");
					}
				} else {
					print("Invalid Click Cycles Value");
				}
			}
		});

		final EditText ruid = (EditText) findViewById(R.id.ruid);
		requestToRemote = (Button) findViewById(R.id.requestToRemoteBtn);
		requestToRemote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ruid.getText().toString().matches("")) {
					print("Null remote UID value to request!");
					return;
				} else {
					StaticLib.requestReadPermissionOfRemote(ruid.getText().toString(), ctx);
					StaticLib.setAnsReceiveUid(ctx, ruid.getText().toString());
					stopClickingForgroundService();
					refreshTxtViews();
					refresher();
				}
			}
		});

		//write_to_db("komal", "mm");
		//install_update("tt.apk");
		accbtn = (Button) findViewById(R.id.accbtn);
		accbtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ctx.startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
				}
			});
		refresher();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view_rqsts, RemoteRequestsFragment.class, null)
                .commit();
                }
	}
	

	public boolean isActivityActive=false;
	
	@Override
	protected void onPause() {
        super.onPause();
		isActivityActive=false;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		isActivityActive=true;
		refresher();
	}
	
	public void stopClickingForgroundService(){
		// Implementation needed
		try
		{
		ctx.stopService(new Intent(ctx, ForegroundService.class));
		}
		catch (Exception e)
		{
			StaticLib.printException(ctx, e);
		}
	}
	
	public void refresher()
	{
		//AccessibilityPermissionStatusRefresh
		if(StaticLib.isAccessibilityServiceEnabled(ctx, AccessibilityClick.class)==true)
		{setEnabled();}
		else
		{setDisabled();}
		
		//Refresh Login
		TextView loginStatus=(TextView) findViewById(R.id.loginStatus);
		TextView myuid1 = (TextView) findViewById(R.id.myuid1);
		TextView recieveuid = (TextView) findViewById(R.id.recieveuid);
		if (StaticLib.shared_preference_contains(ctx, "LOGINED") == true)
		{
			if (StaticLib.get_boolean_data_from_shared_preference(ctx, "LOGINED") == true)
			{
				rmt.setEnabled(true);
				forbtn.setEnabled(true);
				requestToRemote.setEnabled(true);
				loginStatus.setText("Logined: True");
				myuid1.setText(StaticLib.get_String_data_from_shared_preference(ctx, "UID"));
				recieveuid.setText(StaticLib.get_String_data_from_shared_preference(ctx, "UID"));
			}else{requestToRemote.setEnabled(false); forbtn.setEnabled(false); forbtn.setEnabled(false); rmt.setEnabled(false); loginStatus.setText("Logined: Expired");myuid1.setText("Null"); recieveuid.setText("null");}
		}else{requestToRemote.setEnabled(false); forbtn.setEnabled(false); forbtn.setEnabled(false); rmt.setEnabled(false); loginStatus.setText("Logined: False");myuid1.setText("Expired! Login Again."); recieveuid.setText("null");}
	
		//Refreshing ANS_RECEIVE_UID
		if (StaticLib.shared_preference_contains(ctx, "ANS_RECEIVE_UID") == true)
		{
			if (!StaticLib.get_String_data_from_shared_preference(ctx, "ANS_RECEIVE_UID").equals(null))
			{recieveuid.setText(StaticLib.get_String_data_from_shared_preference(ctx, "ANS_RECEIVE_UID"));}
		}
		
	}
	public void setEnabled(){
		accbtn.setTextColor(0xFF00FF00);
		accbtn.setText("DISABLE ACCESSIBILTY SERVICE");
	}
	public void setDisabled(){
		accbtn.setTextColor(0xFFFF0000);
		accbtn.setText("ENABLE ACCESSIBILTY SERVICE");
	}
	
	public void startFloatingActivity() {
		Intent intent = new Intent(this, FloatingActivity.class);
		startActivity(intent);
	}

	private void start_for_service() {
		refresher();
		if(StaticLib.isAccessibilityServiceEnabled(ctx, AccessibilityClick.class)==true){
		Context context = getApplicationContext();
		Intent intent = new Intent(context, ForegroundService.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			context.startForegroundService(intent);
		} else {
			context.startService(intent);
		}}else{
			//If no accessibilityService
			print("No accessibilty Service Enabled");
			Intent intent = new Intent(MainActivity.this, AccessibilityPermissionActivity.class);
			startActivity(intent);
		}
	}

	public void refreshTxtViews() {

	}

	public void print(String str) {
		StaticLib.toast(ctx, str);
	}

	/*
	 private void write_to_db(String key, String value)
	 {
	
	 FirebaseDatabase database = FirebaseDatabase.getInstance("https://autoclickerfx-fd6ec-default-rtdb.asia-southeast1.firebasedatabase.app");
	 DatabaseReference myRef = database.getReference(key);
	 myRef.setValue(value);
	 }*/

	//** UPDATED APK INSTALLER **//
	
	public void installAPK(String update_file_name, String dir) {
		//String PATH = Environment.getExternalStorageDirectory() + "/" + update_file_name; //requires permission
		//String PATH = ctx.getExternalFilesDir(null) + "/update/" + update_file_name;    //does not requires permission
		String PATH = dir + update_file_name;
		
		File file = new File(PATH);
		if (file.exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			//uriFromFile
			intent.setDataAndType(uriFromFile(getApplicationContext(), new File(PATH)),
								  "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			try {
				getApplicationContext().startActivity(intent);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Err in open updated apk", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "installing", Toast.LENGTH_LONG).show();
		}
	}

	public Uri uriFromFile(Context context, File file) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
		} else {
			return Uri.fromFile(file);
		}
	}
	
	
	
	
	
	////// UPDATER /////
	String update_download_status="null";
	public void downloadAndInstallUpdate(UpdateInfo update)
	{
		progressBar = findViewById(R.id.progressbar); //intializingProgressBar
		
		update_download_status="UPDATE_DOWNLOADED_V_"+update.version;
		final String update_file_name="update.apk";
		final String dir = ctx.getExternalFilesDir(null) + "/update/";    //does not requires permission
		
		final File file = new File(dir);
		final File outputFile = new File(file, update_file_name);
		
		
		if (StaticLib.shared_preference_contains(ctx, update_download_status) == true)
		{
		if(StaticLib.get_boolean_data_from_shared_preference(ctx, update_download_status)==false)
		{
			progressBar.setVisibility(View.VISIBLE);
			
			executor.execute(new Runnable() {
					@Override
					public void run() {
						//Background work here
						// AfterGettingPermissions
						
						FileOutputStream fos=null;
						InputStream is=null;			
			
			try
			{
				URL url = new URL(update.uri);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(false);
				c.connect();

				//File file = new File(dir);
				file.mkdirs();
				//File outputFile = new File(file, update_file_name);
				if(outputFile.exists()){
					outputFile.delete();
				}
				
				//FileOutputStream fos = new FileOutputStream(outputFile);
				fos = new FileOutputStream(outputFile);
				
				//InputStream is = c.getInputStream();
				is = c.getInputStream();

				byte[] buffer = new byte[1024];
				int len1 = 0;
				
				final long size=(long) update.size * 1024 * 1024; //size in bytes
				long total = 0; //progress bar counter
				
				publishProgress(Integer.valueOf("" + 0)); // resetting progress bar
				
				while ((len1 = is.read(buffer)) != -1) {
					// publishing the progress....
					total += len1;
					publishProgress(Integer.valueOf("" + (int) (total * 100)/size));
					// writing to download file
					fos.write(buffer, 0, len1);
				}
				fos.close();
				is.close();
			}
			catch (Exception e)
			{
				final String err="ERROR in downloading update: "+e.getMessage();
				printFromAnyThread (err);
			}
			finally 
			{
				if(fos != null){
				try { fos.close(); } catch (Exception e){}}
				
				if(fos != null){
				try { is.close(); } catch (Exception e){}}
			}
	
	mainHandler.post(new Runnable() {
			@Override
			public void run() {
				
				progressBar.setVisibility(View.GONE);
				
				if(outputFile.exists())
				{
					if (MD5.checkMD5(update.md5sum, outputFile)==true)
					{
						StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, true);
						installAPK(update_file_name, dir); 	
					}
					else	
					{
						StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, false);
						retryUpdateDownload(update);
						print("MD5 Mismatch in update: "+MD5.calculateMD5(outputFile));
					}	
				}
				else
				{
					StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, false);
					retryUpdateDownload(update);
				}
		}});		
		
		
		}}); //executer end
		}else{ // update download status
			if(outputFile.exists())
			{
				StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, true);

				if (MD5.checkMD5(update.md5sum, outputFile)==true)
				{
					installAPK(update_file_name, dir); 	
				}
				else	
				{
					StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, false);
					retryUpdateDownload(update);
					print("MD5 Mismatch in update: "+MD5.calculateMD5(outputFile));
				}	
			}
			else
			{
				StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, false);
				retryUpdateDownload(update);
			}
		}
		
		}else{ //exists
		
			if(outputFile.exists()){
				outputFile.delete();
			}
			StaticLib.write_boolean_to_shared_preference(ctx, update_download_status, false);
			retryUpdateDownload(update);
			}
		
}
public int maxUpdateDownloadCounter=0;
public void retryUpdateDownload(UpdateInfo update){
	
	//print("Retrying downloading update: "+maxUpdateDownloadCounter);
	
	
	if(maxUpdateDownloadCounter<5)
	{
		maxUpdateDownloadCounter++;
		if(StaticLib.get_boolean_data_from_shared_preference(ctx, update_download_status)==false)
		{
			
			mainHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						//Do something
						downloadAndInstallUpdate(update);
					}
				}, 3000);
		
		}
	}
}




final public void printFromAnyThread (final String msg)
{
mainHandler.post(new Runnable() {
	@Override
	public void run() {
		print(msg);
	}});
	
/*runOnUiThread(new Runnable() {
		@Override
		public void run() {
			//TODO
		}});
		
runOnUiThread(new Runnable() {
				@Override
				public void run() {
		//
}});

	 catch (MalformedURLException e){eror(e.getMessage()+"MalformedURLException");}
	 catch (ProtocolException f){eror(f.getMessage()+"ProtocolException");}
	 catch (IOException g){eror(g.getMessage()+("IOException"));}
	 if (!file.exists()) {
	 file.mkdirs();
	 }
	 File file = new File(Environment.getExternalStorageDirectory()+"/"+"test.pdf");
	*/
}
	 
	
public void reqPermissionsForUpdate(UpdateInfo update) {
		mainHandler.post(new Runnable() {
				@Override
				public void run() {
		
		boolean installPer=false;
		boolean storPer=false;
		
		if(isActivityActive==true){  // APP INSTALLATION PERMISSION
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			if (!getPackageManager().canRequestPackageInstalls()) {
				startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
						.setData(Uri.parse(String.format("package:%s", getPackageName()))), 1);
			}else{installPer=true;}
		}else{installPer=true;}
		}
		
		if(installPer==true && isActivityActive==true){ // READ WRITE STORAGE PERMISSION 
		if (ContextCompat.checkSelfPermission(ctx,
				Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
		}else{storPer=true;}
		
		if (ContextCompat.checkSelfPermission(ctx,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
		}else{storPer=true;}
		}
		
if (installPer==true && storPer==true)
{
	if(isActivityActive==true)
	{
		//update_download_status="UPDATE_DOWNLOADED_V_"+update.version;
		downloadAndInstallUpdate(update);		
		
	} else {reqPermissionsForUpdateAgainAfterDelay(update);}
		
} else {reqPermissionsForUpdateAgainAfterDelay(update);}

}});
}

public int updatePermissionsMaxAskedCounter=0;
public void reqPermissionsForUpdateAgainAfterDelay(UpdateInfo update){
	if(updatePermissionsMaxAskedCounter<7){
		
	mainHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//Do something
				reqPermissionsForUpdate(update);
			}
		}, 3000);
		
		updatePermissionsMaxAskedCounter++;
}}
	
	
	final public void showUpdateDialog(UpdateInfo update)
	{
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						//print("Downloading Upadate.");
						reqPermissionsForUpdate(update);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						print("Update Cancelled.");
						break;
				} 
			}
		};
		if(update.required==true){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setCancelable(false);
		builder.setMessage("MANDATORY Update available of size("+(update.size)+"MB), REQUIRED TO INSTALL!").setPositiveButton("Install", dialogClickListener)
			.show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setMessage("Updated Version:"+update.version+" available of size("+(update.size)+"MB), Install This?").setPositiveButton("Install", dialogClickListener)
					.setNegativeButton("Later", dialogClickListener).show();
			}
	}
	final public void parseUpdateJson(final String json)
	{
		UpdateInfo update=new UpdateInfo();
		update=null;
		try
		{
		update=StaticLib.json_to_update(json);
		}
		catch (Exception e)
		{
			print(""+e.getMessage());
			update=null;
			return;
		}
		
		//print(""+update.uri+update.size+update.version+" "+update.md5sum);
		if(validateUpdateClassFromate(update)==true)
		{
		  if (update.push==true)
			  {
				  if(update.version>StaticLib.versionEpoch)
				  {
				    showUpdateDialog(update);  
			   	}
			}
		}else{
			print("Wrong update JSON received from server, Kindely contact developer for correction.");
			
			print(json);
		}
		//print(StaticLib.update_to_json(update));		
		
	}
	final public void getUpdateJsonFromServer()
	{
	
       final FirebaseDatabase database = FirebaseDatabase.getInstance(getText(R.string.db_uri).toString());
       final DatabaseReference updateRef=database.getReference("update");
	   updateRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
		@Override
		public void onComplete(@NonNull Task<DataSnapshot> task) {
			if (!task.isSuccessful()) {
				//Log.e("firebase", "Error getting data", task.getException());
					  print("unable checkAppUpdateStatusFromServer");
		   }
	   else
		   {
		final String json=String.valueOf(task.getResult().getValue());
	
				if (!json.equals("")){
					parseUpdateJson(json);
					}
			}
		}
			});
	}

	final public void checkForUpdate(){
		executor.execute(new Runnable() {
				@Override
				public void run() {
					//Background work here
		
	//	new Thread( new Runnable() { @Override public void run() { 
					// Run whatever background code you want here.
					long current_epoch=0;
					current_epoch=StaticLib.getEpoch();
					final long final_current_epoch=current_epoch;
					
					
					mainHandler.post(new Runnable() {
							@Override
							public void run() {
								
								long last_update_check_time=0;
								
								if (StaticLib.shared_preference_contains(ctx, "LAST_UPDATE_CHECK_TIME") == true)
								{
									last_update_check_time = StaticLib.get_long_data_from_shared_preference(ctx, "LAST_UPDATE_CHECK_TIME");
								}else{
									StaticLib.write_long_to_shared_preference(ctx, "LAST_UPDATE_CHECK_TIME", 0);
								}
								if(last_update_check_time==0){
									StaticLib.write_long_to_shared_preference(ctx, "LAST_UPDATE_CHECK_TIME", final_current_epoch);
									getUpdateJsonFromServer();
								}
	
								// Minimum time lag in minutes to check update from server
								final long frqToCheckUpdatesInMinutes=-1; //CHANGE ACCORDINGLY
								if(last_update_check_time!=0 && final_current_epoch!=0){
									if((final_current_epoch-last_update_check_time)>frqToCheckUpdatesInMinutes){
										
										StaticLib.write_long_to_shared_preference(ctx, "LAST_UPDATE_CHECK_TIME", final_current_epoch);
										getUpdateJsonFromServer();
								}} 
								//print("ct:"+final_current_epoch+" lt:"+last_update_check_time); //should be removed
				              
								}
							
			});
		}}); //.start();
	}
	
	
	

	//
	final private void publishProgress(final Integer... progress) {
		mainHandler.post(new Runnable() {
				@Override
				public void run() {
		progressBar.setProgress(progress[0]);
		}});

    }
	
	// Static Methods


	private static boolean validateUpdateClassFromate(UpdateInfo u){
		if
		(
		    u.uri != null &&
			!u.uri.equals("") &&
			u.md5sum != null &&
			!u.md5sum.equals("") &&
			u.version > 0 &&
			u.size > 0.0
		)
		{return true;}
		else
		{return false;}
	}
}
