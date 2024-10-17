import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server implements Auction {
    private ArrayList<AuctionItem> items;
    private static SecretKey key;
    private static String plainKey;
    private static Cipher cipher;

    public Server() {
        super();
        items = new ArrayList<>();
        items.add(new AuctionItem(0, "Vase", "A lovely china vase."));
        items.add(new AuctionItem(0, "Table", "A vintage mahogany table."));
        items.add(new AuctionItem(0, "Roman Coin", "A delicate coin from the roman era."));

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

    }
  
    public SealedObject getSpec(int n) {
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init( Cipher.ENCRYPT_MODE, key );
            
            AuctionItem plainObject = items.get(n);
        
            System.out.println("client request handled");
            return new SealedObject(plainObject, cipher);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
