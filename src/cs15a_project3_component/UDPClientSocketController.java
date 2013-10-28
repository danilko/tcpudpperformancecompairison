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
 * inputed size and iterations through UDP Protocol
 * 
 */

package cs15a_project3_component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPClientSocketController extends SocketController
{
	DatagramSocket mDatagramSocket;

	int DEFAULT_TIMEOUT = 600000;

	/**
	 * Constructor to initialize variables
	 * @param pAddress The address that the client will host on
	 * @param pPort The port that the client will listen on
	 */
	public UDPClientSocketController(InetAddress pAddress, int pPort)
	{
		// Call parent constructor to initialize variables
		super(pAddress, pPort);

		mDatagramSocket = null;
	}  // UDPClientSocketController

	@Override
	public void establishInputOutput()
	{
		try
		{
			// Create a new Datagram socket and bind it to a system assigned port
			mDatagramSocket = new DatagramSocket();    
			mDatagramSocket.setSoTimeout(DEFAULT_TIMEOUT);

			System.out.println("Client Establish Successfully " + mDatagramSocket.getPort());
		}  // try 
		catch (SocketException pException)
		{
			// Print out error meesage and prevents it from running
            pException.printStackTrace();
			System.out.println("Socket cannot be created");
			mRunning = false;
		}  // catch
		catch (SecurityException pException)
		{
			// Print out error meesage and prevents it from running
            pException.printStackTrace();
			System.out.println("Security Setting not allowed port to be opened");
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
				// Only send information if send bytes is not null
				if(mSendBytes != null)
				{
					// Fill the each element in the array
					for(int lIndex = 0; lIndex < mSize; lIndex++)
					{
						mSendBytes[lIndex] = lTestByte;
					}  // for

					// Prepare variables to store data
					int lReceivedPacket = 0;

					long lTotalLatency = 0;
					long lLatency = 0;

					// Set the default timeout as current timeout
					int lTimeout = DEFAULT_TIMEOUT;

					// Loop through the iteration
					for(int lIndex = 0; lIndex < mIteration; lIndex++)
					{   
						// Prepare a DatagramPacket to receive data
						DatagramPacket lPacket= new DatagramPacket(mSendBytes, mSendBytes.length, mAddress, mPort);  

						// Set the start latency for this round trip time
						lLatency = System.currentTimeMillis();
						// Send the packet
						mDatagramSocket.send(lPacket);

						// Prepare a byte array to receive data
						byte [] mByte = new byte[mSize];

						// Prepare a DatagramPacket to receive data
						lPacket = new DatagramPacket(mByte, mByte.length, mAddress, mPort);
						try
						{
							mDatagramSocket.receive(lPacket);

							if(lPacket.getData()[0] != lTestByte)
							{
								continue;
							}  // if
							
							// Get the latency for this round trip time
							lLatency = System.currentTimeMillis() - lLatency;
							// Add to total latency for this round trip time
							lTotalLatency = lTotalLatency + lLatency;

							// If the current 2 * RTT is less than the default timeout at the first round
							// Set the value as the default timeout
							if(((2 * lLatency) < lTimeout) && (lIndex == 0))
							{
								lTimeout = (int)(2*lLatency);
								mDatagramSocket.setSoTimeout(lTimeout);
							}  // if

							// Increase the number of success packets
							lReceivedPacket++;

							// Printout a message
							//System.out.println(TAG + " send and respond " 
							//		+ lPacket.getLength() + " successfully at iteration " + lIndex);
						}  // try
						catch(SocketTimeoutException pException)
						{	
						    if((lTimeout * 2) < DEFAULT_TIMEOUT)
						    {
						        lTimeout = lTimeout * 2;
						    }  // if
						    else
						    {
						        lTimeout = DEFAULT_TIMEOUT;
						    }  // else
						    
						    mDatagramSocket.setSoTimeout(lTimeout);
							// If timeout, do not add to total latency and add to success packets
							//System.out.println(TAG + " timeout and packet lost at iteration " + lIndex);
						}  // catch
					}  // for
					System.out.println("UDP: ");

					// Calculate and printout the Average RTT by total latency divide by receive packets
					float lRTT = 0;
					if(lReceivedPacket > 0)
					{
						lRTT = (float)(lTotalLatency / (float)lReceivedPacket) / (float)(1000f); 
					}  // if
					System.out.println("UDP Average RTT: " + lRTT + " s");

					// Calculate and printout the number of lost packets
					System.out.println("UDP Lost Packet: " + (mIteration - lReceivedPacket) + " Packet(s)");

					// Calculate and printout the effective throughput
					// (Size per packet(B) * 8(b/B) * Number of Received Packet(s)) / Latency(ms) * 1/1000(ms/s)
					float lThroughput = 0;
					if(lReceivedPacket > 0)
					{
						lThroughput = (8 * mSize * lReceivedPacket) / (lTotalLatency / 1000f);
					}  // if
					System.out.println("UDP Effective Throughput: " + lThroughput + " bps");

					mSendBytes = null;
				}  // if

				// Sleep for 3 milli seconds
				Thread.sleep(DELAY);
			}  // try
			catch(InterruptedException pException)
			{
				// Prepare to exit the loop to close the thread
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
}  // class UDPClientSocketController
