import AdditionApp.AdditionPOA;
import org.omg.CORBA.ORB;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
 
class ImplementationToronto extends AdditionPOA {
  private ORB orb;
  public static HashMap<String, HashMap<String, Integer>> hashMap = new HashMap<>();
  public static HashMap<String, HashSet<String>> customerBooking = new HashMap<>();
  static String name = "TOR";
  FileWriter fileWriter;

  public void setORB(ORB orb_val) {
    orb = orb_val; 
  }
 
  // implement add() method
  public int add(int a, int b) {
    int r=a+b;
    return r;
    }
 
  // implement shutdown() method
  public void shutdown() {
    orb.shutdown(false);
  }
  

	

  public ImplementationToronto()  throws Exception{

      hashMap.put("CONFRENCE", new HashMap<>());
      hashMap.put("TRADE SHOW", new HashMap<>());
      hashMap.put("SEMINAR", new HashMap<>());
      hashMap.put("CONFRENCE", new HashMap<>());
      hashMap.get("CONFRENCE").put("TORA123412", 5);
      hashMap.get("CONFRENCE").put("TORA123412", 5);
      hashMap.get("SEMINAR").put("TORE999999", 5);
      hashMap.get("SEMINAR").put("TORA060619", 2);
      hashMap.get("TRADE SHOW").put("TORE989898", 5);
      hashMap.get("TRADE SHOW").put("TORM121219", 5);
      customerBooking.put("TORC1234", new HashSet<>());
      customerBooking.get("TORC1234").add("SEMINAR||TORA123412");
  }


  public String cancelEvent(String customerID, String eventID, String eventType)  {
      String reply = "";
      String[] EventIdArray = (eventID.split("(?<=\\G...)"));
      String EventCityCode = EventIdArray[0];
      if (EventCityCode.equals("TOR")) {
          if (customerBooking.containsKey(customerID)) {
              HashSet<String> bookingHash = customerBooking.get(customerID);
              if (bookingHash.contains(eventType + "||" + eventID)) {
                  bookingHash.remove(eventType + "||" + eventID);
                  HashMap<String, Integer> book = hashMap.get(eventType);
                  int a = book.get(eventID);
                  book.put(eventID, a + 1);
                  hashMap.put(eventType, book);
                  reply = "Event Canceled Successfully";
                  LogData("Event Cancelled Successfully : Event :" + eventID + " for Event type " + eventType + " ,Customer " + customerID + " " + "\n");

              } else {
                  reply = "No Booking For Such Customer Found";
                  LogData("Event Cancelled : Event :" + eventID + " for Event type " + eventType + " ,Customer " + customerID + " not successful as Customer not found " + "\n");

              }
          } else {
              reply = "NO SUCH CUSTOMER FOUND FOR EVENT";
              LogData("Event Cancelled : NO SUCH CUSTOMER FOUND FOR EVENT :Event :" + eventID + " for Event type " + eventType + " ,Customer " + customerID + " not successful as Customer not found " + "\n");

          }

      } else {
          if (EventCityCode.equals("MTL")) {
              LogData("Forwarding Cancel  request to Toronto server for Event :" + eventID + " for Customer " + customerID + "\n");
              String value = "cancelEvent:" + customerID + ":" + eventID + ":" + eventType;
              reply = sendEventToCorrectServer(value, 8084);
              LogData("Reply received from Toronto server for Event :" + eventID + " for Customer " + customerID + " is " + reply + " \n");

          } else {
//              OTTAWA
              LogData("Forwarding Cancel  request to Ottawa server for Event :" + eventID + " for Customer " + customerID + "\n");
              String value = "cancelEvent:" + customerID + ":" + eventID + ":" + eventType;
              reply = sendEventToCorrectServer(value, 8085);
              LogData("Reply received from Ottawa server for Event :" + eventID + " for Customer " + customerID + " is " + reply + " \n");

          }
      }
      return reply;
  }

  synchronized public String bookEvent(String customerID, String eventID, String eventType) {
      String reply = "";
      LogData("BOOK EVENT CALLED by :" + customerID);
      System.out.println(customerBooking);
      String[] EventIdArray = (eventID.split("(?<=\\G...)"));
      String EventCityCode = EventIdArray[0];
      if (EventCityCode.equals("TOR")) {
          if (customerBooking.containsKey(customerID) && (customerBooking.get(customerID).contains(eventType + "||" + eventID))) {
              reply = "Event Already Booked for Customer";
              LogData("Event Already Booked : Event :" + eventID + " Already Booked for Customer " + customerID + "\n");
          } else {
              HashMap<String, Integer> temp;
              boolean exists = hashMap.containsKey(eventType);
              if (exists) {
                  temp = hashMap.get(eventType);
                  if (temp.containsKey(eventID)) {
                      if (temp.get(eventID) > 0) {
                          temp.put(eventID, temp.get(eventID) - 1);
                          if (customerBooking.containsKey(customerID))
                              customerBooking.get(customerID).add(eventType + "||" + eventID);
                          else {
                              customerBooking.put(customerID, new HashSet<>());
                              customerBooking.get(customerID).add(eventType + "||" + eventID);
                          }
                          reply = "Successfully Booked";
                          LogData("Successfully Booked : Event :" + eventID + " Event Type: " + eventType + " Successfully Booked for Customer :" + customerID + "\n");
                      } else {
                          reply = "CAPACITY FULL";
                          LogData("CAPACITY FULL  :Event :" + eventID + " Event Type: " + eventType + " not Booked for Customer :" + customerID + "\n");
                      }

                  } else {
                      reply = "No SUCH EVENT ID FOUND";
                      LogData("No SUCH EVENT ID FOUND  :Event :" + eventID + " Event Type: " + eventType + " Not Booked for Customer \n");
                  }

              }
          }
          System.out.println("---->" + customerBooking);

          return reply.trim();
      } else {
          if (EventCityCode.equals("MTL")) {
              LogData("Forwarding the request to Montreal server for Event :" + eventID + " for Customer " + customerID + "\n");
              String value = "bookEvent:" + customerID + ":" + eventID + ":" + eventType;
              reply = sendEventToCorrectServer(value, 8084);
              LogData("Reply received from Toronto server for Event :" + eventID + " for Customer " + customerID + " is " + reply + " \n");

          } else {
//              OTTAWA
              LogData("Forwarding the request to Ottawa server for Event :" + eventID + " for Customer " + customerID + "\n");
              String value = "bookEvent:" + customerID + ":" + eventID + ":" + eventType;
              reply = sendEventToCorrectServer(value, 8085);
              LogData("Reply received from Ottawa server for Event :" + eventID + " for Customer " + customerID + " is " + reply + " \n");
          }
      }
      return reply.trim();
  }

  synchronized public String removeEvent(String eventID, String eventType) {
      HashMap<String, Integer> temp;
      String reply = "";
      boolean exists = hashMap.containsKey(eventType);
      if(checkEventCity(eventID)){
      if (exists) {
          temp = hashMap.get(eventType);
          if (temp.containsKey(eventID)) {
              temp.remove(eventID);
              for (int i = 0; i < customerBooking.size(); i++) {
                  HashSet<String> tempHash = customerBooking.get(i);
                  if (tempHash.contains(eventType + "||" + eventID)) {
                      tempHash.remove(eventType + "||" + eventID);
                  }
              }
              reply = "EVENT ID REMOVED SUCCESSFULLY";
              LogData("EVENT ID REMOVED SUCCESSFULLY : Event :" + eventID + " Event Type: " + eventType + "\n");

          } else {
              reply = "No SUCH EVENT ID FOUND";
              LogData("No SUCH EVENT ID FOUND : Event :" + eventID + " Event Type: " + eventType + ", event id not found \n");
          }

      } else {
          reply = "NO SUCH EVENT TYPE FOUND";
          LogData("NO SUCH EVENT TYPE FOUND: Event :" + eventID + " Event Type: " + eventType + " ,event type not found \n");

      }
      } else {

          reply = "Manager CANNOT REMOVE EVENT FROM ANOTHER CITY";
      }
      return reply.trim();

  }

  synchronized public String listEventAvailability(String eventType){

      HashMap<String, Integer> temp;
      String reply = "TORONTO SERVER "+eventType;
      boolean exists = hashMap.containsKey(eventType);
      if (exists) {
          temp = hashMap.get(eventType);
          reply = reply + " :- " + temp.toString().substring(1, temp.toString().length() - 1);
          LogData("Fetching Event Availability for " + eventType + ":" + reply + "\n");

      } else {
          reply = "NO SUCH EVENT TYPE FOUND ON TORONTO SERVER";
          LogData("NO SUCH EVENT TYPE FOUND : for " + eventType + "\n");
      }
      //Montreal
      LogData("Fetching Data from Montreal Server : for " + eventType + "\n");
      String value = "listEventAvailability:" + eventType;
      reply = reply + "\n" + sendEventToCorrectServer(value, 8084);
      LogData("Fetching Data from Montreal Server : Reply is : " + reply + "\n");

      //Ottawa
      LogData("Fetching Data from Ottawa Server : for " + eventType + "\n");
      String value1 = "listEventAvailability:" + eventType;
      reply = reply + "\n" + sendEventToCorrectServer(value1, 8085);
      LogData("Fetching Data from Ottawa Server : Reply is : " + reply + "\n");

      return reply.trim();
  }

  synchronized public String listEventAvailabilityServerCall(String eventType)  {
      HashMap<String, Integer> temp;
      String reply = "TORONTO SERVER "+ eventType;
      boolean exists = hashMap.containsKey(eventType);
      if (exists) {
          temp = hashMap.get(eventType);
          reply = reply + " :- " + temp.toString().substring(1, temp.toString().length() - 1);
          LogData("Fetching Event Availability for " + eventType + ":" + reply + "\n");
      } else {
          reply = "NO SUCH EVENT TYPE FOUND ON TORONTO SERVER";
          LogData("NO EVENT TYPE FOUND : for " + eventType + "\n");

      }
      return reply;
  }

  synchronized public String addEvent(String eventID, String eventType, int bookingCapacity)  {
	  System.out.println("mkfalfnoedfnoiwdnf0");
      if (checkEventCity(eventID)) {
          boolean exists = hashMap.containsKey(eventType);
          HashMap<String, Integer> temp;
          if (exists) {
              temp = hashMap.get(eventType);
              if (temp.containsKey(eventID)) {
                  temp.remove(eventID);
                  temp.put(eventID, bookingCapacity);
                  LogData("Add Event (Updated): Event :" + eventID + " of Event Type :" + eventType + " added and booking capacity updated to " + bookingCapacity + "\n");


              } else {
                  temp.put(eventID, bookingCapacity);
                  LogData("Add Event (NEW): Event :" + eventID + " of Event Type :" + eventType + " added and booking capacity is " + bookingCapacity + "\n");

              }

          } else {

              temp = new HashMap<>();
              temp.put(eventID, bookingCapacity);
              hashMap.put(eventType, temp);
              LogData("Add Event (NEW): Event :" + eventID + " of Event Type :" + eventType + " added and booking capacity is " + bookingCapacity + "\n");

          }

          return "SUCCESSFULL";

      } else {
          LogData("Add Event : MANAGER CANNOT ADD EVENT OF ANOTHER CITY \n");
          return "MANAGER CANNOT ADD EVENT OF ANOTHER CITY";

      }
  }

/*    @Override
  synchronized public HashMap<String, HashMap<String, Integer>> getHashMap()  {
      System.out.println(hashMap);
      return hashMap;
  }*/

  synchronized public String getBookingSchedule(String customerId)  {
      String reply = "";
      if (customerBooking.containsKey(customerId)) {
          reply = customerBooking.get(customerId).toString();
          LogData("Get Booking Schedule : for Customer" + customerId + "is: " + reply + " \n");

      } else {
          LogData("Get Booking Schedule : NO SUCH CUSTOMER FOUND \n");
          reply = "NO SUCH CUSTOMER FOUND ON TORONTO SERVER";
      }

      //Ottawa
      LogData("Get Booking Schedule : Fetching Data from Ottawa server \n");
      String value = "getBookingSchedule:" + customerId;
      reply = reply + "\n" + sendEventToCorrectServer(value, 8085);
      LogData("Get Booking Schedule : Fetching Data from Ottawa server : The Value is : " + reply + " \n");

      //Montreal
      LogData("Get Booking Schedule : Fetching Data from Montreal server \n");
      String value1 = "getBookingSchedule:" + customerId;
      reply = reply + "\n" + sendEventToCorrectServer(value1, 8084);
      LogData("Get Booking Schedule : Fetching Data from Montreal server : The Value is : " + reply + " \n");


      return reply;
  }

  public String getBookingScheduleServerCall(String customerId)  {
      String reply = "";
      if (customerBooking.containsKey(customerId)) {
          if (customerBooking.get(customerId).size() <= 0) {
              reply = "CUSTOMER NO LONGER HAVE ANY BOOKINGS";
              LogData("Get Booking Schedule : for Customer" + customerId + "is: " + reply + " \n");

          } else {
              reply = customerBooking.get(customerId).toString();
              LogData("Get Booking Schedule : NO SUCH CUSTOMER FOUND \n");
          }
      } else {
          reply = "NO SUCH CUSTOMER FOUND ON TORONTO SERVER";
          LogData("Get Booking Schedule : NO SUCH CUSTOMER FOUND \n");
      }

      return reply;
  }

  public static String sendEventToCorrectServer(String rawMessage, int remotePortNumber) {
      DatagramSocket aSocket = null;
      String value = "";
      try {
          System.out.println("Toronto UDP CLIENT.Client Started........");
          aSocket = new DatagramSocket();
//          TODO : SEND the correct message
          byte[] message = rawMessage.getBytes(); //message to be passed is stored in byte array
          InetAddress aHost = InetAddress.getByName("localhost"); //Host name is specified and the IP address of server host is calculated using DNS.
          int serverPort = remotePortNumber;//agreed upon port
//          int serverPort = 8084;//agreed upon port
          DatagramPacket request = new DatagramPacket(message, rawMessage.length(), aHost, serverPort);//request packet ready
          aSocket.send(request);//request sent out
          System.out.println("Request message sent from the client is : " + new String(request.getData()));

          byte[] buffer = new byte[100000];//to store the received data, it will be populated by what receive method returns
          DatagramPacket reply = new DatagramPacket(buffer, buffer.length);//reply packet ready but not populated.

          //CLIENT.Client waits until the reply is received-----------------------------------------------------------------------
          aSocket.receive(reply);//reply received and will populate reply packet now.
          System.out.println("Reply received from the server is: " + new String(reply.getData()));//print reply message after converting it to a string from bytes
          value = new String(reply.getData()).trim();
      } catch (SocketException e) {
          System.out.println("Socket: " + e.getMessage());
      } catch (IOException e) {
          e.printStackTrace();
          System.out.println("IO: " + e.getMessage());
      } finally {
          if (aSocket != null)
              aSocket.close();//now all resources used by the socket are returned to the OS, so that there is no
          //resource leakage, therefore, close the socket after it's use is completed to release resources.
      }

      return value;
  }

  public boolean checkEventCity(String id) {
      String city = id.substring(0, 3).toUpperCase();
      if (city.equals(name)) {
          return true;
      } else
          return false;
  }

  public void LogData(String value) {
      Date date = new Date(); // this object contains the current date value
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      File log = new File("TorontoLog.txt");
      try {
          if (!log.exists()) {
              System.out.println("We had to make a new file.");
              log.createNewFile();
          }
          FileWriter fileWriter = new FileWriter(log, true);
          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
          bufferedWriter.write(date + " : " + value + "\n");
          bufferedWriter.close();
      } catch (IOException e) {
          System.out.println("COULD NOT LOG!!");
      }

  }


  
  
}