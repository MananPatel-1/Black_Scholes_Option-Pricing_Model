package com.company;

public class Main {
    /**
     S = stock price / current stock price
     K = strike price
     R = risk-free rate
     T = time to maturity (denoted in days)
     V = implied volatility
     */

    public static void main(String[] args) {
        // passing values as an object :     call/put| Stock| Strike |Interest| Time| Implied Volatility
        BlackScholes obj = new BlackScholes(true,25,15 ,0.05  ,3.5 ,0.20);
        double price =  obj.calcOptPrice();
        System.out.println("price : " + price);
    }
}
