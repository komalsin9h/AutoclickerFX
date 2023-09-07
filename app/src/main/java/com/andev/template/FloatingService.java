package com.andev.template;

//import java.util.ArrayList;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import java.util.*;



public class FloatingService extends Service
{
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		ctx = this;

		random = new Random();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;}
		else
		{layout_parms = WindowManager.LayoutParams.TYPE_PHONE;}

		if (StaticLib.shared_preference_contains(ctx, "APSTR") == true)
		{
			apstr = StaticLib.get_String_data_from_shared_preference(ctx, "APSTR").toString();
		}
		if (StaticLib.shared_preference_contains(ctx, "OPSTR") == true)
		{
			opstr = StaticLib.get_String_data_from_shared_preference(ctx, "OPSTR").toString();
		}
	}

	public boolean floatsPresent=false;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		if (floatsPresent == false)
		{
			aa = StaticLib.json_to_arr(StaticLib.get_String_data_from_shared_preference(ctx, "CORD").toString());
			aps = aa.get(0).size();
			ops = aa.get(0).get(0).size();
			arr_lyt = makearr(aps, ops);
			create_all_floats(aa);
			floatsPresent = true;
		}
		else
		{
			remove_all_floats();
			///Intent myService = new Intent(ctx, FloatingService.class);
			////stopService(myService);
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		remove_all_floats();

		super.onDestroy();
	}

	///GLOBAL
	private Context ctx;
	private String apstr="WIN";
	private String opstr="POI";
	final private int x_correction=11;
	final private int y_correction=11;
	private int aps=0;
	private int ops=0;
	int layout_parms;
	private Random random;
	public ArrayList<ArrayList<ArrayList<Integer>>> aa=new ArrayList<ArrayList<ArrayList<Integer>>>();
	public ArrayList<ArrayList<LinearLayout>> arr_lyt=new ArrayList<ArrayList<LinearLayout>>();
	//\GLOBAL
	private void get_params(int x, int y, int i, int ii)
	{
		updatex(x, i, ii);
		updatey(y, i, ii);
		StaticLib.write_String_to_shared_preference(ctx, "CORD", StaticLib.arr_to_json(aa));

		//StaticLib.broadcast_msg_to_print_everywhere(this, " X:" + x + " Y:" + y + " " + apstr + ":" + (i + 1) + " " + opstr + ":" + (ii + 1));
	}

	final private void updatex(int x, int i, int ii)
	{
		ArrayList<ArrayList<Integer>> c = aa.get(0);
		ArrayList<Integer> cc=c.get(i);
		cc.set(ii, x);
		c.set(i, cc);
		aa.set(0, c);
	}
	final private void updatey(int y, int i, int ii)
	{
		ArrayList<ArrayList<Integer>> c= aa.get(1);
		ArrayList<Integer> cc=c.get(i);
		cc.set(ii, y);
		c.set(i, cc);
		aa.set(1, c);
	}

	final private void remove_all_floats()
	{
		for (int i = 0; i < aps; i++)
		{
			for (int ii = 0; ii < ops; ii++)
			{
				create_floating(arr_lyt.get(i).get(ii), 0, 0, false, true, 0, 0); //FOR REMOVING EXISTING FLOATES
			}
		}

		//stopSelf();
	}
	final private void create_all_floats(ArrayList<ArrayList<ArrayList<Integer>>> aa)
	{
		// CREATE FLOATINGS OF GIVEN ARRAY
		if (StaticLib.check_malformed_arr(aa) == true)
		{
			for (int i = 0; i < aps; i++)
			{
				for (int ii = 0; ii < ops; ii++)
				{
					create_floating(arr_lyt.get(i).get(ii), aa.get(0).get(i).get(ii), aa.get(1).get(i).get(ii), false, false, i, ii);
				}
			}
		}
		else
		{
			print(ctx, "Location data malformed, Pls reinstall app or create new location points.");
		}
	}

	final private ArrayList<ArrayList<LinearLayout>> makearr(int aps, int ops)
	{
		// RETURN VIEW ARRAY
		ArrayList<ArrayList<LinearLayout>> ll=make_emp_arr(aps, ops);

		for (int i = 0; i < aps; i++)
		{
			int red=random.nextInt(255);
			int green=random.nextInt(255);
			int blue=random.nextInt(255);

			ArrayList<LinearLayout> l=make_emp_half_arr(ops);

			for (int ii = 0; ii < ops; ii++)
			{
				//StaticLib.broadcast_msg_to_print_everywhere(this, "tost" + i + ii);
				l.set(ii, layout(red, green, blue, i, ii, this));
			}

			ll.set(i, l);
		}

		return ll;
	}

	final private void create_floating(View v, int x, int y, boolean random_spread, boolean remove, int r, int rr)
	{

		// FLOATING VIEW MAKER AT 'X' AND 'Y'
		final View myFloatingView = v;
		final WindowManager mWindowManager = (WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE);

		if (remove == true)
		{
			try
			{
				mWindowManager.removeView(myFloatingView);
				//mWindowManager.removeViewImmediate(myFloatingView);
			}
			catch (Exception e)
			{
				//StaticLib.broadcast_msg_to_print_everywhere(this, "exception.");
			}


			return;
		}

		if (remove == false)
		{
			final int i=r;
			final int ii=rr;

			final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				layout_parms,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

			//final Display display = mWindowManager.getDefaultDisplay();
			//final int width = display.getWidth();
			//final int height = display.getHeight();

			params.gravity = Gravity.TOP | Gravity.LEFT; 
			/*if (random_spread == false)
			 {*/
			params.x = x - x_correction;   // random.nextInt(800) - 400;
			params.y = y - y_correction;//Second plus for Correction, Dont change   //random.nextInt(1600) - 800;
			/*}
			 if (random_spread == true)
			 {
			 params.x = random.nextInt((width - 160) + 1) + 3; //nextInt((max - min) + 1) + min;
			 params.y = random.nextInt((height - 6) + 1) + 3;
			 }

			 if (params.x < 0)
			 {params.x = 0;}
			 if (params.y < 0)
			 {params.y = 0;}*/

			mWindowManager.addView(myFloatingView, params);
			myFloatingView.setOnTouchListener(new View.OnTouchListener() {

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
								//this code is helping the widget to move around the screen with fingers
								params.x = initialX + (int) (event.getRawX() - initialTouchX);
								params.y = initialY + (int) (event.getRawY() - initialTouchY);
								mWindowManager.updateViewLayout(myFloatingView, params);
								return true;
							case MotionEvent.ACTION_UP:
								//get_params(event.getRawX(), event.getRawY(), myFloatingView.getId());
								get_params(params.x + x_correction, params.y + y_correction, i, ii);
								return true;
						}
						return false;
					}
				});

		}
	}





//lacals
	final private ArrayList<ArrayList<LinearLayout>> make_emp_arr(int aps, int ops)
	{

		ArrayList<ArrayList<LinearLayout>> ll=new ArrayList<ArrayList<LinearLayout>>();
		for (int i=0; i < aps; i++)
		{
			ArrayList<LinearLayout> l=new ArrayList<LinearLayout>();

			for (int ii = 0; ii < ops; ii++)
			{

				l.add(null);
			}
			ll.add(l);
		}
		return ll;
	}

	final private ArrayList<LinearLayout> make_emp_half_arr(int ops)
	{
		ArrayList<LinearLayout> l=new ArrayList<LinearLayout>();

		for (int ii = 0; ii < ops; ii++)
		{
			l.add(null);
		}
		return l;
	}

	final private LinearLayout layout(int red, int green, int blue, int ap, int op, Context ctx)
	{
		View ti=new View(ctx);
		ti.setLayoutParams(new LayoutParams(24, 24)); //w,h
		ti.setBackground(bg_shape());

		TextView opt=new TextView(ctx);
		opt.setText(apstr + ": " + (ap + 1) + "\n" + opstr + ": " + (op + 1));
		opt.setPadding(5,   5, 5 ,  5) ;       //o p t.set T extS i ze( T ypedValue.COMPLEX_UNIT_DIP, 7);
		opt.setTextSize(TypedValue.COMPLEX_UNIT_PX, 19);
		opt.setTypeface(null, Typeface.BOLD);       
		opt.setTextColor(Color.rgb(red, green, blue) ^ 0x00ffffff);
		opt.setBackground(shape(red, green, blue));
		opt.setGravity(Gravity.CENTER);
		opt.setLayoutParams(new LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
											 WindowManager.LayoutParams.WRAP_CONTENT));

		LinearLayout l=new LinearLayout(ctx);
		l.setGravity(Gravity.START | Gravity.TOP);
		l.setOrientation(LinearLayout.VERTICAL);
		l.setPadding(0, 0, 0, 0);
		//l.setId(1000);
		l.addView(ti);
		l.addView(opt);			
		return l;
	}
	final private static GradientDrawable shape(int red, int green, int blue)
	{
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE);
		shape.setCornerRadii(new float[] {50,50,50,50,50,50,50,50});
		shape.setColor(Color.rgb(red, green, blue));
		shape.setStroke(1, Color.rgb(red, green, blue) ^ 0x00ffffff);
		return shape;
	}
	final private static GradientDrawable bg_shape()
	{
		GradientDrawable bg_shape=new GradientDrawable();
		bg_shape.setShape(GradientDrawable.RECTANGLE);
		bg_shape.setCornerRadii(new float[] {50,50,50,50,50,50,50,50});
		bg_shape.setColor(Color.alpha(255));
		bg_shape.setStroke(8, Color.rgb(160, 0, 60));
		return bg_shape;
	}

	final private void print(Context ctx, String str)
	{
		StaticLib.broadcast_msg_to_print_everywhere(ctx, str);
	}


}
