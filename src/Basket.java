import java.util.ArrayList;
import java.util.List;

public class Basket {

    private int bucketId;
    private List<Integer> itemList;
    public Basket(int bucketId) {
        this.bucketId = bucketId;
        itemList = new ArrayList<>();
    }
    public int getBucketId() {
        return bucketId;
    }
    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }
    public List<Integer> getItemList() {
        return itemList;
    }
    public void setItemList(List<Integer> itemList) {
        this.itemList = itemList;
    }
    public void addItem(int item) {
        this.itemList.add(item);
    }
}
