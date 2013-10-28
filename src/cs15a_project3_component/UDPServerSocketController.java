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
 * @author Kai - Ting (Danil) KO
 * The class is used as a server to receive and to return packets according to 
 * the inputed size
 */

package cs15a_project3_component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPServerSocketController extends SocketController
{
    DatagramSocket mDatagramSocket;
    
    /**
     * Constructor to initialize variables
     * @param pAddress The address that the client will host on
     * @param pPort The port that the client will listen on
     */
    public UDPServerSocketController(InetAddress pAddress, int pPort)
    {
        super(pAddress, pPort);
        
        mDatagramSocket = null;
    }  // UDPServerSocketController
    
    public void establishInputOutput()
    {
        try
        {
        	// Create the tread according to the inputed address and port
            mDatagramSocket = new DatagramSocket(mPort, mAddress);     
            System.out.println("Server Establish Successfully " + mDatagramSocket.getInetAddress());
        }  // try 
        catch (SocketException pException)
        {
        	// Cannot establish socket. Stop the thread from running
            pException.printStackTrace();
            mRunning = false;
        }  // catch
    }  // establishInputOutput
    
    @Override
    public void run()
    {
        // Make a test byte
        byte lTestByte = "A".getBytes()[0];
    	
        while(mRunning)
        {
            try
            {
                // Prepare a DatagramPacket to receive data
                byte [] mByte = new byte[mSize];
                DatagramPacket lPacket= new DatagramPacket(mByte, mByte.length);
                
                // Retrive the packet
                mDatagramSocket.receive(lPacket);
                
				if(lPacket.getData()[0] != lTestByte)
				{
					continue;
				}  // if
                
                // Printout the message
                System.out.println("UDP Received Message Client: "+ lPacket.getAddress());
                
                // Get the data and prepare it in a DatagramPacket to send it back
                mByte = lPacket.getData();
                lPacket= new DatagramPacket(mByte, mByte.length, lPacket.getAddress(), lPacket.getPort());
                
                // Sends the DatagramPacket
                mDatagramSocket.send(lPacket); 

                // Sleep for DELAY seconds
                Thread.sleep(DELAY);
            }  // try
            catch(InterruptedException pException)
            {
            	// Exit the loop and close the thread
                pException.printStackTrace();
                mRunning = false;
            }  // catch
            catch (UnknownHostException pException)
            {
            	// Cannot connect to host
                pException.printStackTrace();
                System.out.println("Cannot send packets to Unkown Host");
            }  // catch
            catch (IOException pException)
            {
            	// Error in sending and receiving data
            	// Prepare to exit the loop to close the thread
                pException.printStackTrace();
                System.out.println("IO Error");
                mRunning = false;
            }  // catch
        }  // while
        
        // Close the socket
        mDatagramSocket.close();
    }  // run
}  // class UDPServerSocketController
