package com.example.securesms;


import java.util.ArrayList;
import java.lang.Object;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SMSWrite extends Activity {
	
		Intent write = getIntent();
		public final static String SMS_Message = "com.example.SMSTest.MESSAGE";
		public final static String SMS_Phone = "com.example.SMSTest.MESSAGE";
		static TextView txtmsg;
		int s=0;
		Button btnSendSMS;
		//EditText txtPhoneNo;
		EditText txtMessage;
		Ecc ecc; 
		
		String str[]=null;
		int i =0;
		ArrayList<String> al=new ArrayList<String>();
		AutoCompleteTextView txtPhoneNo;
				
		
		/** Called when the activity is first created. */
		
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_smswrite);
	        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
	        txtPhoneNo = (AutoCompleteTextView) findViewById(R.id.txtPhoneNo);
	        txtMessage = (EditText) findViewById(R.id.txtMessage);
	        
	        ecc=new Ecc(this);
	        
	        System.out.println("Sender Activity Started");
	        
	        ContentResolver cr=getContentResolver();
	        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
	        str=new String[cur.getCount()];
	        if(cur.getCount()>0)
	        {
	        	while(cur.moveToNext())
	        	{
	        		String id=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	        		String name=cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        		if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0)
	        		{
	        			Cursor pcur=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?", new String[]{id}, null);
	        			while(pcur.moveToNext())
	        			{
	        				String phoneNo=pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        				String phoneno=""+name+"-"+phoneNo;
	        				str[i]=phoneNo;
	        				al.add(phoneno);
	        				i++;
	        				
	        				
	        			}
	        			pcur.close();
	        		}
	        	
	        	
	        	
	        	txtPhoneNo.setAdapter(new ArrayAdapter<String>(this,R.layout.list_xml,al));
	        
	        
	        btnSendSMS.setTag(1);
	        btnSendSMS.setText("Send Key");
	        btnSendSMS.setOnClickListener(new View.OnClickListener() 
	        {
	            public void onClick(View v) 
	            {            	
	            	if (s==0){
	            		s=1;
	               		btnSendSMS.setText("Encrypt and Send SMS");
	               	    v.setTag(0);
		            	String phoneNo = txtPhoneNo.getText().toString(); 
		            	String message = txtMessage.getText().toString();
		            	message="pmg."+message;
		            	Toast.makeText(getBaseContext(), 
		                        "Send SMS Clicked", 
		                        Toast.LENGTH_SHORT).show();
		            	
		            	if (phoneNo.length()>0 && message.length()>0){
		            		ecc.recv(phoneNo, message);
		            	}
		                else
		                	Toast.makeText(getBaseContext(), 
		                        "Please enter both phone number and message.", 
		                        Toast.LENGTH_SHORT).show();
		            }
	            	else if(s==1){
	            	
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
		                   System.out.println("Message: "+msg_body);
		                   System.out.println("number: "+sender_number);
		       	    		if("key".equals(chk) || "emg".equals(chk)){
		       	    			System.out.println("Sending message");
		       	    		    ecc.recv(sender_number,msg_body);
		       	    		 Intent last = new Intent(SMSWrite.this, MainActivity.class);
		                		startActivity(last);
		       	    		}
		                }
		                c.close();
		                s=1;
	               		btnSendSMS.setText("Send Key");
	               	    v.setTag(1);
		            	
	            	}
	            }
	        });      

	     }
}	    
