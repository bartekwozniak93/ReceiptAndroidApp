package pl.wozniakbartlomiej.receipt.Models;

/*
 * ListData class will hold data for displaying in ListView
 * */
public class Event {

    private String id;
    private String title;
    private String description;
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