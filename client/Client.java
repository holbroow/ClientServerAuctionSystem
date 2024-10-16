import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client{
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java Client n");
      return;
    }

    int n = Integer.parseInt(args[0]);
    try {
      String name = "myserver";
      Registry registry = LocateRegistry.getRegistry("localhost");
      Auction server = (Auction) registry.lookup(name);

      AuctionItem result = server.getSpec(n);
      System.out.println("Matching AuctionItem retrieved.");
    }

    catch (Exception e) {
      System.err.println("Exception:");
      e.printStackTrace();
    }

  }
}
