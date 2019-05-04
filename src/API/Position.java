package API;

import java.util.Date;

public class Position {
    /* Vars */
    private String positionName;
    private Integer positionQuantity;
    private Double positionValue;
    private String positionDate;

    /* Constructor */
    public Position(Stock stock, Integer positionQuantity,String positionDate){
        this.positionName = stock.getStockName();
        this.positionQuantity = positionQuantity;
        this.positionValue = stock.getStockValue()*positionQuantity;
        this.positionDate=positionDate;
    }

    /* Encapsulate fields */
    public String getPositionName() {
        return positionName;
    }

    public Integer getPositionQuantity() {
        return positionQuantity;
    }

    public Double getPositionValue() {
        return positionValue;
    }

    public String getPositionDate() {
        return positionDate;
    }
}
