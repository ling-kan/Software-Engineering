package API;

import java.util.ArrayList;
import java.util.Date;

public class User {
    /* Vars */
    private String userName;
    private String userPassword;
    /* Default balance of all new users is 10K */
    private Double userBalance = 10000D;

    /* Positions held by user - stock, qty, price (at purchase) */
    private ArrayList<Position> heldPositions = new ArrayList<>();

    /* Constructor */
    public User(String userName,String userPassword){
        this.userName=userName;
        this.userPassword=userPassword;
    }

    /* Encapsulation */
    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public ArrayList<Position> getHeldPositions() {
        return heldPositions;
    }

    /* give position a date so it can be identified later, could for example have 2 positions with the same stock and qty but different values */
    public String addPosition(Stock stock, Integer stockQuantity,String positionDate) {
        /* Check if user can afford this purchase */
        if (stock.getStockValue()*stockQuantity>this.userBalance){
            return "Insufficient funds!";
        } else{
            /* Debit user price of position */
            this.userBalance -= stock.getStockValue()*stockQuantity;
            /* Deduct size of position from stock total */
            stock.setStockQuantity(stock.getStockQuantity()-stockQuantity);
            /* Add the position */
            this.heldPositions.add(new Position(stock,stockQuantity,positionDate));
        }
        // This can be run on the PrintWiter
        return "Purchase made";
    }

    /* stock sell - pass date from gui position viewer */
    public void sellPosition(Stock stock,String positionDate) {
        /* Don't check if empty */
        if (getHeldPositions().size()>=1){
            for (int i = 0; i<getHeldPositions().size();i++){
                /* Search all positions to find correct one based on date and the stock */
                if (getHeldPositions().get(i).getPositionName().equals(stock.getStockName())&&getHeldPositions().get(i).getPositionDate().equals(positionDate)){
                    /* Return the stock to the total */
                    stock.setStockQuantity(stock.getStockQuantity()+getHeldPositions().get(i).getPositionQuantity());
                    /* Credit user balance the price of the stock at point of sale */
                    this.userBalance += stock.getStockValue()*getHeldPositions().get(i).getPositionQuantity();
                    /* Remove the position */
                    this.heldPositions.remove(getHeldPositions().get(i));
                }
            }

        }
    }
}