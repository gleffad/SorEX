package architecture;

import blockchain.Transaction;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SorexClient {
	private final static int serverPort = 3025;

	private static String serverIP;

	private static String clientIP;

	private static double balance = 1.0;

	private static List<String> notifications = new ArrayList<>();

	private static Socket clientSocket;

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("Server IP : ");
		serverIP = sc.nextLine();

		// Starting Threads
		clientSocket = connect(serverIP, serverPort);
		transactionReceiver(clientSocket);

		// Main Menu
		String choice;
		do {
			System.out.println("Welcome to SorEx Client.");
			System.out.println("[+] Main menu");
			System.out.println("\t[1] Check balance");
			System.out.println("\t[2] Check notifications");
			System.out.println("\t[3] Send money");
			System.out.println("\t[0] Exit");
			System.out.print("\n> ");

			choice = sc.nextLine();

			switch (choice) {
				case "1":
					checkBalance();
					break;

				case "2":
					showNotifications();
					break;

				case "3":
					sendMoney(clientSocket);
					break;

				case "0":
					System.out.println("See ya !");
					clientSocket.close();
					System.exit(0);
			}
		} while (!choice.equals("0"));
	}

	public static Socket connect(String serverIP, int serverPort) {
		Socket socket = null;
		try {
			clientIP = InetAddress.getLocalHost().getHostAddress();
			socket = new Socket(serverIP, serverPort);
			System.out.println("[+] Connected to server.");
		} catch (Exception e) {
			System.err.println("Connection Error.");
			System.exit(-1);
		}
		return socket;
	}

	public static void checkBalance() {
		System.out.println("Your Balance is " + getBalance());
	}

	public static void sendMoney(Socket socket) throws IOException {
		System.out.print("\n");
		boolean valid = false;
		Scanner sc = new Scanner(System.in);
		String destAddress;
		double amount = 0;
		System.out.print("> Destination IP address : ");
		destAddress = sc.nextLine();
		System.out.print("> Amount you want to send : ");
		do {
			try {
				amount = sc.nextDouble();
				valid = false;
			} catch (Exception e) {
				valid = true;
			}
		} while (valid);

		if (amount > getBalance()) {
			System.out.println("Insufficient balance.");
		} else if (amount <= 0) {
			System.out.println("Invalid amount.");
		} else {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // toServer

			Transaction transaction = new Transaction(getIP(), destAddress, amount);
			oos.writeObject(transaction);
		}
	}

	public static void transactionReceiver(Socket socket) {
		Runnable task = () -> {
			while (true) {
				Transaction transaction = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					transaction = (Transaction) ois.readObject();
					System.out.print("\n");

					// Receiving ACK/ERR
					if (transaction.getSenderIP().equals(getIP())) {
						System.out.println("[+] Received response from server : " + transaction.getControlValue());
						if (transaction.getControlValue().equals("ACK")) {
							System.out.println("[+] Sent successfully !");
							addNotification("[+] " + getDateTime() + " | Sent " + transaction.getValue() + " SorEx to " + transaction.getReceiverIP());
							setBalance(getBalance() - transaction.getValue());
						} else {
							System.out.println("[-] Error while sending money : Unknown host.");
							addNotification("[+] " + getDateTime() + " | Failed to send " + transaction.getValue() + " SorEx to " + transaction.getReceiverIP());
						}
					} else {    // Receiving Money
						setBalance(getBalance() + transaction.getValue());
						System.out.println("[+] Received " + transaction.getValue() + " SorEx from " + transaction.getSenderIP());
						addNotification("[+] " + getDateTime() + " | Received " + transaction.getValue() + " SorEx from " + transaction.getSenderIP());
					}
				} catch (Exception e) {
					System.out.println("Disconnected. Exiting.");
					exit();
				}
				System.out.print("> ");
			}
		};
		new Thread(task).start();
	}

	public static void showNotifications() {
		System.out.println("[~] You have " + getNotifications().size() + " notifications :");
		for (String notification : getNotifications()) {
			System.out.println("\t" + notification);
		}
		System.out.print("\n");
	}


	public static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		return dateFormat.format(date);
	}


	public static String getIP() {
		return clientIP;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static String getServerIP() {
		return serverIP;
	}

	public static void setServerIP(String serverIP) {
		SorexClient.serverIP = serverIP;
	}

	public static double getBalance() {
		return balance;
	}

	public static void setBalance(double balance) {
		SorexClient.balance = balance;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public static void addNotification(String message) {
		getNotifications().add(message);
	}

	public static String getClientIP() {
		return clientIP;
	}

	public static void setClientIP(String clientIP) {
		SorexClient.clientIP = clientIP;
	}

	public static List<String> getNotifications() {
		return notifications;
	}

	public static void exit() {
		try {
			clientSocket.close();
		} catch (IOException ex) {
		}
		System.exit(0);
	}
}
