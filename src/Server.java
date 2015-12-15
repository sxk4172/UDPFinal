import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Server {

	public static void main(String args[]) throws IOException {
		
		  InetAddress ip = InetAddress.getByName("localhost"); 
		  DatagramSocket ds = new DatagramSocket(8000);
		  DatagramSocket client_socket = new DatagramSocket(); 
		  byte[] data = new byte[1024]; 
		  while (true) {
		  DatagramPacket dp = new DatagramPacket(data, data.length);
		  ds.receive(dp); 
		  byte[] data1 = dp.getData(); 
		  String value = new String(data1, 0, data1.length);
		  System.out.println(value);
		  String ack = "ACK"; byte[] send_data = ack.getBytes();
		  
		  DatagramPacket send_packet = new DatagramPacket(send_data,
		  send_data.length, ip, 5000);
		  
		  client_socket.send(send_packet);
		  
		  break; 
		  }
		  client_socket.close();
		  ds.close();
	}

/*	public static void main(String args[]) throws IOException {
		String receiveFile = args[0];
		int listening_port = Integer.parseInt(args[1]);
		FileOutputStream filename = new FileOutputStream(receiveFile);
		DatagramSocket getSocket = new DatagramSocket(listening_port);
		byte[] buf = new byte[40012];
		byte[] data= new byte[40000];
		byte[] senderPort = new byte[4];
		byte[] destinationPot = new byte[4];
		byte[] seq_num = new byte[4];
		while(data.length + 12 == buf.length) {
			System.out.println("Receiving packets");
			DatagramPacket getPacket = new DatagramPacket(buf, buf.length);
			getSocket.receive(getPacket);
			byte[] packet = getPacket.getData();
			data = new byte[(getPacket.getLength()) - 12];
			System.arraycopy(packet, 12, data, 0, data.length);
			filename.write(data, 0, data.length);
			System.arraycopy(packet, 0, senderPort, 0, 4);
			System.arraycopy(packet, 4, destinationPot, 0, 4);
			System.arraycopy(packet, 8, seq_num, 0, 4);
			final ByteBuffer bb = ByteBuffer.wrap(senderPort);
		    bb.order(ByteOrder.BIG_ENDIAN);
			int senderP = bb.getInt();
			final ByteBuffer bb1 = ByteBuffer.wrap(destinationPot);
		    bb1.order(ByteOrder.BIG_ENDIAN);
			int destinationP = bb1.getInt();
			final ByteBuffer bb2 = ByteBuffer.wrap(seq_num);
		    bb2.order(ByteOrder.BIG_ENDIAN);
			int seqNum = bb2.getInt();
			System.out.println(data.length);
			System.out.println(senderP + " " + destinationP + " " + seqNum);

		}
		
		System.out.println("Delivered Successfully!!!");
		getSocket.close();
		filename.close();

	}
*/}
