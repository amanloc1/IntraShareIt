import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
	public static void main(String args[]) throws Exception {
		// if at least two argument are passed, consider the first one as directory path
		// and the second one as port number
		// If port number is not present, default it to 3333
		// If directory path is not present, show error
		if(args.length == 0) {
			System.out.println("Please enter the server directory address as first argument while running from command line.");
		}
		else {
			int id = 1;
			System.out.println("Server started...");
			System.out.println("Waiting for connections...");

			ServerSocket welcomeSocket;

			// port number is passed by the user
			if(args.length >= 2){
				welcomeSocket = new ServerSocket(Integer.parseInt(args[1]));
			}
			else{
				welcomeSocket = new ServerSocket(3333);
			}

			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Client with ID " + id + " connected from " + connectionSocket.getInetAddress().getHostName() + "...");
				Thread server = new ThreadedServer(connectionSocket, id, args[0]);
				id++;
				server.start();
			}
		}
	}
}

class ThreadedServer extends Thread {
	int n;
	int m;
	String name, f, ch, fileData;
	String filename;
	Socket connectionSocket;
	int counter;
	String dirName;

	public ThreadedServer(Socket s, int c, String dir) {
		connectionSocket = s;
		counter = c;

		// set dirName to the one that's entered by the user
		dirName = dir;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			InputStream inFromClient = connectionSocket.getInputStream();
			PrintWriter outPw = new PrintWriter(connectionSocket.getOutputStream());
			OutputStream output = connectionSocket.getOutputStream();

			ObjectOutputStream oout = new ObjectOutputStream(output);
			oout.writeObject("Server says Hi!");

			File ff = new File(dirName);
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(ff.list()));
			int len = names.size();
			oout.writeObject(String.valueOf(names.size()));
			for(String name: names) {
				oout.writeObject(name);
			}
                        DataInputStream oin=new DataInputStream(inFromClient);//added input from client
                        DataOutputStream dout = new DataOutputStream(output);
                        while(true){//added
			name = in.readLine();
			ch = name.substring(0, 1);
			if (ch.equals("*")) {
				n = name.lastIndexOf("*");
				filename = name.substring(1, n);
				FileInputStream file = null;
				BufferedInputStream bis = null;
				boolean fileExists = true;
				System.out.println("Request to download file " + filename + " recieved from " + connectionSocket.getInetAddress().getHostName() + "...");
				filename = dirName + filename;File temp1=null;//added
				//System.out.println(filename);
				try {
                                        temp1=new File(filename);//added file object
					file = new FileInputStream(temp1);
					bis = new BufferedInputStream(file);
				} 
				catch (FileNotFoundException excep) {
					fileExists = false;
					System.out.println("FileNotFoundException:" + excep.getMessage());
				}
				if (fileExists) {
					dout.writeUTF("Success "+temp1.length());//send file size with success
					System.out.println("Download begins");
					sendBytes(bis, output);
					System.out.println("Completed");
					bis.close();
					file.close();
					//oout.flush();
					//output.close();
				}
				else {
					dout.writeUTF("Failure 1234");
                                        System.out.println("stop");
					//bis.close();
					//file.close();
					//oout.flush();
					//output.close();
				}
                               dout.flush();
			} 
			else{
				try {
					boolean complete = true;
					System.out.println("Request to upload file " + name + " recieved from " + connectionSocket.getInetAddress().getHostName() + "...");
					File directory = new File(dirName);
					if (!directory.exists()) {
						System.out.println("Dir made");
						directory.mkdir();
					}

					//int size = 9022386;
					byte[] data = new byte[8192];
                                        String s=oin.readUTF();//added 
                                        long l=Long.parseLong(s.split(" ")[1]);s=s.split(" ")[0];//added to get file size
                                        if(s.equals("Success"))
                                        {
                                            File fc = new File(dirName+name);
                                            FileOutputStream fileOut = new FileOutputStream(fc);
                                            DataOutputStream dataOut = new DataOutputStream(fileOut);
                                            //while (complete) {
                                            while(l>0)
                                            {
                                               m = inFromClient.read(data);
                                               l-=m;
                                                    //if (m == -1) {
                                                            //complete = false;
                                                            //System.out.println("Completed");
                                                            //m=0;
                                                    //} else {
                                                dataOut.write(data, 0, m);
                                                            //dataOut.flush();
                                            }
                                            System.out.println("Completed");
                                            //}
                                            //}
                                            fileOut.close();
                                            dataOut.close();//added
                                            //oin.reset();
                                }
				} catch (Exception exc) {
					System.out.println(exc.getMessage());
				}
                                
                        }
                        }
                }
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
        


	private static void sendBytes(BufferedInputStream in , OutputStream out) throws Exception 
        {
		//int size = 9022386;
		byte[] data = new byte[8192];
                int c=0;
                while ((c=in.read(data))>0)
                   out.write(data,0,c);
                
	}
}
