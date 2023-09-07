package com.andev.template;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import android.content.SharedPreferences;

import android.os.AsyncTask;
import java.util.Random;
import java.util.*;
import android.os.HandlerThread;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class FloatingActivity extends AppCompatActivity implements View.OnClickListener
{
	private ArrayList<ArrayList<ArrayList<Integer>>> ary=null;
	private boolean movedTasksToBack=false;
	private EditText t1;
	private EditText t2;
	private EditText t3;
	private int aps=1;
	private int ops=3;
	private int t1int=0;
	private int t2int=0;
	private TextView txt;
	private TextView win;
	private TextView poi;
	private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
	private Intent inten=new Intent();
	final private static Random random=new Random();;
	private Context ctx;
	private SharedPreferences prefs;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

		ctx = this;
		findViewById(R.id.start).setOnClickListener(this);
		findViewById(R.id.reset).setOnClickListener(this);
		findViewById(R.id.apply).setOnClickListener(this);
		t1 = (EditText) findViewById(R.id.screen);
		t2 = (EditText) findViewById(R.id.points);
		txt = (TextView) findViewById(R.id.txt);
		
		win = (TextView) findViewById(R.id.screenText);
		poi = (TextView) findViewById(R.id.pointsText);
		if (StaticLib.shared_preference_contains(ctx, "APSTR") == true)
		{
			win.setText("Number of "+StaticLib.get_String_data_from_shared_preference(ctx, "APSTR").toString()+" windows.");
		}
		if (StaticLib.shared_preference_contains(ctx, "OPSTR") == true)
		{
			poi.setText("Number of "+StaticLib.get_String_data_from_shared_preference(ctx, "OPSTR").toString()+" clicking points for each window.");
		}

		if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
		{
			ary = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
			if (StaticLib.vlidateCord3dArrayLst(ary)==false)
			{
				ary = StaticLib.mk_rand_arr(2, aps, ops);
				StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(ary));
			}
			else
			{
			aps = ary.get(0).size();
			ops = ary.get(0).get(0).size();
			}
		}
		else
		{
			ary = StaticLib.mk_rand_arr(2, aps, ops);
			StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(ary));
		}

		if (aps<1 || ops<1)
		{
			t1.setText(Integer.toString(1));
			t2.setText(Integer.toString(1));
		}
		else
		{
			t1.setText(Integer.toString(aps));
			t2.setText(Integer.toString(ops));
		}

		reftxt();


		prefs = StaticLib.sharedPref(ctx);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
			{
				// Implementation
				if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
				{
					//ary = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
					if (StaticLib.vlidateCord3dArrayLst(ary)==true)
					{
						aps = ary.get(0).size();
						ops = ary.get(0).get(0).size();
					}
					else
					{
						aps=1;
						ops=1;
					}
				}
				t1.setText(Integer.toString(aps));
				t2.setText(Integer.toString(ops));
				reftxt();
				//print(ctx,"hello");
			}
		};


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
		{
			//askPermission();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		try {
			prefs.unregisterOnSharedPreferenceChangeListener(listener);
		}
		catch (Exception e)
		{
			StaticLib.printException(ctx, e);
		}
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		try 
		{
			if (listener==null){
				return;
				}
			prefs.registerOnSharedPreferenceChangeListener(listener);
		}
		catch (Exception e)
		{
			StaticLib.printException(ctx, e);
		}
		reftxt();
	}
	
	private long mLastClickTime = 0;
	@Override
    public void onClick(View v)
	{
		// Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 200) {
			print(ctx,"don't be superfast!");
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
		
		int id=v.getId();
		if (id == R.id.apply)
		{
			t1int = Integer.parseInt(t1.getText().toString()) ;
			t2int = Integer.parseInt(t2.getText().toString());

			apply();
			reftxt();
		} 
		else if 
		(id == R.id.reset) 
		{
			reset();
			reftxt();
		} 
		else if (id == R.id.start) 
		{
			reftxt();
			dispatchIntent();
		}
    }	
	@Override
    public void onDestroy()
    {

		//Intent myService = new Intent(this, FloatingService.class);
		//stopService(myService);

        super.onDestroy();
    }

	final private void apply()
	{
		if (fieldEmpty() == false)
		{
			if (t1int > 0 && t2int > 0)
			{
				if (StaticLib.shared_preference_contains(this, "CORD") == false)
				{
					ary = StaticLib.mk_rand_arr(2, Integer.parseInt(t1.getText().toString()), Integer.parseInt(t2.getText().toString()));
					StaticLib.write_String_to_shared_preference(this, "CORD", StaticLib.arr_to_json(ary));
				}

				ary = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
				sync_app();
				StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(ary));
				ary = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
				sync_ops();
				StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(ary));
				ary = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
				//print(ctx, StaticLib.arr_to_json(ary));
			}
			else
			{
				print(ctx, "ENTERED VALUE CAN'T BE LESS THAN '1'.");
			}

			if (StaticLib.check_malformed_arr(ary) == true)
			{
				StaticLib.write_String_to_shared_preference(this, "CORD", StaticLib.arr_to_json(ary));
			}
			else
			{
				print(this, "" + StaticLib.arr_to_json(ary));
				print(this, "MALFORMED ARRAY, REINSTALL MAY FIX.");
			}
		}
		else
		{
			print(this, "ENTERED VALUE CAN'T BE EMPTY.");
		}
	}

	final private void dispatchIntent()
	{
		inten = new Intent(getApplicationContext(), FloatingService.class);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		{
			startService(inten);
			moveTasksToBackground();
		}
		else if (Settings.canDrawOverlays(this))
		{
			startService(inten);
			moveTasksToBackground();
		}
		else
		{
			askPermission();
			print(this, "Enable OVERLAY permisaion required..");
		}
	}



//////
	final private void askPermission()
	{
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
								   Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

	final private boolean fieldEmpty()
	{
		if (t1.getText().toString().matches(""))
		{return true;}
		else if (t2.getText().toString().matches(""))
		{return true;}
		else
		{return false;}
	}
	final private void addAps(int r)
	{
		ArrayList<ArrayList<Integer>> x=ary.get(0);
		ArrayList<ArrayList<Integer>> y=ary.get(1);
		int nu =x.get(0).size();

		for (int i=0; i < r; i++)
		{
			ArrayList<Integer> ap=new ArrayList<Integer>();

			for (int ii=0; ii < nu; ii++)
			{
				ap.add(rand());
			}
			x.add(ap);
			y.add(ap);
		}
		ArrayList<ArrayList<ArrayList<Integer>>> k=new ArrayList<ArrayList<ArrayList<Integer>>>();
		k.add(x); k.add(y);
		ary = k;
	}
	final private void removeAps(int r)
	{
		ArrayList<ArrayList<Integer>> x=ary.get(0);
		ArrayList<ArrayList<Integer>> y=ary.get(1);

		for (int i=0; i < r; i++)
		{
			x.remove(x.size() - 1);
			y.remove(y.size() - 1);
		}
		ArrayList<ArrayList<ArrayList<Integer>>> k=new ArrayList<ArrayList<ArrayList<Integer>>>();
		k.add(x); k.add(y);
		ary = k;
	}

	final private ArrayList<Integer> asok(ArrayList<Integer> ar, int r)
	{
		ArrayList<Integer> a=ar;
		for (int i=0; i < r; i++)
		{
			//print(this, "ini"+a.size());
			a.add(5);
			//print(this, "aft"+a.size());
		}
		return a;
	};
	final private void addOps(int r)
	{
		ArrayList<ArrayList<Integer>> x=ary.get(0);
		ArrayList<ArrayList<Integer>> y=ary.get(1);
		int nu=ary.get(0).size();

		for (int i=0; i < nu; i++)
		{
			ArrayList<Integer> xtm=x.get(i);
			ArrayList<Integer> ytm=y.get(i);
			for (int ii=0; ii < r; ii++)
			{
				//print(this, "ini"+a.size());
				xtm.add(rand());
				ytm.add(rand());
			}
			x.set(i, xtm);
			y.set(i, ytm);
		}
		ArrayList<ArrayList<ArrayList<Integer>>> k=new ArrayList<ArrayList<ArrayList<Integer>>>();
		k.add(x); k.add(y);
		ary = k;
	}

	final private void removeOps(int r)
	{
		ArrayList<ArrayList<Integer>> x=ary.get(0);
		ArrayList<ArrayList<Integer>> y=ary.get(1);
		int nu =x.size();

		for (int ii=0; ii < nu; ii++)
		{
			ArrayList<Integer> xx=x.get(ii);
			ArrayList<Integer> yy=y.get(ii);
			for (int iii=0; iii < r; iii++)
			{
				xx.remove(xx.size() - 1);
				yy.remove(yy.size() - 1);
			}
			x.set(ii, xx);
			y.set(ii, yy);
		}
		ArrayList<ArrayList<ArrayList<Integer>>> k=new ArrayList<ArrayList<ArrayList<Integer>>>();
		k.add(x); k.add(y);
		ary = k;
	}
	final private void sync_app()
	{
		if (StaticLib.check_malformed_arr(ary) == true)
		{
			if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
			{
				int w=Integer.parseInt(t1.getText().toString());
				if (ary.get(0).size() < w)
				{
					addAps(w - ary.get(0).size());
				}
				if (ary.get(0).size() > w)
				{
					removeAps(ary.get(0).size() - w);
				}
			}
		}
		else
		{
			print(ctx, "UNABLE TO SYNC_APP MALFORMED ARRAY, TRY REINSTALLING.");
		}
	}
	final private void sync_ops()
	{
		if (StaticLib.check_malformed_arr(ary) == true)
		{
			if (StaticLib.shared_preference_contains(ctx, "CORD") == true)
			{
				int p=Integer.parseInt(t2.getText().toString());
				if (ary.get(0).get(0).size() < p)
				{
					addOps(p - ary.get(0).get(0).size());
				}
				if (ary.get(0).get(0).size() > p)
				{
					removeOps(ary.get(0).get(0).size() - p);
				}
			}
		}
		else
		{
			print(this, "UNABLE TO SYNC_OPS MALFORMED ARRAY, TRY REINSTALLING.");
		}
	}

	final private void print(Context ctx, String str)
	{

		StaticLib.broadcast_msg_to_print_everywhere(ctx, str);

	}
	final private void reftxt()
	{
		txt.setText(StaticLib.arr_to_json(ary));
	}
	final private void reset()
	{
		//String json="[[[1,1,1,1,1]],[[1,1,1,1,1]]]";
		//StaticLib.write_String_to_shared_preference(this, "CORD", json);
		final int i1=Integer.parseInt(t1.getText().toString());
		final int i2=Integer.parseInt(t2.getText().toString());
		ary = StaticLib.mk_rand_arr(2, i1, i2);
		StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(ary));

		//finishAffinity();
	}
	final private static int rand()
	{
		return random.nextInt((700 - 100) + 1) + 100;
	}
	
	final private void moveTasksToBackground()
	{
		if (movedTasksToBack == false || false) // Currently Disabled
			{
				movedTasksToBack = true;
				//finishAffinity();
				moveTaskToBack(true);
			}
	}
}



