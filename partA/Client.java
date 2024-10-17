import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Client class to recieve, decrypt, and display info for incoming objects from a Server application.
 * 
 * @author holbroow
 */
public class Client{
  private static SecretKey key;     // Key object for actual use with decrypting incoming object(s).
  private static String plainKey;   // The key read from the file in plain text.
  private static Cipher cipher;     // The cipher to be used in conjunction with the key for decryption.

  public static void main(String[] args) {
    // Handle the case where no args are given, in which the user is informed of the command line usage.
    if (args.length < 1) {
      System.out.println("Usage: java Client n");
      return;
    }

    // Assign said command line argument.
    int n = Integer.parseInt(args[0]);

    // Read the key.txt file, if existing, and store and convert the String to a usable key for decryption.
    try {
        // Create AES cipher for decryption.
        cipher = Cipher.getInstance( "AES" );

        File keyFile = new File("key.txt");
        Scanner s = new Scanner(keyFile);
        
        plainKey = s.nextLine();
        byte[] decodedKey = Base64.getDecoder().decode(plainKey);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        
        s.close();
    } catch (Exception e) {
        // Incase key.txt not found.
        e.printStackTrace();
    }

    // Connect to server and recieve the encypted object, before decrypting it and printing out it's respective variables.
    try {
      String name = "myserver";
      Registry registry = LocateRegistry.getRegistry("localhost", 0);
      Auction server = (Auction) registry.lookup(name);

      cipher.init(Cipher.DECRYPT_MODE, key);
      SealedObject recievedObject = server.getSpec(n);
      AuctionItem result = (AuctionItem) recievedObject.getObject( cipher );
      
      System.out.println("Matching AuctionItem retrieved.");
      System.out.printf("Auction Item: %s - %s - Current Highest Bid: %d\n\n", result.name, result.description, result.highestBid);
    }

    catch (Exception e) {
      System.err.println("Exception:");
      e.printStackTrace();
    }

  }
}
