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
 * The class is used as a server to receive and to return packets according to 
 * the inputed size
 */

package cs15a_project3_component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TCPServerSocketController extends SocketController
{
    private ServerSocket mServerSocket;
    
    private ArrayList<TCPClientSocketController> mClientList;
    
    private boolean mNoDelay;
    private int mTraffic;
    
	/**
	 * Constructor to initialize variables
	 * @param pAddress The address that the server will host on
	 * @param pPort The port that the server will listen on
	 */
    public TCPServerSocketController(InetAddress pAddress, int pPort)
    {
        super(pAddress, pPort);
        
        mServerSocket = null;
        
        TAG = "Server";
        
        // Initialize as no delay and traffic class to reliable
        mNoDelay = false;  
        mTraffic = 0x04;
    }  // TCPServerSocketController
    
    public void establishInputOutput()
    {
            try
            {
            	// Create the server according to the inputed address and port
                mServerSocket = new ServerSocket(mPort, 0, mAddress);
                System.out.println(TAG + " " + mServerSocket.getLocalSocketAddress());
                // Create an arraylist of SocketController to store clients
                mClientList = new ArrayList<TCPClientSocketController>();
            } 
            catch (IOException pException)
            {
            	// Print out error message and prevent the thread from running
                pException.printStackTrace();
                mRunning = false;
            }  // catch
    }  // establishInputOutput
    
    public void run()
    {
    	// If the flag is false, the thread will exit and close
        if(mRunning == false)
        {
            return;
        }  // if
    	
        while(mRunning)
        {
            try
            {
            	// Listen for any new client and cloned a client socket to respond to the client
                TCPClientSocketController lClient = new TCPClientSocketController(mServerSocket.accept());
                lClient.establishInputOutput();
                lClient.setSize(mSize);
                lClient.setIteration(mIteration);
                lClient.setTAG(TAG);
                lClient.setTraffic(mTraffic);
                lClient.setNoDelay(mNoDelay);
                
                // Add to the list of clients
                mClientList.add(lClient);
                
                // Start the client thread
                lClient.start();
                
                // Sleep for DELAY time
                Thread.sleep(DELAY);
            }  // try
            catch(InterruptedException pException)
            {
            		// Exit the loop and closed the thread
                    mRunning = false;
            }  // catch
            catch (IOException pException)
            {
            	// Error in sending and receiving data
            	// Exit the loop and closed the thread
                mRunning = false;
            }  // catch
        }  // while
        
        try 
        {
        	// Close the server socket
			mServerSocket.close();
		}  // try
        catch (IOException pException) 
		{
        	// Error in closeing the server socket
			pException.printStackTrace();
		}  // catch
    }  // void run
    
    @Override
    public void setSize(int pSize)
    {
    	mSize = pSize;
    	// Loop through all clients and set the inputed size
    	for(SocketController lController : mClientList)
    	{
    		lController.setSize(mSize);
    	}  // for
    }  // void setSize
    
    @Override
    public void setIteration(int pIteration)
    {
    	mIteration = pIteration;
    	// Loop through all clients and set the inputed iteration
    	for(SocketController lController : mClientList)
    	{
    		lController.setIteration(mIteration);
    	}  // for
    }  // void setIteration
    
    /**
     * Disable or enable Nagle's algorithm on the client socket
     * @param pInput The input to enable or to disable Nagle's algorithm
     *                true to enable and false to disable
     */
    public void setNoDelay(boolean pInput)
    {
        mNoDelay = pInput;
        
        // Loop through all clients
        for(TCPClientSocketController lController : mClientList)
        {
            lController.setNoDelay(pInput);
        }  // for
    }  // void setNoDelay
    
    /**
     * Set the traffic class for the client socket
     * @param pInput The input to for the traffic class for the client socket
     */
    public void setTraffic(int pInput)
    {
        mTraffic = pInput;
        // Loop through all clients
        for(TCPClientSocketController lController : mClientList)
        {
            lController.setTraffic(pInput);
        }  // for
    }  // void setTraffic
}  // class TCPServerSocketController
