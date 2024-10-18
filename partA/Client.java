import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Client class to receive, decrypt, and display info for incoming objects from a Server application.
 * 
 * @author holbroow
 */
public class Client {
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

        // Create cipher for use with decryption.
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm for key not found.");
        } catch (NoSuchPaddingException e) {
            System.err.println("No such padding for cipher creation.");
        }

        // Read the testKey.aes file to use the key for decryption.
        byte[] keyBytes = new byte[16];
        try (FileInputStream fis = new FileInputStream("keys/testKey.aes")) {
            int bytesRead = fis.read(keyBytes);
            if (bytesRead != keyBytes.length) {
                throw new IOException("Could not read the full AES key from file. Is the server running? / Has it generated a key?");
            } else {
                key = new SecretKeySpec(keyBytes, "AES");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Connect to server and receive the encrypted object, before decrypting it and printing out its respective variables.
        try {
            String name = "Auction";
            Registry registry = LocateRegistry.getRegistry("localhost", 0);
            Auction server = (Auction) registry.lookup(name);

            cipher.init(Cipher.DECRYPT_MODE, key);
            SealedObject receivedObject = server.getSpec(n);
            AuctionItem result = (AuctionItem) receivedObject.getObject(cipher);
            
            System.out.println("Matching AuctionItem retrieved.");
            System.out.printf("Auction Item: %s - %s - Current Highest Bid: %d\n\n", result.getName(), result.getDescription(), result.getHighestBid());
        } catch (Exception e) {
            System.err.println("Error, does the auction item exist on the server?");
        }
    }
}