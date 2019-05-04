package API;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StockMarket implements Runnable
{
    /* Classes */
    private static StockMarket stockMarket;
    private ArrayList<Stock> stockList = new ArrayList<>();

    /* File handling */
    private InputStream loadFile = getClass().getResourceAsStream("stocks.csv");
    private InputStreamReader readStream = new InputStreamReader(loadFile);
    private BufferedReader readFile = new BufferedReader(readStream);

    /* Constructor */
    private StockMarket()
    {
        /* Populate stock list */
        updateStockList();
    }

    /* Singleton */
    public static StockMarket getStockMarket()
    {
        if(stockMarket == null)
        {
            stockMarket = new StockMarket();
        }
        return stockMarket;

    }

    /* Get stocks from .csv file */
    private void updateStockList(){
        String s;
        /* Populate the stocks */
        try {
            /* Default file is stocks.csv */
            while((s=readFile.readLine())!=null){
                String [] temp = s.split(",");
                /* Create an object and pop it in the StockList ArrayList */
                stockList.add(new Stock(temp[0],Double.parseDouble(temp[1]),Integer.parseInt(temp[2]),Double.parseDouble(temp[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Update stock prices at random */
    private void updateStockPrice(){
        /* Generate randoms */
        Random random = new Random();
        /* Loop through the stock objects */
        for (int i = 0; i<stockList.size();i++){
            /* Update on a true/false basis, not all stocks should update every time this is called */
            if (random.nextBoolean()){
                /* Set the change */
                Double temp = random.nextInt(11)*random.nextDouble();
                if (random.nextBoolean()){
                    /* Positive change */
                    Double diff = stockList.get(i).getStockValue();
                    stockList.get(i).setStockValue(stockList.get(i).getStockValue()+temp);
                    stockList.get(i).setValueChange(stockList.get(i).getStockValue()-diff);
                } else {
                    /* Negative change */
                    Double diff = stockList.get(i).getStockValue();
                    stockList.get(i).setStockValue(stockList.get(i).getStockValue()+-temp);
                    stockList.get(i).setValueChange(stockList.get(i).getStockValue()-diff);
                }
                /* If the value change generated is 0 then there's no need to set the stock value to something else or print it */
                if (stockList.get(i).getValueChange()!=0.0D){
                    /* Print the update locally */
                    System.out.println("UPD:"+stockList.get(i).getStockName()
                            +":"+stockList.get(i).getStockValue()
                            +":"+stockList.get(i).getValueChange());
                }
            }
        }
    }

    /* Get index of stock */
    public Integer getStockIndex(String stockName){
        /* If size is 0 then there are no stocks and therefore no match */
        if (stockList.size() >= 1) {
            /* Cycle through and check for a match */
            for (int i = 0; i < stockList.size(); i++) {
                /* Compare index to passed data */
                if (stockList.get(i).getStockName().equals(stockName)) {
                    /* Return the index */
                    return i;
                }
            }
        }
        return 0;
    }

    /* Implements runnable */
    @Override
    public void run(){
        /* Run price updates every 5S */
        while (true){
            /* Update */
            updateStockPrice();
            /* Just print a blank line to make the console neater */
            System.out.println();
            /* 5S delay */
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* Encapsulate stock array */
    public ArrayList<Stock> getStockList() {
        return stockList;
    }
}



















