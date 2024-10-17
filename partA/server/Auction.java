import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.crypto.SealedObject;

/**
 * Auction interface to allow clients to use the 'getSpec' function within the Server class.
 * 
 * @author holbroow
 */
public interface Auction extends Remote {
    public SealedObject getSpec(int itemID) throws RemoteException;
}