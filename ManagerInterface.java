package AssignmentDistri;
import java.rmi.*;
import java.util.HashMap;

public interface ManagerInterface extends Remote {

    public String managerID = "";
    public String managerCity = "";


    public String bookEvent(String customerID, String eventID, String eventType) throws RemoteException;

    public String removeEvent(String eventID, String eventType) throws RemoteException;

    public String listEventAvailability(String eventType) throws RemoteException;

    public String addEvent(String eventID, String eventType, int bookingCapacity) throws RemoteException;

    public HashMap<String, HashMap<String, Integer>> getHashMap() throws RemoteException;

    public String getBookingSchedule(String customerID) throws RemoteException;

    public String cancelEvent(String customerID, String eventID, String eventType) throws RemoteException;

}

