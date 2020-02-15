package com.pawelnie.expensessplitter.calc;

import java.util.LinkedHashMap;

public class DebtSplitter {
    private LinkedHashMap<Long, Double> debtList;
    private double [][] debtArray;
    private LinkedHashMap<Long, Double> balanceMap = new LinkedHashMap<>();

    public double [][] getDebtArray(Occasion occasion){

        int n = occasion.getPersonsList().size();
        double [][] debtArray = new double[n][n];
        for (Person p : occasion.getPersonsList()){
            balanceMap.put(p.getId(), occasion.calculateTotalBalance(p.getId()));
        }

        BalanceMaxVal tempMax = this.findBalanceMaxVal();

        while (tempMax.getMaxVal() != 0){

            System.out.println("tempMaxVal: "+tempMax.getMaxVal());
            for (int i = 0; i < n; i++){
                if (balanceMap.get(Long.valueOf(i)) < 0){
                    if (tempMax.getMaxVal() > Math.abs(balanceMap.get(Long.valueOf(i)))){
                        debtArray[tempMax.getMaxValId().intValue()][i] =
                                balanceMap.get(Long.valueOf(i));

                        balanceMap.replace(tempMax.getMaxValId(),
                                Double.valueOf(tempMax.getMaxVal()+
                                        balanceMap.get(Long.valueOf(i))));

                        balanceMap.replace(Long.valueOf(i),
                                Double.valueOf(0));

                    }else{
                        debtArray[tempMax.getMaxValId().intValue()][i] =
                                tempMax.getMaxVal();

                        balanceMap.replace(Long.valueOf(i),
                                Double.valueOf(tempMax.getMaxVal()+
                                        balanceMap.get(Long.valueOf(i))));

                        balanceMap.replace(tempMax.getMaxValId(),
                                Double.valueOf(0));
                        continue;
                    }
                    tempMax = this.findBalanceMaxVal();
                    if (tempMax.getMaxVal() == 0) break;
                }
            }

        }
        return debtArray;
    }

    private BalanceMaxVal findBalanceMaxVal(){
        Long maxValId = null;
        Double maxVal = new Double(0);
        for (Long id : balanceMap.keySet()){
            if (balanceMap.get(id) > maxVal){
                maxVal = balanceMap.get(id);
                if (maxVal < 0.00001) maxVal = 0.0;
                maxValId = id;
            }
        }
        return new BalanceMaxVal(maxValId, maxVal);
    }
}

class BalanceMaxVal{
    private Long id;
    private Double value;

    public BalanceMaxVal(Long maxValId, Double maxVal) {
        this.id = maxValId;
        this.value = maxVal;
    }

    public Long getMaxValId() {
        return id;
    }

    public void setMaxValId(Long maxValId) {
        this.id = maxValId;
    }

    public Double getMaxVal() {
        return value;
    }

    public void setMaxVal(Double maxVal) {
        this.value = maxVal;
    }
}