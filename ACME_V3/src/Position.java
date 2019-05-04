public class Position {
    /* Vars */
    private String positionName;
    private Integer positionQuantity;
    private Double positionValue;
    private String positionDate;

    /* Constructor */
    public Position(String positionName, Integer positionQuantity, Double positionValue, String positionDate){
        this.positionName = positionName;
        this.positionQuantity = positionQuantity;
        this.positionValue = positionValue;
        this.positionDate = positionDate;
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
