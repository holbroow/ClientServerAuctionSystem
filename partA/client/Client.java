import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Client{
  private static SecretKey key;
  private static String plainKey;
  private static Cipher cipher;
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java Client n");
      return;
    }

    int n = Integer.parseInt(args[0]);

    try {
        File keyFile = new File("key.txt");
        Scanner s = new Scanner(keyFile);
        cipher = Cipher.getInstance( "AES" );
        
        plainKey = s.nextLine();
        byte[] decodedKey = Base64.getDecoder().decode(plainKey);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        s.close();
    } catch (Exception e) {
        // Incase key.txt not found.
        e.printStackTrace();
    }

    try {
      String name = "myserver";
      Registry registry = LocateRegistry.getRegistry("localhost", 0);
      Auction server = (Auction) registry.lookup(name);

      cipher.init(Cipher.DECRYPT_MODE, key);
      SealedObject recievedObject = server.getSpec(n);
      AuctionItem result = (AuctionItem) recievedObject.getObject( cipher );
      
      //AuctionItem result = server.getSpec(n);
      System.out.println("Matching AuctionItem retrieved.");
      System.out.printf("Auction Item: %s - %s - Current Highest Bid: %d\n\n", result.name, result.description, result.highestBid);
    }

    catch (Exception e) {
      System.err.println("Exception:");
      e.printStackTrace();
    }

  }
}
