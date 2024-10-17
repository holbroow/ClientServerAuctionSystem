/**
 * AuctionItem object containing variables to describe an item for sale.
 * 
 * @author holbroow
 */
public class AuctionItem implements java.io.Serializable {
    int itemID;
    String name;
    String description;
    int highestBid;

    public AuctionItem(int id, String name, String desc) {
        this.itemID = id;
        this. name = name;
        this.description = desc;
        this.highestBid = 0;
    }
}