package pl.wozniakbartlomiej.receipt.Models;

/*
 * ListData class will hold data for displaying in ListView
 * */
public class Cost {
    private String user;
    private String userForCost;
    private String valueForCost;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserForCost() {
        return userForCost;
    }

    public void setUserForCost(String userForCost) {
        this.userForCost = userForCost;
    }

    public String getValueForCost() {
        return valueForCost;
    }

    public void setValueForCost(String valueForCost) {
        this.valueForCost = valueForCost;
    }
}