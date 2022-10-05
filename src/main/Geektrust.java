package main;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 @author Chandra Prakash
  * @date 10/04/22
 */

public class Geektrust {

    Double investedEquityPercentage , investedGoldPercentage , investedDebtPercentage ;
    Integer totalEquityInvested,totalDebtInvested,totalGoldInvested;
    Double changeEquityPercentage,changeGoldPercentage,changeDebtPercentage;
    Integer equitySIP,debtSIP,goldSIP;
    Integer totalAmountToBeRebalanced=0;
    HashMap<String, List<Integer>> balanceTracker = new HashMap<>();

    public  static void main(String args[]){
        Geektrust geektrust = new Geektrust();
        try {
        if(args.length<1 || isNullOrEmpty(args[0]))
        {
            throw new Exception("File Not Provided");
        }}
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        geektrust.balanceManager(args[0]);

    }

    private void balanceManager(String filePath)  {

        try {
            File configFile = new File(filePath);
            FileInputStream inputStream = new FileInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
            String [] data  = line.split(" ");
            String inputValue =data[0];
            switch (inputValue) {
                case "ALLOCATE":
                    totalEquityInvested = Integer.valueOf(data[1]);
                    totalDebtInvested =  Integer.valueOf(data[2]);
                    totalGoldInvested = Integer.valueOf(data[3]);
                    calculateIntialPercentageInvested(totalEquityInvested, totalDebtInvested, totalGoldInvested);
                  break;
                case "SIP":
                    equitySIP = Integer.valueOf(data[1]);
                    debtSIP = Integer.valueOf(data[2]);
                    goldSIP = Integer.valueOf(data[3]);
                    break;
                case "CHANGE":
                    changeEquityPercentage = Double.valueOf(data[1].replace("%",""));
                    changeDebtPercentage =  Double.valueOf(data[2].replace("%",""));
                    changeGoldPercentage = Double.valueOf(data[3].replace("%",""));
                    calculateChangedInvestedAmount(changeEquityPercentage, changeGoldPercentage, changeDebtPercentage,data[4]);
                    SIP(equitySIP,debtSIP,goldSIP);
                    break;
                case "BALANCE":
                        List<Integer> balance =  balanceTracker.get(data[1]);
                        System.out.println(balance.get(0)+" "+balance.get(1)+" "+balance.get(2));
                    break;
                case "REBALANCE":
                    if(balanceTracker.get("DECEMBER")!=null && balanceTracker.get("DECEMBER").size()>0)
                    {
                        rebalance(balanceTracker.get("DECEMBER"));
                    }
                    else if(balanceTracker.get("JUNE")!=null && balanceTracker.get("JUNE").size()>0)
                    {
                        rebalance(balanceTracker.get("JUNE"));
                    }
                    else
                    {
                        System.out.println("CANNOT_REBALANCE");
                    }
            }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }catch (IOException e) {
            System.out.println("Invalid Input");
        }
    }


    private void  rebalance(List<Integer> assetAmont)
    {
        for(Integer value: assetAmont)
        {
            totalAmountToBeRebalanced =totalAmountToBeRebalanced+value;
        }
        totalEquityInvested=(int)Math.floor((totalAmountToBeRebalanced*investedEquityPercentage)/100);
        totalDebtInvested=(int)Math.floor((totalAmountToBeRebalanced*investedDebtPercentage)/100);
        totalGoldInvested=(int)Math.floor((totalAmountToBeRebalanced*investedGoldPercentage)/100);
        System.out.println(totalEquityInvested+" "+totalDebtInvested+" "+totalGoldInvested);
    }

    private  void calculateIntialPercentageInvested(Integer totalEquityInvested, Integer totalDebtInvested, Integer totalGoldInvested) {
        Integer amountInvested = totalEquityInvested+totalDebtInvested+totalGoldInvested;
         investedEquityPercentage = ((Double.valueOf(totalEquityInvested)/Double.valueOf(amountInvested))*100);
         investedDebtPercentage = ((Double.valueOf(totalDebtInvested)/Double.valueOf(amountInvested))*100);
         investedGoldPercentage = ((Double.valueOf(totalGoldInvested)/Double.valueOf(amountInvested))*100);
    }

    private void SIP(Integer equitySIP,Integer debtSIP,Integer goldSIP)
    {
        totalEquityInvested =totalEquityInvested+equitySIP;
        totalDebtInvested = totalDebtInvested+debtSIP;
        totalGoldInvested = totalGoldInvested + goldSIP;
    }

    private  void calculateChangedInvestedAmount(Double changeEquityPercentage,Double changeGoldPercentage,Double changeDebtPercentage,String month)
    {

        List<Integer> updatedBalance = new ArrayList<>();
            totalEquityInvested = (int)(Math.floor(totalEquityInvested + ((totalEquityInvested * changeEquityPercentage) / 100)));
            totalGoldInvested =(int)(Math.floor(totalGoldInvested + ((totalGoldInvested * changeGoldPercentage) / 100)));
            totalDebtInvested = (int)(Math.floor(totalDebtInvested + ((totalDebtInvested * changeDebtPercentage) / 100)));

        updatedBalance.add(totalEquityInvested);
        updatedBalance.add(totalDebtInvested);
        updatedBalance.add(totalGoldInvested);

        balanceTracker.put(month,updatedBalance);
    }

    public static boolean isNullOrEmpty(String pStr) {
        return pStr == null || pStr.trim().length() == 0 || pStr.trim().equalsIgnoreCase("null");
    }
}
