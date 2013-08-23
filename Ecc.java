package com.example.securesms;

import java.math.BigInteger;
import java.util.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;


public class Ecc{
	
	public final static String SMS_Message = "com.example.smsWrite.MESSAGE";
	public final static String SMS_Phone = "com.example.smsWrite.MESSAGE";
	
	
    BigInteger p,a,b,x,d,n;
    ECParameters params;
    ECPoint G,Qs=null,K,Qr=null;
    String msg="",k="",ph="",temp="";
    Boolean sender=false;
    
    AES aes=new AES();
    
    // Reference to the smsWrite instance
    private SMSWrite smsWrite;
    
    // Reference to the smsRead instance
    private SMSRead smsRead;
    
    //Constructor
    public Ecc(SMSWrite smsWrite) 
    	{
        	this.smsWrite = smsWrite;
    	}
    
    //Constructor
    public Ecc(SMSRead smsRead) 
	{
    	this.smsRead = smsRead;
	}

      
    public void recv(String phn, String smsg)
    	{
 
    		//splitting the keywords
	    	String[] spl1=smsg.split("\\.");
			
	    	if("pmg".equals(spl1[0]))
				{
					System.out.println("Plain Text Entered");
	    			//basic plain text entered by sender
					sender = true;
					ph=phn;
			    	msg=spl1[1];//store plain test in global variable
			       	startECC();//start ecc at sender side
				}
			
	    	if("key".equals(spl1[0]))//test for key
    			{
	    			System.out.println("Key received");
	    			if(sender)//testing if this is sender side or receiver side
						{
							// for senders end
							startKey(spl1[1]);//generate secret key of sender once receiver public key is received
							System.out.println("Encrypting Message");
							encrypt(msg);
						}
    				//for receivers end
    				else
						{
			        		temp=spl1[1];//public key of sender is stored st receiver end
			        		ph=phn;
			        		startECC();// start ecc at receivers end
						}
    			}
	
	    	if("emg".equals(spl1[0]))
				{
	    			System.out.println("Encrypted Text Received");
	    			System.out.println("Decrypting Message");
					//encrypted test sent to receiver
			       	ph=phn;//store number
					startKey(temp);//generate secret key at receiver side
				    decrypt(spl1[1]); 
				}        
    	}
    
	public void startECC()
		{
			System.out.println("Ecc Started");
			String msgs,pqx,pqy;
			ECPoint pQ;
			try
				{
					iniatilization();
				}
			catch(Exception e)
				{
					System.out.println(e);
				}
			randomSelection();
			pQ=publicValue();
			pqx=pQ.x().toString();
			pqy=pQ.y().toString();
			msgs = "key."+pqx+","+pqy;
			//sending public key to the receiver
			sendSMS(ph,msgs);//send public value
		}
	
	public void startKey(String rQ)
		{
			// get public key
		System.out.println("Key Generation Started");
			String[] spl=rQ.split("\\,");
	   		String Qrx,Qry;
	   		Qrx=spl[0]; 
	   		Qry=spl[1];
		   	BigInteger qsx=convBigInteger(Qrx);
		   	BigInteger qsy=convBigInteger(Qry);
		   	Qr = new ECPoint(qsx,qsy,params);//generate ECPoint public key
		   	secretValue(Qr);//generate secret
		}
	
	/*
			Trigger the 
			receiver in 
			main algorithm 
			on android
	   */

    public void iniatilization() throws Exception
		{
			// Initialization of paramets
	         p = new BigInteger ("6277101735386680763835789423207666416083908700390324961279");
			 a = new BigInteger ("-3") .mod (p);
			 b = new BigInteger ("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1",16);
			 params = new ECParameters (p, a, b);
		   
	                 // Calculation of Generator Point, n
			 G = new ECPoint
				(new BigInteger ("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff10123345",16),
				 new BigInteger ("07192b95ffc8da78631011ed6b24cdd573f977a11e7948112345",16),
				 params);
			 n = new BigInteger ("6277101735386680763835789423176059013767194773182842284081");
			
	    }

	public void randomSelection()
        {
			Random prng;
	       long seed = 125L;
	       prng = new Random (seed);
				 
  			//d is the sender private key
               // Step 1 : Select random number d such that d <n
			d = new BigInteger (192, prng);
                if (d.compareTo (n) > 0)
                    d = d.subtract (n);
        }	   
	   
    public ECPoint publicValue()
      	{
			//Q is the sender public key
            // Step 2: calculate Q= d. G
            Qs = G.multiply (d);
       		return (Qs);
       	}
	   
		// calculation of K
    public void secretValue(ECPoint Qk)
    	{
            // Sender Side Step 5 : calculation of secret value Ks = d * Qr
    		K = Qk.multiply (d);
	       	String res = (K.x().multiply(K.y())).toString(); 
	    	k = res.substring(0,16);
		}
			      
    public void decrypt(String data)
    	{
    
    		aes.setKey(k);  // choose 16 byte password
    		System.out.println("Decryption");
			//Decryption
			    	System.out.println("\nCipher text : ["+data+"] ["+data.length()+" bytes]");
					String unencrypted = ((AES) aes).Decrypt(data); 
					System.out.println("\nPlain text : ["+unencrypted+"] ["+unencrypted.length()+" bytes]");
					String plain = unencrypted;
					Toast.makeText(smsRead, "Decrypted", 
			                Toast.LENGTH_SHORT).show();
					smsRead.update("From:" +ph+"\n\n"+
							"Plain Message:"+plain+"\n");
				
		}
			    
	public void encrypt(String data)
		{
			System.out.println("Encryption");
				aes.setKey(k);  // choose 16 byte password
				System.out.println("Data:"+data);
			    	//Encryption
			    	
			    	System.out.println("\nPlain text : ["+data+"] ["+data.length()+" bytes]");
			    	String encrypted = ((AES) aes).Encrypt(data); 
					System.out.println("\nCypher text : ["+encrypted+"] ["+encrypted.length()+" bytes]");
					encrypted = "emg."+encrypted;
					System.out.println("Cypher Size:"+encrypted.length());
					Toast.makeText(smsWrite, "Encrypted", 
			                Toast.LENGTH_SHORT).show();
					sendSMS(ph,encrypted);	
					Toast.makeText(smsWrite, 
	                        "Encrypted message sent", 
	                        Toast.LENGTH_SHORT).show();
	            	
			    }
					
		public static BigInteger convBigInteger (String str){
		
                   byte [ ] asciiValues = str.getBytes ( );
                   return new BigInteger (asciiValues);
            }
        public static String bytesToHex(byte[] b){
                char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                                     '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                StringBuffer buf = new StringBuffer();
                for (int j=0; j<b.length; j++) {
                        buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
                        buf.append(hexDigit[b[j] & 0x0f]);
                }
                return buf.toString();
            }              
        public void sendSMS(String phoneNumber, String message)
    	{      
        
        	/*
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, test.class), 0);                
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phoneNumber, null, message, pi, null);        
            */
        	
        	String SENT = "SMS_SENT";
        	String DELIVERED = "SMS_DELIVERED";
        	final Context cont;
        	if (sender){
        		cont=smsWrite;
        	}
        	else{
        		cont=smsRead;
        	}
            PendingIntent sentPI = PendingIntent.getBroadcast(cont, 0,
                new Intent(SENT), 0);
            
            PendingIntent deliveredPI = PendingIntent.getBroadcast(cont, 0,
                new Intent(DELIVERED), 0);
        	
            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver(){
    			@Override
    			public void onReceive(Context arg0, Intent arg1) {
    				switch (getResultCode())
    				{
    				    case Activity.RESULT_OK:
    					    Toast.makeText(cont, "SMS sent", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    				    	Toast.makeText(cont, "Generic failure", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				    case SmsManager.RESULT_ERROR_NO_SERVICE:
    				    	Toast.makeText(cont, "No service", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				    case SmsManager.RESULT_ERROR_NULL_PDU:
    				    	Toast.makeText(cont, "Null PDU", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				    case SmsManager.RESULT_ERROR_RADIO_OFF:
    				    	Toast.makeText(cont, "Radio off", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				}
    			}
            }, new IntentFilter(SENT));
            
            //---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver(){
    			@TargetApi(Build.VERSION_CODES.DONUT)
    			@SuppressLint("NewApi")
    			@Override
    			public void onReceive(Context arg0, Intent arg1) {
    				switch (getResultCode())
    				{
    				    case Activity.RESULT_OK:
    				    	Toast.makeText(cont, "SMS delivered", 
    					    		Toast.LENGTH_SHORT).show();
    					    break;
    				    case Activity.RESULT_CANCELED:
    				    	Toast.makeText(cont, "SMS not delivered", 
    					    		Toast.LENGTH_SHORT).show();
    					    		break;					
    				}
    			}
            },new IntentFilter(DELIVERED));        
        	System.out.println("Sending message");
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            
        }


		private void registerReceiver(BroadcastReceiver broadcastReceiver,
				IntentFilter intentFilter) {
			// TODO Auto-generated method stub
			
		}

}
