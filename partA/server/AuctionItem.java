/**
 * AuctionItem object containing variables to describe an item for sale.
 * 
 * @author holbroow
 */
public class AuctionItem implements java.io.Serializable {
    private int itemID;
    private String name;
    private String description;
    private int highestBid;

    public AuctionItem(int id, String name, String desc) {
        this.itemID = id;
        this. name = name;
        this.description = desc;
        this.highestBid = 0;
    }

    public int getItemID() {
        return this.itemID;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getHighestBid() {
        return this.highestBid;
    }

    public void setHighestBid(int n) {
        this.highestBid = n;
    }

}