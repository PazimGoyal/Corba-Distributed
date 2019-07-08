package AssignmentDistri;
/*
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Hashtable;

public class ServerImplementationToronto extends UnicastRemoteObject implements ManagerInterface {
    public static HashMap<String, HashMap<String, Integer>> hashMap = new HashMap<>();

    public ServerImplementationToronto() throws RemoteException {

        super();

        hashMap.put("CONFRENCE", new HashMap<>());
        hashMap.put("TRADE SHOW", new HashMap<>());
        hashMap.put("SEMINAR", new HashMap<>());
        hashMap.put("CONFRENCE", new HashMap<>());
    }


    public boolean bookEvent(String customerID, String eventID, String eventType) throws RemoteException {
        return false;
    }

    public String removeEvent(String eventID, String eventType) throws RemoteException {
        HashMap<String, Integer> temp;
        String reply = "";

        boolean exists = hashMap.containsKey(eventType);
        if (exists) {
            temp = hashMap.get(eventType);
            if (temp.containsKey(eventID)) {
                temp.remove(eventID);
                reply = "EVENT ID REMOVED SUCCESSFULLY";
            } else {
                reply = "No SUCH EVENT ID FOUND";
            }

        } else {
            reply = "NO SUCH EVENT TYPE FOUND";
        }
        return reply;


    }

    public void listEventAvailability(String eventType) throws RemoteException {

    }

    public void addEvent(String eventID, String eventType, int bookingCapacity) throws RemoteException {

        */
/*TODO : Check the EventID type and then call UDP shit*//*

//		String[] vals = split(eventID);
        String[] EventIdArray = (eventID.split("(?<=\\G...)"));
        String EventCityCode = EventIdArray[0];
        if (EventCityCode.equals("TOR")) {
            boolean exists = hashMap.containsKey(eventType);
            HashMap<String, Integer> temp;
            if (exists) {
                temp = hashMap.get(eventType);
                if (temp.containsKey(eventID)) {
                    temp.remove(eventID);
                    temp.put(eventID, bookingCapacity);
                } else {
                    temp.put(eventID, bookingCapacity);

                }

            } else {

                temp = new HashMap<>();
                temp.put(eventID, bookingCapacity);
                hashMap.put(eventType, temp);
            }
        }else{
            if(EventCityCode.equals("MTL")){
                sendEventToCorrectServer("this is message", 8084);
            }else{
//                TODO : OTTAWA
            }
        }


    }

    @Override
    public HashMap<String, HashMap<String, Integer>> getHashMap() throws RemoteException {
        System.out.println(hashMap);
        return hashMap;
    }

    public static void sendEventToCorrectServer(String rawMessage, int remotePortNumber){
        DatagramSocket aSocket = null;
        try{
            System.out.println("Toronto UDP CLIENT.Client Started........");
            aSocket = new DatagramSocket();
            byte [] message = "Hello".getBytes(); //message to be passed is stored in byte array
            InetAddress aHost = InetAddress.getByName("localhost"); //Host name is specified and the IP address of server host is calculated using DNS.
            int serverPort = remotePortNumber;//agreed upon port
//            int serverPort = 8084;//agreed upon port
            DatagramPacket request = new DatagramPacket(message, "Hello".length(), aHost, serverPort);//request packet ready
            aSocket.send(request);//request sent out
            System.out.println("Request message sent from the client is : "+ new String(request.getData()));

            byte [] buffer = new byte[1000];//to store the received data, it will be populated by what receive method returns
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);//reply packet ready but not populated.

            //CLIENT.Client waits until the reply is received-----------------------------------------------------------------------
            aSocket.receive(reply);//reply received and will populate reply packet now.
            System.out.println("Reply received from the server is: "+ new String(reply.getData()));//print reply message after converting it to a string from bytes
        }
        catch(SocketException e){
            System.out.println("Socket: "+e.getMessage());
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("IO: "+e.getMessage());
        }
        finally{
            if(aSocket != null) aSocket.close();//now all resources used by the socket are returned to the OS, so that there is no
            //resource leakage, therefore, close the socket after it's use is completed to release resources.
        }
    }


}*/
