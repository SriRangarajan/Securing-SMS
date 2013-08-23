package com.example.securesms;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnWriteSMS, btnReceiveSMS;
	
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //txtMessage = (EditText)findViewById(R.id.txtMessage);
        btnWriteSMS = (Button) findViewById(R.id.btnSender);
        btnReceiveSMS = (Button) findViewById(R.id.btnReceiver);
        
        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Content of the SMS goes here..."); 
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
        */
        
        btnWriteSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	

           		Toast.makeText(getBaseContext(), 
                        "Sender Side", 
                        Toast.LENGTH_SHORT).show();
           		System.out.println("Sender Side CLicked");
           		Intent write = new Intent(MainActivity.this, SMSWrite.class);
           		startActivity(write);
            	
            }
        });
        
        btnReceiveSMS.setOnClickListener(new View.OnClickListener()
        { 
           	public void onClick(View v)
           	{
           		Toast.makeText(getBaseContext(), 
                        "Receiver Side", 
                        Toast.LENGTH_SHORT).show();
           		System.out.println("Receiver Side CLicked");
           		Intent read = new Intent(MainActivity.this, SMSRead.class);
           		startActivity(read);
           	}
        });
                            
    }

}
