package pl.wozniakbartlomiej.receipt.Models;

/**
 * Created by Bartek on 22/10/16.
 * ListData class will hold data for displaying in ListView
 * */
public class Receipt {

    private String id;
    private String title;
    private String description;
    private String total;
    private String eventId;
    private int imageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id= id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imgResId) {
        this.imageId = imgResId;
    }

}