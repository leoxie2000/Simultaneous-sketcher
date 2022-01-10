import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
		private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			Map<Integer, Shape> shapes = server.getSketch().getIDShapeMap();
			for(Integer key: shapes.keySet())
			{
				Shape s = shapes.get(key);
				String msg = "AS " + s.toString();
				out.println(msg);
			}

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			// Handle messages
			String line;
			while((line = in.readLine() ) != null){
				System.out.println("Received " + line);
				String[] allword = line.split(" ");

				//recoloring mode
				if(allword[0].equals("RC")){
					server.getSketch().setShapeColor(Integer.parseInt(allword[1]),Integer.parseInt(allword[2]));

				}

				//deleting mode
				if(allword[0].equals("DL")){
					int key = Integer.parseInt(allword[1]);
					server.getSketch().removeShape(key);

				}

				//moving mode
				if(allword[0].equals("MS")){
					int key = Integer.parseInt(allword[1]);
					int dx = Integer.parseInt(allword[2]);
					int dy = Integer.parseInt(allword[3]);
					server.getSketch().moveShape(key,dx,dy);

				}


				//adding shape mode
				if(allword[0].equals("AS")){
					String key = allword[1];
					int x1 = Integer.parseInt(allword[2]);
					int y1 = Integer.parseInt(allword[3]);
					int x2 = Integer.parseInt(allword[4]);
					int y2 = Integer.parseInt(allword[5]);
					Color c = new Color(Integer.parseInt(allword[6]));


					//adding based on type
					if(key.equals("ellipse")){
						Shape shape = new Ellipse(x1,y1,x2,y2,c);

						server.getSketch().addShape(shape);
					}
					if(key.equals("rectangle")){
						Shape shape = new Rectangle(x1,y1,x2,y2,c);
						server.getSketch().addShape(shape);

					}
					if(key.equals("segment")){
						Shape shape = new Segment(x1,y1,x2,y2,c);
						server.getSketch().addShape(shape);


					}


				}

				//broadcast to editor communicator
				server.broadcast(line);
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
