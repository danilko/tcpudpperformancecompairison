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
 * The base blueprints for all socket controllers
 */

package cs15a_project3_component;

import java.net.InetAddress;

public abstract class SocketController extends Thread
{
    protected long DELAY = 1;
    
    protected int mSize;
    
    protected int mIteration;
    
    protected InetAddress mAddress;
    protected int mPort;
    
    protected boolean mRunning;
    
    protected byte [] mSendBytes;
    
    protected String TAG;
    
    /**
     * Constructor to initialize variables
     * @param pAddress The address that the socket will host on
     * @param pPort The port that the socket will listen on
     */
    public SocketController(InetAddress pAddress, int pPort)
    {
        mAddress = pAddress;
        mPort = pPort;
        mRunning = true;
        mSendBytes = null;
        
        mSize = 0;
        mIteration = 0;
        
        TAG = "TAG";
    }  // SocketController
    
    /**
     * To establish socket, and its input and output streams
     */
    public abstract void establishInputOutput();
    
    /**
     * To prepare bytes to send according to inputed size
     */
    public void send()
    {    	
        mSendBytes = new byte[mSize];
    }  // void send
    
    /**
     * Get the address for the controller
     * @return the Address for the controller
     */
    public InetAddress getAddress()
    {
        return mAddress;
    }  // InetAddress getAddress

    /**
     * Set the address for the controller
     * @param pAddress 	The inputed address for
     * 					the controller
     */
    public void setAddress(InetAddress pAddress)
    {
        mAddress = pAddress;
    }  // void setAddress

    /**
     * Set the address for the controller
     * @return the Size for the controller
     */
    public int getSize()
    {
        return mSize;
    }  // int getSize
    
    /**
     * Set the size for the controller
     * @param Size The inputed size for
     * 					the controller
     */
    public void setSize(int pSize)
    {
        mSize = pSize;
    }  // void setSize
    
    /**
     * Set the Iteration for the controller
     * @param pIteration
     */
    public void setIteration(int pIteration)
    {
        mIteration = pIteration;
    }  // void setIteration

    /**
     * Get the Iteration for the controller
     * @return the Iteration for the controller
     */
    public int getIteration()
    {
        return mIteration;
    }  // int getIteration

    
    /**
     * Get the port for the controller
     * @return the Port for the controller
     */
    public int getPort()
    {
        return mPort;
    }  // int getPort

    /**
     * Set the port for the controller
     * @param mPort The inputed port to be set 
     * 				for the controller
     */
    public void setPort(int pPort)
    {
        mPort = pPort;
    }  // void setPort
    
    /**
     * Get the TAG for the controller
     * @return the TAG of the controller
     */
    public String getTAG()
    {
        return TAG;
    }  // int getPort

    /**
     * Set the TAG for the controller
     * @param pTAG	The inputed port to be set 
     * 				for the controller
     */
    public void setTAG(String pTAG)
    {
        TAG = pTAG;
    }  // void setPort
}  // class SocketController
