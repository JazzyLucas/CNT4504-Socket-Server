/**
 * CNT4504: Concurrent Server Socket Project - Client Class
 * <p>
 * This Client class connects to a local machine and uses
 * multithreading to deploy simple Linux tasks.
 * 
 * @author Lucas Rendell, Brittne Cruz, Justin Bentley 
 * @version 11/19/2020
 */


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	public static long[] threadTimeEnd;
	
    public static void main(String[] args) throws IOException {
        try {
            
        	Scanner scan = new Scanner(System.in);
        	String ipString = "139.62.210.153";
        	String portString;
        	int port = 4069;
            
            // Prompt the user for an IP and port
            boolean hasWorkingConnection = false;
            while (!hasWorkingConnection) {
				// Prompt client
				System.out.println("Enter an IP to connect to.");
				System.out.printf("(Leave blank for 139.62.210.153): ");
				ipString = scan.nextLine();
				if (ipString.isEmpty()) {
					ipString = "139.62.210.153";
				}
				System.out.println("Enter a port to connect through.");
				System.out.printf("(Leave blank for 4069): ");
				portString = scan.nextLine();
				if (portString.isEmpty()) {
					port = 4069;
				} else {
					try {
					port = Integer.parseInt(portString);
					} catch (Exception e) {
						System.out.println("Your port encountered an error when parsing, now using default port.");
						port = 4069;
					}
				}
				InetAddress ip;
				try {
					ip = InetAddress.getByName(ipString);
					Socket socket = new Socket(ip, port);
					hasWorkingConnection = true;
					System.out.println("Initial socket connection established!");
					socket.close();
				} catch (UnknownHostException e2) {
					System.out.println("Unknown Host, try again.");
				}catch (Exception e) {
					hasWorkingConnection = false;
					System.out.println("Initial socket connection failed, try again.");
				} 
            }// end while !hasWorkingConnection
            
            
            // Prompt the user for linux actions
            while (true) {
				// Display the prompt client-side
				System.out.println("\nWhat command would you like to perform?");
				System.out.println("1: Date and Time");
				System.out.println("2: Uptime");
				System.out.println("3: Memory Use");
				System.out.println("4: Netstat");
				System.out.println("5: Current Users");
				System.out.println("6: Running Processes");
				System.out.println("7: Quit");
				System.out.printf("Enter a number: ");

				// Loop to make sure the input is valid
				boolean validInput = false;
				boolean exiting = false;
				int command = 0;

				while (!validInput) {
					
					// Scan input
					if (scan.hasNext()) {
						command = scan.nextInt();
					} else {
						validInput = false;
						System.out.printf("Invalid command. Enter a number 1-7: ");
						continue;
					}
					
					// Parse the input
					switch(command) {
						case 1:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "date"
							break;
						case 2:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "uptime"
							break;
						case 3:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "free" (Memory usage)
							break;
						case 4:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "netstat"
							break;
						case 5:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "who" (Current users)
							break;
						case 6:
							validInput = true;
							System.out.println("Command " + command + " accepted.");
					        // Command is set to "psaux" (Running processes)
							break;
						case 7:
							validInput = true;
							exiting = true;
							System.out.println("Adios");
							scan.close();
							break;
						default:
							validInput = false;
							System.out.printf("Invalid command. Enter a number 1-7: ");
							continue;
					}// end switch
					if (exiting)
						break;






					System.out.println("How many requests would you like to generate?");
					System.out.printf("Enter 1, 5, 10, 15, 20, 25, or 100: ");
					boolean validThreadInput = false;
					int arraySize = 0;
					while (!validThreadInput) {
					int requests;
					if (scan.hasNext()) {
						requests = scan.nextInt();
					} else {
						System.out.printf("Invalid input. Enter 1, 5, 10, 15, 20 or 25, or 100: ");
						validThreadInput = false;
						continue;
					}
					switch(requests) {
						case 1:
							validThreadInput = true;
							arraySize = 1;
							break;
						case 5:
							validThreadInput = true;
							arraySize = 5;
							break;
						case 10:
							validThreadInput = true;
							arraySize = 10;
							break;
						case 15:
							validThreadInput = true;
							arraySize = 15;
							break;
						case 20:
							validThreadInput = true;
							arraySize = 20;
							break;
						case 25:
							validThreadInput = true;
							arraySize = 25;
							break;
                        case 100:
							validThreadInput = true;
							arraySize = 100;
							break;
						default:
							System.out.printf("Invalid number. Enter 1, 5, 10, 15, 20, 25, or 100: ");
							validThreadInput = false;
							continue;
						}// end Switch
					}// end while
					
                    CommandThread initialCommand = new CommandThread(arraySize + 100, 0, port, ipString);
                    initialCommand.runRequests();

					// Create a thread array, respective timers, and booleans for threads.
					CommandThread[] threadArray = new CommandThread[arraySize];
					long totalTime = 0;
					long[] threadTimeStart = new long[arraySize];
					threadTimeEnd = new long[arraySize];

					// Start each thread
					for (int i = 0; i < arraySize; i++) {
						threadArray[i] = new CommandThread(command, i, port, ipString);
						threadArray[i].start();
						threadTimeStart[i] = System.currentTimeMillis();
					}// end for starting each thread

					// Wait for each thread to die
					for (int i = 0; i < arraySize; i++) {
						while (threadArray[i].isAlive()) {
						}
					}// end for waiting for each thread to die

					// Gather the response times (and display each one)
					for (int i = 0; i < arraySize; i++) {
						long time = threadTimeEnd[i] - threadTimeStart[i];
						System.out.println("Thread " + (i+1) + " response time: " + time + " ms");
						totalTime += time;
					}// end for gathering response times
					
					// Print out total response times and average
					System.out.println("Total response time: " + totalTime + " ms");
					long averageTime = totalTime / arraySize;
					System.out.println("Average response time: " + averageTime + " ms");

				}// end while (!validInput)

			if(exiting) {
				break;
			}

			}// end while
            
        } catch(Exception e) {
            e.printStackTrace();
        }// end try/catch
    }
    
}// end Client class

class CommandThread extends Thread {
	String ipString = "139.62.210.153";
	int port = 4069;
	int command;
	Socket socket;
	private long timeFinished = 0;
	private int index;

	CommandThread (int command, int index, int port, String ip) {
		this.command = command;
		this.index = index;
		this.port = port;
		this.ipString = ip;
	}

	public void runRequests() {

		// Store the server IP
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(ipString);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}

		// Establish a socket connection over the ip and 4069 port
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

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
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			dataOutput.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Close the socket and inputs/outputs.
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dataInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dataOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {

		// Store the server IP
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(ipString);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}

		// Establish a socket connection over the ip and 4069 port
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

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
		try {
			dataOutput.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String received = dataInput.readUTF();
			System.out.println(received);
			timeFinished = System.currentTimeMillis();
			Client.threadTimeEnd[index] = timeFinished;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Close the socket and inputs/outputs.
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dataInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dataOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// end run()
}// end CommandThread class extending Thread