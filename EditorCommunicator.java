import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			String line;
			while((line = in.readLine() ) != null){
				System.out.println("Received " + line);
				String[] allword = line.split(" ");

				//recoloring
				if(allword[0].equals("RC")){
					editor.getSketch().setShapeColor(Integer.parseInt(allword[1]),Integer.parseInt(allword[2]));

				}

				//deleting
				if(allword[0].equals("DL")){
					int key = Integer.parseInt(allword[1]);
					editor.getSketch().removeShape(key);

				}

				//moving
				if(allword[0].equals("MS")){
					int key = Integer.parseInt(allword[1]);
					int dx = Integer.parseInt(allword[2]);
					int dy = Integer.parseInt(allword[3]);
					editor.getSketch().moveShape(key,dx,dy);

				}

				//adding shape
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

						editor.getSketch().addShape(shape);
					}
					if(key.equals("rectangle")){
						Shape shape = new Rectangle(x1,y1,x2,y2,c);
						editor.getSketch().addShape(shape);

					}
					if(key.equals("segment")){
						Shape shape = new Segment(x1,y1,x2,y2,c);
						editor.getSketch().addShape(shape);


					}


				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}	

	//Method for composing a message for recoloring
	public void sendRecolor(Integer key, Color c){
		String msg = "RC " + key +" "+c.getRGB();
		System.out.println("Sending " + msg);
		send(msg);
	}

	//Method for composing a message for deleting

	public void sendDeleteShape(Integer key){
		String msg = "DL "+ key;
		System.out.println("Sending " + msg);
		send(msg);
	}

	//Method for composing a message for moving

	public void sendMoveShape (Integer key, int dx, int dy){
		String msg = "MS "+key+" "+ dx+" "+dy;
		System.out.println("Sending " + msg);
		send(msg);
	}
	//Method for composing a message for adding

	public void sendAddShape(Shape s){
		String msg = "AS " + s.toString();
		System.out.println("Sending " + msg);
		send(msg);
	}
	// Send editor requests to the server
	// TODO: YOUR CODE HERE
	
}
