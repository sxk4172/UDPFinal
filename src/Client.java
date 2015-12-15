import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
	static ArrayList<byte[]> packets = new ArrayList<byte[]>();
	String file;
	int senderPort;
	int destinationPort;
	byte[] destinationP;
	byte[] senderP;
	byte[] udpPacketNumber;

	public Client(String file, String senderPort, String destinationPort) {
		this.file = file;
		this.senderPort = Integer.parseInt(senderPort);
		this.destinationPort = Integer.parseInt(destinationPort);
		destinationP = ByteBuffer.allocate(4).putInt(this.destinationPort)
				.array();
		senderP = ByteBuffer.allocate(4).putInt(this.senderPort).array();
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		
		InetAddress ip = InetAddress.getByName("localhost");
		Client client = new Client(args[0], args[1], args[2]);
		Path file = Paths.get(client.file);
		byte[] fileIntoBytes = Files.readAllBytes(file);
		CreatingConnection();
/*		creatingPackets(fileIntoBytes, client.destinationPort,
				client.senderPort, client);
		DatagramSocket sender_socket = new DatagramSocket();
		for (int i = 0; i < packets.size(); i++) {
			System.out.println(packets.get(i));
			DatagramPacket sendPacket = new DatagramPacket(packets.get(i),
					packets.get(i).length, ip, client.destinationPort);
			sender_socket.send(sendPacket);
			Thread.sleep(1000);
		}
		sender_socket.close();*/
	}

	private static void CreatingConnection() throws IOException {
		byte[] receive_data = new byte[1024];
		InetAddress ip = InetAddress.getByName("localhost");
		String syn = "SYNC";
		byte[] synBytes = syn.getBytes();
		DatagramSocket ds = new DatagramSocket();
		DatagramSocket server_socket = new DatagramSocket(5000);
		while (true) {
			DatagramPacket dp = new DatagramPacket(synBytes, synBytes.length,
					ip, 8000);
			ds.send(dp);
			DatagramPacket receive_packet = new DatagramPacket(receive_data,
					receive_data.length);

			server_socket.receive(receive_packet);

			byte[] data3 = receive_packet.getData();

			String data1 = new String(data3, 0, data3.length);
			System.out.println(data1);

			break;
		}

		server_socket.close();
		ds.close();
	}

	private static void creatingPackets(byte[] fileIntoBytes,
			int destinationPort, int senderPort, Client client){
		int temp = 0, sum = 40000;
		int udpPackets = 0;
		while (true) {
			byte data[] = new byte[40012];
			
			ByteBuffer buf = ByteBuffer.wrap(data);
			
			buf.put(Arrays
					.copyOfRange(client.senderP, 0, client.senderP.length));// sender
																			// port
			buf.put(Arrays.copyOfRange(client.destinationP, 0,
					client.destinationP.length));// destination port
			
			if (sum < fileIntoBytes.length) {
				client.udpPacketNumber = ByteBuffer.allocate(4)
						.putInt(udpPackets).array();
				buf.put(client.udpPacketNumber); //packet number
				
				buf.put(Arrays.copyOfRange(fileIntoBytes, temp, sum)); // file
				temp = sum;
				System.out.println("Temp : " + temp);
				sum = sum + 40000;
				System.out.println("Sum :" + sum);
				udpPackets = udpPackets + 1;
				System.out.println("count " +udpPackets);
				packets.add(data);

			}
			if (fileIntoBytes.length < sum && fileIntoBytes.length > temp) {
				byte data1[] = new byte[((fileIntoBytes.length - temp)+ 12 )];
				ByteBuffer buf1 = ByteBuffer.wrap(data1);
				buf1.put(Arrays
						.copyOfRange(client.senderP, 0, client.senderP.length));// sender
																				// port
				
				buf1.put(Arrays.copyOfRange(client.destinationP, 0,
						client.destinationP.length));// destination port

				
				client.udpPacketNumber = ByteBuffer.allocate(4)
						.putInt(udpPackets).array(); 
				buf1.put(client.udpPacketNumber);//packet number
				
				System.out.println("Bytes remaining  : "
						+ (fileIntoBytes.length - temp));
				buf1.put(Arrays.copyOfRange(fileIntoBytes, temp,
						fileIntoBytes.length)); //file
				packets.add(data1);
				udpPackets = udpPackets + 1;
				System.out.println("Number of packets :" + udpPackets);
				break;
			}

		}
		
	}
}
