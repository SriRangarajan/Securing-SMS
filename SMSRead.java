package com.example.securesms;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSRead extends Activity {
	
	public final static String SMS_Message = "com.example.SMSTest.MESSAGE";
	public final static String SMS_Phone = "com.example.SMSTest.MESSAGE";
	
	Button btnReadSMS,btnDone;
	int s=0;
	TextView txtMessage; 
	Ecc ecc; 
	
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsread);
        txtMessage = (TextView)findViewById(R.id.txtMessage);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnReadSMS = (Button) findViewById(R.id.btnReadKey);
        
        ecc=new Ecc(this);
        
        btnReadSMS.setTag(1);
        btnReadSMS.setText("Receive Key");
        btnReadSMS.setOnClickListener(new View.OnClickListener()
        { 
           	public void onClick(View v)
           	{
           		if (s==0){
           		Toast.makeText(getBaseContext(), 
                        "Read SMS Clicked", 
                        Toast.LENGTH_SHORT).show();
            	
           		Uri my_uri = Uri.parse("content://sms/inbox");          
	            Cursor c =v.getContext().getContentResolver().query(my_uri, null, null ,null,null); 
	            if(c.moveToFirst()) 
	            {
	               String  msg_body =  c.getString(c.getColumnIndexOrThrow("body")).toString();
	               String sender_number = c.getString(c.getColumnIndexOrThrow("address")).toString();
	               String chk=msg_body.substring(0, 3);
	   	    		if("key".equals(chk) || "emg".equals(chk)){
	   	    			System.out.println("Sending Message");
	   	    		    ecc.recv(sender_number,msg_body);
	   	    		}
	   	    		else{
	   	    			System.out.println("error here at :"+chk);
	   	    		}
	            }
	            c.close();
           	
           		s=1;
           		btnReadSMS.setText("Decrypt");
           	    v.setTag(0);
           	}
        
           	else if (s==1){
           		Toast.makeText(getBaseContext(), 
                        "Decryption", 
                        Toast.LENGTH_SHORT).show();
            	
           		Uri my_uri = Uri.parse("content://sms/inbox");          
	            Cursor c =v.getContext().getContentResolver().query(my_uri, null, null ,null,null); 
	            if(c.moveToFirst()) 
	            {
	               String  msg_body =  c.getString(c.getColumnIndexOrThrow("body")).toString();
	               String sender_number = c.getString(c.getColumnIndexOrThrow("address")).toString();
	               String chk=msg_body.substring(0, 3);
	   	    		if("key".equals(chk) || "emg".equals(chk)){
	   	    			System.out.println("Sending Message");
	   	    		    ecc.recv(sender_number,msg_body);
	   	    		}
	   	    		else{
	   	    			System.out.println("error here at :"+chk);
	   	    		}
	            }
	            c.close();
           		s=0;
           		btnReadSMS.setText("Receive Key");
           	    v.setTag(1);
        }
	}
    });
        
                            
    
    btnDone.setOnClickListener(new View.OnClickListener()
    { 
       	public void onClick(View v)
       		{
    	   			Toast.makeText(getBaseContext(), 
       					"Done Clicked", 
       					Toast.LENGTH_SHORT).show();
    	   			Intent done = new Intent(SMSRead.this, MainActivity.class);
            		startActivity(done); 
       		}
    });
  
	}
	public void update(String msg) {
		// TODO Auto-generated method stub
		txtMessage.append(msg);
		
	}

}
