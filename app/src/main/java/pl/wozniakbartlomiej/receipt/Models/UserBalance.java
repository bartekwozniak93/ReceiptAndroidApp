package pl.wozniakbartlomiej.receipt.Models;

/**
 * Created by Bartek on 04/11/16.
 */
public class UserBalance {
    private String eventTitle;
    private String eventDescription;
    private String balance;

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
