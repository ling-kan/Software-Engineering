public class Stock {
    /* Vars */
    private String stockName;
    private Integer stockQuantity;
    private Double stockValue;
    private Double valueChange;

    /* Constructor */
    public Stock(String stockName,Double stockValue,Integer stockQuantity,Double valueChange){
        this.stockName=stockName;
        this.stockQuantity=stockQuantity;
        this.stockValue=stockValue;
        this.valueChange=valueChange;
    }

    /* Encapsulate vars */
    public String getStockName() {
        return stockName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getStockValue() {
        return stockValue;
    }

    public void setStockValue(Double stockValue) {
        this.stockValue = stockValue;
    }

    public Double getValueChange() {
        return valueChange;
    }

    public void setValueChange(Double valueChange) {
        this.valueChange = valueChange;
    }
}