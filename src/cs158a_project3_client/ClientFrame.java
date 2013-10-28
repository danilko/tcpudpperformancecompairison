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
 * The UI for the application
 */

package cs158a_project3_client;

import javax.swing.JFrame;
import javax.swing.JButton;

import cs15a_project3_component.TCPClientSocketController;
import cs15a_project3_component.TCPServerSocketController;
import cs15a_project3_component.UDPClientSocketController;
import cs15a_project3_component.UDPServerSocketController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

public class ClientFrame
{
    private int counter;
    private JFrame frame;
    private JTextField txtAddress;
    private JTextField txtServerPort;

    private TCPClientSocketController mTCPClient;
    private UDPClientSocketController mUDPClient;
    private TCPServerSocketController mTCPServer;
    private UDPServerSocketController mUDPServer;
    
    private JTextField txtSize;
    private JTextField txtIteration;
    
    private JRadioButton rdbtnNoDelay;
    private JRadioButton rdbtnTrafficClassLowCost;
    
    /**
     * Constructor to create the UI
     */
    public ClientFrame()
    {
        initialize();
    }  // ClientFrame()

    /**
     * Convert the input string to an InetAddress object
     * @param pString The inputed string for conversion
     * @return the converted InetAddress object or null
     * 		   if the conversion failed
     */
    private InetAddress getInetAddress(String pString)
    {
    	// Create an Inet object
        InetAddress lAddress;
        try
        {
        	// COnverted the string
            lAddress = InetAddress.getByName(txtAddress.getText());
        } catch (UnknownHostException pException)
        {
            System.out.println("Address cannot be bind");
            
            return null;
        }  // catch
        
        return lAddress;
    }  // InetAddress getInetAddressByString

    /**
     * Set whether the UI is visible or not
     * @param pVisible The inputed boolean to set whether the UI
     * 				   is visible or not
     */
    public void setVisible(boolean pVisible)
    {
        frame.setVisible(pVisible);
    }  // void setVisible

    /**
     * Convert the inputed string to an Integer
     * @param pValue The inputed string for conversion
     * @return the converted Integer
     */
    private int getValue(String pValue)
    {
        int lValue = 0;
        try
        {
        	// Convert the string to an Integer
            lValue = Integer.parseInt(pValue);
        }  // try
        catch(NumberFormatException pException)
        {
        	System.out.println("Values cannot be accepted");
            return -1;
        }  // catch
        
        return lValue;
    }  // int getPort
    
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
    	// Initialize variables
        mTCPClient = null;
        mUDPClient = null;
        counter = 0;
        frame = new JFrame();
        
        // Set frame size
        frame.setBounds(100, 100, 450, 573);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        // Create a label for the UDP panel
        JLabel lblUdpSocket = new JLabel("UDP Socket");
        lblUdpSocket.setBounds(12, 249, 86, 14);
        frame.getContentPane().add(lblUdpSocket);
        
        // Created a panel and its its contents
        JPanel panelUDP = new JPanel();
        panelUDP.setBounds(0, 281, 434, 86);
        frame.getContentPane().add(panelUDP);
        GridBagLayout gbl_panelUDP = new GridBagLayout();
        gbl_panelUDP.columnWidths = new int[]{179, 119, 200, 0};
        gbl_panelUDP.rowHeights = new int[]{29, 9, 0, 0};
        gbl_panelUDP.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
        gbl_panelUDP.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelUDP.setLayout(gbl_panelUDP);
        
        // Set the button to create a UDP server
        JButton btnUDPServer = new JButton("Server");
        btnUDPServer.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Collect necessary information
            	InetAddress lAddress = getInetAddress(txtAddress.getText());
                int lPort = getValue(txtServerPort.getText());
                int lSize = getValue(txtSize.getText());
                int lIteration = getValue(txtIteration.getText());
                
                // Check the information is valid
                if(lAddress == null || lPort == -1 || lIteration == -1 || lSize == -1)
                {
                    return;
                }  // if
                
                // Create a new UDP server according to the inputed information
                mUDPServer = new UDPServerSocketController(lAddress, lPort);
                mUDPServer.establishInputOutput();
                mUDPServer.setIteration(lIteration);
                mUDPServer.setSize(lSize);
                mUDPServer.start();
            }  //void actionPerformed
        });  // btnUDPServer.addActionListener
        // Create the grid to hold the UDP Server Button
        GridBagConstraints gbc_btnUDPServer = new GridBagConstraints();
        gbc_btnUDPServer.insets = new Insets(0, 0, 5, 5);
        gbc_btnUDPServer.fill = GridBagConstraints.BOTH;
        gbc_btnUDPServer.gridx = 1;
        gbc_btnUDPServer.gridy = 1;
        panelUDP.add(btnUDPServer, gbc_btnUDPServer);
        
        // Set the button to create a UDP client
        JButton btnUDPClient = new JButton("Client");
        btnUDPClient.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Collect necessary information
            	InetAddress lAddress = getInetAddress(txtAddress.getText());
                int lPort = getValue(txtServerPort.getText());
                int lSize = getValue(txtSize.getText());
                int lIteration = getValue(txtIteration.getText());
                
                // Check the information is valid
                if(lAddress == null || lPort == -1 || lIteration == -1 || lSize == -1)
                {
                    return;
                }  // if
                
                // Create a new UDP client according to the inputed information
                mUDPClient = new UDPClientSocketController(lAddress, lPort);
                mUDPClient.establishInputOutput();
                mUDPClient.setTAG("Client " + counter);
                mUDPClient.setIteration(lIteration);
                mUDPClient.setSize(lSize);
                mUDPClient.start();
                counter++;
            }  // actionPerformed
        });  // btnUDPClient.addActionListener
        // Create the grid to hold the UDP Client Button
        GridBagConstraints gbc_btnUDPClient = new GridBagConstraints();
        gbc_btnUDPClient.insets = new Insets(0, 0, 0, 5);
        gbc_btnUDPClient.fill = GridBagConstraints.BOTH;
        gbc_btnUDPClient.gridx = 1;
        gbc_btnUDPClient.gridy = 2;
        panelUDP.add(btnUDPClient, gbc_btnUDPClient);
        
        // Create the button to send the UDP bytes
        JButton btnUDPSend = new JButton("Send");
        btnUDPSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check to see if a UDP client is available
            	if(mUDPClient != null)
                {
            		// Call to prepare to send data
                    mUDPClient.send();
                }  // if
                else
                {
                    System.out.println("Client do not exist");
                }  // else
            }  // void actionPerformed
        });  // btnUDPSend.addActionListener
        // Create the grid to hold the UDP Send Button
        GridBagConstraints gbc_btnUDPSend = new GridBagConstraints();
        gbc_btnUDPSend.gridx = 2;
        gbc_btnUDPSend.gridy = 2;
        panelUDP.add(btnUDPSend, gbc_btnUDPSend);
        
        // Create a label for the TCP panel
        JLabel lblTcpSocket = new JLabel("TCP Socket");
        lblTcpSocket.setBounds(12, 386, 86, 14);
        frame.getContentPane().add(lblTcpSocket);
        
        // Create a panel and its contents
        JPanel panelTCP = new JPanel();
        panelTCP.setBounds(0, 411, 434, 85);
        frame.getContentPane().add(panelTCP);
        GridBagLayout gbl_panelTCP = new GridBagLayout();
        gbl_panelTCP.columnWidths = new int[]{184, 65, 155, 0};
        gbl_panelTCP.rowHeights = new int[]{23, 0, 27, 0};
        gbl_panelTCP.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_panelTCP.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelTCP.setLayout(gbl_panelTCP);
        
        // Create a button to create a TCP server
        JButton btnTCPServer = new JButton("Server");
        btnTCPServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Collect necessary information
            	InetAddress lAddress = getInetAddress(txtAddress.getText());
                int lPort = getValue(txtServerPort.getText());
                int lSize = getValue(txtSize.getText());
                int lIteration = getValue(txtIteration.getText());
                
                // Check the information is valid
                if(lAddress == null || lPort == -1 || lIteration == -1 || lSize == -1)
                {
                    return;
                }  // if
                
                // Create a new TCP server according to the inputed information
                mTCPServer = new TCPServerSocketController(lAddress,lPort);
                mTCPServer.establishInputOutput();
                mTCPServer.setIteration(lIteration);
                mTCPServer.setSize(lSize);
                mTCPServer.start();
            }  // void actionPerformed
        });  // btnTCPServer.addActionListener
        // Create the grid to hold the TCP Server Button
        GridBagConstraints gbc_btnTCPServer = new GridBagConstraints();
        gbc_btnTCPServer.insets = new Insets(0, 0, 5, 5);
        gbc_btnTCPServer.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnTCPServer.gridx = 1;
        gbc_btnTCPServer.gridy = 1;
        panelTCP.add(btnTCPServer, gbc_btnTCPServer);
        
        // Create a button to create a TCP client
        JButton btnTCPClient = new JButton("Client");
        btnTCPClient.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                // Collect necessary information
            	InetAddress lAddress = getInetAddress(txtAddress.getText());
                int lPort = getValue(txtServerPort.getText());
                int lSize = getValue(txtSize.getText());
                int lIteration = getValue(txtIteration.getText());
                
                // Check the information is valid
                if(lAddress == null || lPort == -1 || lIteration == -1 || lSize == -1)
                {
                    return;
                }  // if
                
                // Create a new TCP client according to the inputed information
                mTCPClient = new TCPClientSocketController(lAddress, lPort);
                mTCPClient.establishInputOutput();
                mTCPClient.setTAG("Client " + counter);
                mTCPClient.setSize(lSize);
                mTCPClient.setIteration(lIteration);
                mTCPClient.start();
                counter++;
            }  // void actionPerformed
        });  // btnTCPClient.addActionListener
        // Create the grid to hold the TCP Client Button
        GridBagConstraints gbc_btnTCPClient = new GridBagConstraints();
        gbc_btnTCPClient.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnTCPClient.insets = new Insets(0, 0, 0, 5);
        gbc_btnTCPClient.gridx = 1;
        gbc_btnTCPClient.gridy = 2;
        panelTCP.add(btnTCPClient, gbc_btnTCPClient);
        
        JButton btnTCPSend = new JButton("Send");
        btnTCPSend.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent arg0) 
            {
                if(mTCPClient != null)
                {
                    mTCPClient.send();
                }  // if
                else
                {
                    System.out.println("Client do not exist");
                }  // else
            }  // void actionPerformed(
        });  //  btnTCPSend.addActionListener
        // Create the grid to hold the TCP Send Button
        GridBagConstraints gbc_btnTCPSend = new GridBagConstraints();
        gbc_btnTCPSend.anchor = GridBagConstraints.NORTH;
        gbc_btnTCPSend.gridx = 2;
        gbc_btnTCPSend.gridy = 2;
        panelTCP.add(btnTCPSend, gbc_btnTCPSend);
        
        // Create a text field to input the address
        txtAddress = new JTextField();
        txtAddress.setText("localhost");
        txtAddress.setBounds(138, 12, 86, 20);
        frame.getContentPane().add(txtAddress);
        txtAddress.setColumns(10);
        
        // Create a label for the address text field
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(10, 14, 122, 14);
        frame.getContentPane().add(lblAddress);
        
        // Create a label for the port text field
        JLabel lblPort = new JLabel("Port:");
        lblPort.setBounds(242, 14, 58, 14);
        frame.getContentPane().add(lblPort);
        
        // Create a text field to input the port
        txtServerPort = new JTextField();
        txtServerPort.setText("8888");
        txtServerPort.setBounds(321, 11, 86, 20);
        frame.getContentPane().add(txtServerPort);
        txtServerPort.setColumns(10);
        
        // Create a text field to input the size
        txtSize = new JTextField();
        txtSize.setBounds(138, 44, 86, 19);
        frame.getContentPane().add(txtSize);
        txtSize.setColumns(10);
        
        // Create a text field to input the iteration
        txtIteration = new JTextField();
        txtIteration.setBounds(321, 44, 86, 19);
        frame.getContentPane().add(txtIteration);
        txtIteration.setColumns(10);
        
        // Create a label for the size text field
        JLabel lblSize = new JLabel("Size:");
        lblSize.setBounds(10, 40, 70, 15);
        frame.getContentPane().add(lblSize);
        
        // Create a label for the iteration text field
        JLabel lblIteration = new JLabel("Iteration:");
        lblIteration.setBounds(242, 46, 70, 15);
        frame.getContentPane().add(lblIteration);
        
        rdbtnNoDelay = new JRadioButton("No Delay");
        rdbtnNoDelay.setBounds(138, 70, 109, 23);
        frame.getContentPane().add(rdbtnNoDelay);
        
        rdbtnTrafficClassLowCost = new JRadioButton("Traffic Class LOWCOST");
        rdbtnTrafficClassLowCost.setBounds(138, 96, 162, 23);
        frame.getContentPane().add(rdbtnTrafficClassLowCost);
        
        // Create a button to set iteration and size to all available servers and clients
        JButton btnConfirmSettings = new JButton("Confirm Settings");
        btnConfirmSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) 
            {
            	// Collect the information
                int lSize = getValue(txtSize.getText());
                int lIteration = getValue(txtIteration.getText());
                
                boolean lNoDelay = false;
                int lTrafficClass = 0x04;
                
                if(rdbtnNoDelay.isSelected())
                {
                    lNoDelay = true;
                }  // if
                
                if(rdbtnTrafficClassLowCost.isSelected())
                {
                    lTrafficClass = 0x02;
                }  // if
                
                // Check whether the information is valid
                if(lIteration == -1 || lSize == -1)
                {
                    return;
                }  // if
                
                // Update all servers and clients if they are available
                if(mUDPClient != null)
                {
                    System.out.println("Reset for UDP Client");
                    mUDPClient.setIteration(lIteration);
                    mUDPClient.setSize(lSize);
                }  // if
                
                if(mUDPServer != null)
                {
                    System.out.println("Reset for UDP Server");
                    mUDPServer.setIteration(lIteration);
                    mUDPServer.setSize(lSize);
                }  // if
                
                if(mTCPClient != null)
                {
                    System.out.println("Reset for TCP Client");
                    mTCPClient.setIteration(lIteration);
                    mTCPClient.setSize(lSize);
                    mTCPClient.setNoDelay(lNoDelay);
                    mTCPClient.setTraffic(lTrafficClass);
                }  // if
                
                if(mTCPServer != null)
                {
                    System.out.println("Reset for TCP Server");
                    mTCPServer.setIteration(lIteration);
                    mTCPServer.setSize(lSize);
                    mTCPServer.setNoDelay(lNoDelay);
                    mTCPServer.setTraffic(lTrafficClass);
                }  // if                
            }  // void actionPerformed
        });  // btnSetSizeAndIteration.addActionListener
        btnConfirmSettings.setBounds(138, 201, 259, 25);
        frame.getContentPane().add(btnConfirmSettings);
        
        // Create a button to send bytes concurrently through both TCP and UDP
        JButton btnConcurrently = new JButton("UDP TCP Send Concurrently");
        btnConcurrently.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) 
            {
                if((mTCPClient != null) && (mUDPClient != null))
                {
                    mTCPClient.send();
                    mUDPClient.send();
                }  // if
                else
                {
                    System.out.println("Client(s) do not exist");
                }  // else   
            }  // void actionPerformed
        });  // btnConcurrently.addActionListener
        btnConcurrently.setBounds(148, 501, 259, 23);
        frame.getContentPane().add(btnConcurrently);

    }  // void initialize
}  // class ClientFrame
