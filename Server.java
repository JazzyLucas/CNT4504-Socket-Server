/**
 * CNT4504: Server Server Socket Project - Server Class
 * <p>
 * This Server class looks for active clients, accepts the connection 
 * and deploys simple Linux tasks in a multithreaded manner.
 *
 * @author Lucas Rendell, Brittne Cruz, Justin Bentley 
 * @version 11/19/2020
 */

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{
	static int threadSize = 0;
	static boolean isWaiting = false;
	
    public static void main(String[] args) throws IOException
    {
    	Scanner scan = new Scanner(System.in);
    	String portString = "";
    	int port = 4069;
    	
    	// Prompt the user for a port to listen on
        boolean hasPort = false;
        while (!hasPort) {
			// Prompt client
			System.out.println("Enter a port to connect through.");
			System.out.printf("(Leave blank for 4069): ");
			portString = scan.nextLine();
			if (portString.isEmpty()) {
				port = 4069;
				hasPort = true;
			} else {
				try {
				port = Integer.parseInt(portString);
				hasPort = true;
				} catch (Exception e) {
					System.out.println("Your port encountered an error when parsing.");
					hasPort = false;
				}
			}
        }// end while !hasWorkingConnection
		System.out.println("Creating a socket on port " + port + "...");
        
        // Create a server socket on the port
        ServerSocket ss = new ServerSocket(port);
        
		System.out.println("Awaiting connections!");
        
        // loop for listening and accepting client requests
        while (true)
        {
            Socket s = null;
            if (threadSize == 0 && !isWaiting) {
				try {
					// Open socket and accept requests
					s = ss.accept();

					System.out.println("A socket/input was accepted.");

					// Open input and output streams
					DataInputStream dataInput = new DataInputStream(s.getInputStream());
					int received = dataInput.read();
					if (received > 100) {
	                	System.out.println("A thread request size of " + (received - 100) + " was received.");
						threadSize = (received - 100);
						isWaiting = true;
					}
				}
				catch (Exception e) {
					s.close();
					ss.close();
					e.printStackTrace();
					break;
				} 
			} else {
				if (threadSize > 0) {
					int temp = threadSize;
					threadSize = 0;
					Thread[] threads = new Thread[temp];
                	System.out.println("Starting " + temp + " threads.");
					for (int i = 0; i < temp; i++) {
						threads[i] = new ClientHandler(port, ss);
						threads[i].start();
					}
					// Wait for each thread to die
					for (int i = 0; i < temp; i++) {
						while (threads[i].isAlive()) {
						}
					}// end for waiting for each thread to die
					System.out.println("All threads succeeded.");
					isWaiting = false;
				}
			}
        }
        scan.close();
}

// ClientHandler class
static class ClientHandler extends Thread
{
	int port = 4069;
	Socket socket = null;

    // Constructor
    public ClientHandler(int port, ServerSocket ss)
    {
    	this.port = port;
    	try {
			socket = ss.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void run()
    {
        int received;
        String commandResult;
        Process process = null;
        BufferedReader stdInput = null;

 		// Store input and output streams for the socket
 		DataInputStream dataInput = null;
 		try {
 			dataInput = new DataInputStream(socket.getInputStream());
 		} catch (IOException e2) {
 			e2.printStackTrace();
 		}
         DataOutputStream dataOutput = null;
 		try {
 			dataOutput = new DataOutputStream(socket.getOutputStream());
 		} catch (IOException e2) {
 			e2.printStackTrace();
 		}
 		try {
 			dataOutput = new DataOutputStream(socket.getOutputStream());
 			dataInput = new DataInputStream(socket.getInputStream());
 		} catch (IOException e1) {
 			e1.printStackTrace();
 		}
        
        /* Getting rid of this while loop along with the break
           statement still works, and doesn't loop the error.*/
        while (true)
        {
            try {
                received = dataInput.read();
                commandResult = "";
                // Write resulting output from command to output stream
                switch (received) {
                
                    case 1 :
                        // Command selected: date
                        process = Runtime.getRuntime().exec("date");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;
                        
                    case 2 :
                        // Command selected: uptime
                        process = Runtime.getRuntime().exec("uptime");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;

                    case 3 :
                        // Command selected: free
                        process = Runtime.getRuntime().exec("free");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;

                    case 4 :
                        // Command selected: netstat
                        process = Runtime.getRuntime().exec("netstat");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;

                    case 5 :
                        // Command selected: who
                        process = Runtime.getRuntime().exec("who");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;
                        
                    case 6 :
                        // Command selected: psaux
                        process = Runtime.getRuntime().exec("ps aux");
                        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        for (String line; (line = stdInput.readLine()) != null; commandResult += line){
                                commandResult += "\n";
                        }
                        dataOutput.writeUTF(commandResult);
                        break;

                    default:
                        dataOutput.writeUTF("Invalid input");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
				// ^ Commented this out because it's annoying lol
                break;
            }
    }
}
}
}
