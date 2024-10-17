import java.io.FileWriter;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

/**
 * Server class to fetch, encrypt, and send an AuctionItem object to a requesting Client application.
 * 
 * @author holbroow
 */
public class Server implements Auction {
    private ArrayList<AuctionItem> items;    // ArrayList 'items' to store hard-coded items for the auction.
    private static SecretKey key;            // Key object for actual use with decrypting incoming object(s).
    private static Cipher cipher;            // The cipher to be used in conjunction with the key for decryption.

    public Server() {
        super();
        // A hard-coded ArrayList of items to be browsed.
        items = new ArrayList<>();
        items.add(new AuctionItem(1, "Vase", "A lovely china vase."));
        items.add(new AuctionItem(2, "Table", "A vintage mahogany table."));
        items.add(new AuctionItem(3, "Roman Coin", "A delicate coin from the roman era."));
        items.add(new AuctionItem(4, "Silver Watch", "A stylish wrist watch."));
        items.add(new AuctionItem(5, "Mirror", "A mirror, refurbished to perfection."));

        // Generate random AES key.
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keyGen.init(random);
            key = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm for key not found.");
        }
        
        // Store the generated key in a file.
        byte[] keyBytes = key.getEncoded();
        try (FileWriter writer = new FileWriter("../keys/keyFile.aes")) {
            writer.write(Base64.getEncoder().encodeToString(keyBytes));
            System.out.println("AES key written to /keys/keyFile.aes");
        } catch (IOException e) {
            System.err.println("Error writing to file.");
        }

    }
  
    // 'getSpec' method to return an encypted object as per client request.
    public SealedObject getSpec(int n) {
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init( Cipher.ENCRYPT_MODE, key );
            
            // Get object from ArrayList.
            for (AuctionItem item : items) {
                if (item.getItemID() == n) {
                    // Return the encrypted version using the cipher, and inform the server user.
                    System.out.println("client request handled");
                    return new SealedObject(item, cipher);
                }
            }
            // If item not found, return null and the client will handle the exception.
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Create and run the server to recieve and serve client requests.
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