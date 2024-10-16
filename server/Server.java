import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server implements Auction {
    private ArrayList<AuctionItem> items;
    public Server() {
        super();
        items = new ArrayList<>();
        items.add(new AuctionItem(0, "Vase", "A lovely china vase."));
        items.add(new AuctionItem(0, "Table", "A vintage mahogany table."));
        items.add(new AuctionItem(0, "Roman Coin", "A delicate coin from the roman era."));
    }
  
    public AuctionItem getSpec(int n) {
        
        System.out.println("client request handled");
        return items.get(n);
    }
  
    public static void main(String[] args) {
        try {
            Server s = new Server();
            String name = "myserver";

            Auction stub = (Auction) UnicastRemoteObject.exportObject(s, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);

            System.out.println("Server ready");

        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
      }
  }
