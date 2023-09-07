
package com.andev.template;


import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.TaskExecutors;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class AuthActivity extends AppCompatActivity
{
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
	private Context ctx;
    // string for storing our verification ID
    private String verificationId;

    // variable for our text input 
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;
    private Button verifyOTPBtn, generateOTPBtn,logout;
	private TextView uid, loginStatus,print;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

		ctx = AuthActivity.this;

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

		uid = findViewById(R.id.uid);
		print = findViewById(R.id.print);
		loginStatus = findViewById(R.id.loginStatus);

        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);

        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
		logout = findViewById(R.id.logout);

        // setting onclick listener for generate OTP button.
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					// below line is for checking weather the user
					// has entered his mobile number or not.
					if (TextUtils.isEmpty(edtPhone.getText().toString()))
					{
						// when mobile number text field is empty
						// displaying a toast message.
						printAll("Please enter a valid phone number.");
					}
					else
					{
						// if the text field is not empty we are calling our 
						// send OTP method for getting OTP from Firebase.
						String phone = edtPhone.getText().toString();
						if (isValidPhoneNumber(phone) == true)
						{
							sendVerificationCode(phone);
						}
						else
						{
							printAll("Please enter a valid phone number.");
						}
					}
				}

			});

        // initializing on click listener
        // for verify otp button
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					// validating if the OTP text field is empty or not.
					if (TextUtils.isEmpty(edtOTP.getText().toString()))
					{
						// if the OTP text field is empty display 
						// a message to user to enter OTP
						printAll("Please enter OTP");
					}
					else
					{
						// if OTP field is not empty calling 
						// method to verify the OTP.
						verifyCode(edtOTP.getText().toString());
					}
				}
			});


		logout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					signOut();
				}
			});


		setUserInfo();
    }


	@Override
	protected void onPause()
	{
		super.onPause();

	}
	@Override
	protected void onResume()
	{
		super.onResume();
		setUserInfo();
	}
    private void sendVerificationCode(String number)
	{
		// Force reCAPTCHA flow
		//FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);


        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
			PhoneAuthOptions.newBuilder(mAuth)
			.setPhoneNumber(number)            // Phone number to verify
			.setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
			.setActivity(this)                 // Activity (for callback binding)
			.setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
			.build();
        PhoneAuthProvider.verifyPhoneNumber(options);

		//Toast.makeText(AuthActivity.this, "Verification Code Sended", Toast.LENGTH_SHORT).show();

    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

	// initializing our callbacks for on
	// verification callback method.
	mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when 
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
		{
            super.onCodeSent(s, forceResendingToken);

            // when we receive the OTP it 
            // contains a unique id which 
            // we are storing in our string 
            // which we have already created.
			print("code successfully sended.. \n VerificationId: " + s);

            verificationId = s;
        }

        // this method is called when user 
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
		{
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.

            if (code != null)
			{
                // if the code is not null then
                // we are setting that code to 
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code 
                // to OTP edittext field we 
                // are calling our verifycode method.
                verifyCode(code);
				print("code detected: " + code);

            }
			else
			{
				signIn(phoneAuthCredential);
				print("verification sucess without code: " + code);
			}
		}
        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e)
		{
			//displaying error message with firebase exception.
			printAll("code can't sended " + e.getMessage());
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code)
	{
        // below line is used for getting  
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
		signIn(credential);
	}


	private void signIn(PhoneAuthCredential credential)
	{
		mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
				{
                    if (task.isSuccessful())
					{
                        // Sign in success, update UI with the signed-in user's information
						printAll("Sign In: Success");


						// FirebaseUser user = task.getResult().getUser();
						setUserInfo();
						finish();
                    }
					else
					{
                        // Sign in failed, display a message and update the UI
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
						{
                            // The verification code entered was invalid
							printAll("Sign In: failed invalid code.");
                        }
						else
						{
							printAll("Sign In: Failed unknkwn reson.");
						}
                    }
                }
            });
	}



	private boolean isValidPhoneNumber(CharSequence phoneNumber)
	{
		if (!TextUtils.isEmpty(phoneNumber))
		{
			return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
		}
		return false;
	}

	private void signOut()
	{
		if (mAuth.getCurrentUser() != null)
		{
			mAuth.signOut();
			StaticLib.write_String_to_shared_preference(this, "UID", "");
			// unnecessory Auth.GoogleSignInApi.signOut(apiClient);
			setUserInfo();
		}
	}
	private void printToast(String msg)
	{
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
	private void print(String msg)
	{
		print.setText(msg);
	}

	private void printAll(String msg)
	{
		print(msg);
		printToast(msg);
	}

	private void setUserInfo()
	{
		FirebaseUser user=mAuth.getCurrentUser();
		if (user != null)
		{
			// User is signed in
			StaticLib.write_boolean_to_shared_preference(ctx, "LOGINED", true);
			StaticLib.write_String_to_shared_preference(ctx, "UID", user.getUid());
		}
		else
		{
			// No user is signed in
			StaticLib.write_boolean_to_shared_preference(ctx, "LOGINED", false);
		}

		loginStatus.setText("Logined: " + StaticLib.get_boolean_data_from_shared_preference(ctx, "LOGINED"));
		uid.setText(StaticLib.get_String_data_from_shared_preference(ctx, "UID").toString());

		if (StaticLib.shared_preference_contains(ctx, "ANS_RECEIVE_UID") == false)
		{
			StaticLib.write_String_to_shared_preference(ctx, "ANS_RECEIVE_UID", StaticLib.get_String_data_from_shared_preference(ctx, "UID"));
		}
		//////////
		print(StaticLib.get_String_data_from_shared_preference(ctx, "ANS_RECEIVE_UID")); //rmove it
//////
		
		FirebaseDatabase.getInstance(getText(R.string.db_uri).toString()).getReference().child("ans").child(StaticLib.get_String_data_from_shared_preference(ctx, "UID")).setValue(0);
		//StaticLib.addRemoteClient(StaticLib.get_String_data_from_shared_preference(ctx, "UID"),ctx);
	}
}






	
