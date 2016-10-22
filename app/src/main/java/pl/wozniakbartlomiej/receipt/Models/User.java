package pl.wozniakbartlomiej.receipt.Models;

/**
 * Created by Bartek on 16/10/16.
 */
public class User {
    private String id;
    private String email;
    private boolean isChecked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id= id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
