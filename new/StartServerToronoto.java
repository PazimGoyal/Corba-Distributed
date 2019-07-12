import EventManagementSystem.*;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Naming;
import java.util.Properties;
 
public class StartServerToronoto {
    static ImplimentationToronto exportedObj;

 
  public static void main(String args[]) {
    try{
      // create and initialize the ORB //// get reference to rootpoa &amp; activate the POAManager
      ORB orb = ORB.init(args, null);      
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();
 
      // create servant and register it with the ORB
      ImplimentationToronto addobj = new ImplimentationToronto();
      addobj.setORB(orb); 
 
      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(addobj);
      Eventmanagement href = EventmanagementHelper.narrow(ref);
 
      org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
      NameComponent path[] = ncRef.to_name( "TOR" );
      ncRef.rebind(path, href);
 
      System.out.println("Addition Server ready and waiting ...");
      exportedObj = new ImplimentationToronto();
      startUDPServer(8086);

      
      // wait for invocations from clients
      for (;;){
	  orb.run();
      }
    

    
    } 
 
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
 
      System.out.println("HelloServer Exiting ...");
 
  }
  
  private static void startUDPServer(int portNumber) {
      DatagramSocket aSocket = null;
      String val = "";
      try {
          aSocket = new DatagramSocket(portNumber);
          byte[] buffer = new byte[100000];// to stored the received data from
          // the client.
          System.out.println("Toronto UDP Server Started on 8086............");
          while (true) {
              buffer=new byte[100000];
              DatagramPacket request = new DatagramPacket(buffer, buffer.length);

              aSocket.receive(request);

              System.out.println("Request received from client: " + new String(request.getData()));
              String valuePassed = new String(request.getData());
              String[] parameterToBePassed = valuePassed.split(":");
              if (parameterToBePassed[0].equals("bookEvent")) {
                  val = exportedObj.bookEvent(parameterToBePassed[1].trim(), parameterToBePassed[2].trim(), parameterToBePassed[3].trim());
              } else if (parameterToBePassed[0].equals("listEventAvailability")) {
                  val = exportedObj.listEventAvailabilityServerCall(parameterToBePassed[1].trim());
              } else if (parameterToBePassed[0].equals("getBookingSchedule")) {
                  val = exportedObj.getBookingScheduleServerCall(parameterToBePassed[1].trim());
              } else if (parameterToBePassed[0].equals("cancelEvent")) {
                  val = exportedObj.cancelEvent(parameterToBePassed[1].trim(), parameterToBePassed[2].trim(), parameterToBePassed[3].trim());
              }

              DatagramPacket reply = new DatagramPacket(val.getBytes(), val.length(), request.getAddress(),
                      request.getPort());// reply packet ready

              aSocket.send(reply);// reply sent
          }
      } catch (SocketException e) {
          System.out.println("Socket: " + e.getMessage());
      } catch (IOException e) {
          System.out.println("IO: " + e.getMessage());
      } finally {
          if (aSocket != null)
              aSocket.close();
      }
  }

}