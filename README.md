Securing-SMS
============


===========================================================================================================
Objective:
===========================================================================================================

The main objective of the project is to send messages from one mobile 

phone to another in a secure and authenticated manner.

===========================================================================================================
Existing System: 
===========================================================================================================

The necessity of providing security to SMS has been imperative since a long 

time and many algorithms and techniques have been implemented in various 

platforms to try and provide security to the messages. At present there are 

many algorithms based on symmetric cryptography that provides security 

to the messages transferred based on a shared secret key. The main 

disadvantage of a secret-key cryptosystem is related to the exchange of 

keys. Symmetric encryption is based on the exchange of a secret. Therefore 

problem of key distribution arises. Private-key systems need to use keys 

that are at least as long as the message to be encrypted. Symmetric 

encryption requires that a secure channel be used to exchange the key, 

which seriously diminishes the usefulness of this kind of encryption system 

when we talk about SMS.

===========================================================================================================
Proposed System:
===========================================================================================================

The proposed system uses the concept of elliptic curve cryptography along 

with AES to encrypt the message and send it over a common channel. The 

sender writes a message and gives the recipient’s number, when he sends 

the message the algorithm is triggered on both the devices. The keys are 

generated and shared among the devices and the encryption takes place at 

the senders end. After encryption, the message is sent to the receiver and 

he decrypts it using his key to read it.

===========================================================================================================
MODULES:
===========================================================================================================

• Sender

• Receiver

_______________________________________________________________________________
Sender

The sender types in the receiver’s contact number and the message 

and sends the public key to the receiver. 

Once he receives the public key of the receiver he accepts it then the 

message is encrypted and sent to the receiver.
_______________________________________________________________________________
Receiver:

The receiver first receives the public key of the sender accepts it and 

sends his own public key to the sender. 

When the encrypted message is received, it is decrypted and displayed 

to the receiver.


===========================================================================================================
Details:
===========================================================================================================
Android SMS Application used to send secure sms between users using a combination of ECC and AES.

The MainActivity.java file is the first page of the SMS application that provides options to send or receive SMS.

The SMSWrite.java file is the sender interface that allows the user to type the message, send the key, accept the receiver key and automatically send the encrypted message.

The Ecc.java file is the android file that contains my basic algorithm which uses the mobile number to establish a connection with the receiver and sends the message to AES.java for encryption and decryption.

The ECParameters.java file provides the paramerters to create the required elliptic curve variabales.

The ECPoint.java file provides the methods that create 128 bit Elliptic Curve points.

The AES.java file is an open source java code for the implementation of AES algorithm.

The SMSRead.java is the receiver interface that allows the user to receive the public and then view the decrypted message.


