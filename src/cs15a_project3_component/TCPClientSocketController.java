/**
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Kai-Ting (Danil) Ko
 * 
 * Permission is hereby granted, free of charge, 
 * to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the 
 * following conditions:
 * 
 * The above copyright notice and this permission notice 
 * shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY 
 * OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

/**
 * @author Kai - Ting (Danil) Ko
 * 
 * The class is used as a client to receive and to send bytes according to 
 * inputed size and iterations through TCP Protocol. It can be a stand alone
 * client or a cloned of a server to response to a particular client
 * 
 */

package cs15a_project3_component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TCPClientSocketController extends SocketController
{
    private Socket mSocket;
    private DataOutputStream mOut;
    private DataInputStream mIn;
    
    /**
     * Constructor to initialize variables and to prepare the controller
     * as a cloned of the server to response to a particular client
     * @param pSocket the Socket that the controller will use
     */
    public TCPClientSocketController(Socket pSocket)
    {
        super(pSocket.getInetAddress(), pSocket.getPort());
        System.out.println("Server accepts a new client");
        TAG = "Default Server";
        
        mSocket = pSocket;
        
        mOut = null;
        mIn = null;
    }  // TCPClientSocketController
    
    /**
     * Constructor to initialize variables and to prepare the controller
     * as a stand alone TCP client
     * @param pAddress The address that the client will host on
     * @param pPort The port that the client will listen on
     */
    public TCPClientSocketController(InetAddress pAddress, int pPort)
    {
        super(pAddress, pPort);
        TAG = "Default Client";
        
        mSocket = null;
        
        mOut = null;
        mIn = null;
    }  // TCPClientSocketController
    
    @Override
    public void establishInputOutput()
    {
        try
        {
        	// If the socekt is null, create a new socket with the
        	// inputed address and port
            if(mSocket == null)
            {
                mSocket = new Socket(mAddress, mPort);
            }  // if
            
            // Create the output and input streams
            // and prinout successful message
            mOut = new DataOutputStream(mSocket.getOutputStream());
            mIn = new DataInputStream(mSocket.getInputStream());
            System.out.println( TAG + " Establish Success");
        }  // try
        catch(UnknownHostException pException)
        {
        	// Cannot connect to the server, prevent the thread from running
            pException.printStackTrace();
            System.out.println("Cannot establish connection to IP = " + mAddress);
            mRunning = false;
        }  // catch
        catch(IOException pException)
        {
        	// Cannot create the output and/or input streams, 
        	// prevent the thread from running
            pException.printStackTrace();
            System.out.println("Cannot establish output");
            mRunning = false;
        }  // catch
    }  // establishInputOutput
    
    @Override
    public void run()
    {   
    	// If the flag is false, the thread will exit and close
        if(mRunning == false)
        {
            return;
        }  // if
        
        // Make a test byte
        byte lTestByte = "A".getBytes()[0];
        
        // Continue to run until the flag is false
        while(mRunning)
        {
            try
            {
            	  // If it is a client socket for the server to respond to clients
                  if(TAG == "Server")
                  {
                	  // Prepare the byte array accroding to the inputed size
                	  byte [] lReceivedBytes = new byte[mSize];
                	  
                	  while(true)
                	  {
                		  mIn.readFully(lReceivedBytes);
	                	  // Only respond if received byte(s) are receive fully
	                	  if(lReceivedBytes[mSize - 1] == lTestByte)
	                	  {
	                		  // Set the send bytes as the received bytes
	                		  mSendBytes = lReceivedBytes;
	
	                		  // Print out message
	                		  System.out.println("TCP Received Message Client: " + mAddress);
	
	                		  // Send original bytes back to the client
	                		  mOut.write(mSendBytes, 0, mSendBytes.length);
	                		  mOut.flush();
	
	                		  // Set send bytes as null
	                		  mSendBytes = null;
	                		  
	                		  break;
	                	  }  // if
                	  } // while
                    }  // if
                  	// If it is a stand alone client
                    else
                    {
                    		// Only send information if send bytes is not null
                            if(mSendBytes != null)
                            {
                            	// Fill the each element in the array
                                for(int lIndex = 0; lIndex < mSize; lIndex++)
                                {
                                    mSendBytes[lIndex] = lTestByte;
                                }  // for
                                
                                // Prepare variables to store data
                                long mLatency = 0;
                                long lLatency = 0;
                                int lReceivedPacket = 0;
                                
                                // Loop through the iteration
                                for(int lIndex = 0; lIndex < mIteration; lIndex++)
                                {
                                	// Set the start time for the RTT
                                    lLatency = System.currentTimeMillis();
                                    mOut.write(mSendBytes, 0, mSendBytes.length);
                                    mOut.flush();
                                    
                                    // Retrieve bytes
                                    byte [] lReceivedBytes = new byte[mSize];
                                    
                                    while(true)
                                    {
	                                    mIn.readFully(lReceivedBytes);
	                                    if(lReceivedBytes[mSize - 1] == lTestByte)
	                                    {
	                                    	lReceivedPacket++;
	                                        break;
	                                    }  // if
                                    }  // while
                                	// Calculate current RTT and add it to total latency
                                    lLatency = System.currentTimeMillis() - lLatency;
                                    mLatency = mLatency + lLatency;
                                    
                                    //System.out.println(TAG + " send and respond " + lReceivedByteLength + " bytes successfully at iteration " + lIndex);
                                }  // for
                                
                                System.out.println("TCP: ");
                                
                                // Calculate and printout RTT
                                float lRTT = 0;
                                if(mLatency > 0)
                                {
                                	lRTT = (float)(mLatency / (float)mIteration) / (float)(1000f); 
                                }  // if
                                System.out.println("TCP Average RTT: " + lRTT + " s");

                                // Calculate and printout the number of lost packets
                                System.out.println("TCP Lost Packet: " + (mIteration - lReceivedPacket) + " Packet(s)");
                                
                                // Calculate and printout effective throughput
                                // (Size per packet(B) * 8(b/B)) / Average RTT
                                float lThroughput  = 0;
                                if(mLatency > 0)
                                {
                                    lThroughput = (8 * mSize * lReceivedPacket) / (mLatency / 1000f);
                                }  // if
                                System.out.println("TCP Effective Throughput: " + lThroughput + " bps");
                              
                                // Set send byte to null to avoid to transmit again
                                mSendBytes = null;
                            }  // if
                    }  // else
                
                // Sleep for DELAY time
                Thread.sleep(DELAY);
            }  // try
            catch(InterruptedException pException)
            {
            	// Exit the loop and close the thread
                pException.printStackTrace();
                mRunning = false;
            }  // catch
            catch (IOException pException)
            {
            	// Error in sending or receiving data
            	// Exit the loop and close the thread
                pException.printStackTrace();
                mRunning = false;
            }  // catch
         }  // while
        
        try
        {
        	// Close all input and output streams
            mOut.close();
            mIn.close();
        	// Close the socket
            mSocket.close();
        } 
        catch (IOException pException)
        {
        	// Error in close the input or the out streams
            pException.printStackTrace();
        }  // catch
    }  // void run
    
    /**
     * Disable or enable Nagle's algorithm on the client socket
     * @param pInput The input to enable or to disable Nagle's algorithm
     *                true to enable and false to disable
     */
    public void setNoDelay(boolean pInput)
    {
        try
        {
            mSocket.setTcpNoDelay(pInput);
        }  // try 
        catch (SocketException pException)
        {
            pException.printStackTrace();
        }  // catch
    }  // void setNoDelay
    
    /**
     * Set the traffic class for the client socket
     * @param pInput The input to for the traffic class for the client socket
     */
    public void setTraffic(int pInput)
    {
        try
        {
            mSocket.setTrafficClass(pInput);
        }  // try 
        catch (SocketException pException)
        {
            pException.printStackTrace();
        }  // catch
    }  // void setTraffic
}  // TCPClientSocketController
