package edu.nwmissouri.personalfinancetracker.Manager;

public class DatabaseManager {
    private static DatabaseManager instance;

    private int fdAmount = 2000;
    private int stocksAmount = 2500;
    private int rdAmount = 1500;

    private String email = "john@gmail.com";
    private String firstName = "Barak";
    private String lastName = "Obama";
    private String mobile = "123-456-65";

    // Private constructor to prevent instantiation.
    public DatabaseManager() {
    }

    // Get the singleton instance of DatabaseManager.
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Get the FD (Fixed Deposit) amount.
    public int getFD() {
        return fdAmount;
    }

    // Get the Stocks amount.
    public int getStocks() {
        return stocksAmount;
    }

    // Get the RD (Required Deposit) amount.
    public int getRD() {
        return rdAmount;
    }


    public void setEmail(String email) {
       this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }
}
