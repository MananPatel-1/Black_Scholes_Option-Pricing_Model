package com.company;

/** the private instance variables are as follows
   S = stock price / current stock price
   K = strike price
   R = risk-free rate
   T = time to maturity (denoted in days)
   V = implied volatility
 */

public class BlackScholes  {

    private boolean call;
    private double S;
    private double K;
    private double R;
    private double T;
    private double V;
    private double F;

    private  double Delta; // Intrinsic value of the underlying asset
    private  double Gamma;// Rate of change of delta
    private  double Vega; // change in volatility
    private  double Rho;// risk-free interest


    public BlackScholes(boolean call, double s, double k, double r, double t, double v ) {
        S = s;
        K = k;
        R = r;
        T = t;
        V = v;
        this.call = call;
    }

    public double calcOptPrice(){
        F = S * Math.exp((R  * T));
        double SD = standardNormalDistribution(d1());
        double d1 = d1();
        double d2 = d2();
        double CD1call = CND(d1, SD);
        double CD2call = CND(d2);
        double CD1put = CND(-d1);
        double CD2put = CND(-d2);

        // Greeks
        Gamma = SD / (S * V * Math.sqrt(T));
        Vega = S * SD * Math.sqrt(T);

        if (call){
            Rho = K * T * Math.exp(-R * T) * CD2call;
            Delta = CD1call;
            printGreek();

            return CallOption( CD1call, CD2call);
        }
        else{
            Rho = -K * T * Math.exp(-R * T) * CD2put;
            Delta = CD1put - 1;
            printGreek();

            return PutOption(CD1put, CD2put);
        }
    }

    public double CND(double x) {
        return CND(x, standardNormalDistribution(x));
    }

    // Here we calculate Standard normal probability density function
    private double standardNormalDistribution(double x) {
        return (Math.exp(-0.5 * Math.pow(x, 2))) / Math.sqrt(2 * Math.PI);
    }

    private void printGreek() {
        System.out.println("Delta: "+ Delta + ", Gamma: "+ Gamma + ", Vega: "+ Vega + ", Rho: "+ Rho);
    }

    //  cumulative normal distribution function (Calculating for X)
    private double CND(double x, double SD) {
        //  6 constant values for numerical approximation
         double P = 0.2316419;
         double b1 = 0.319381350;
         double b2 = -0.356563782;
         double b3 = 1.781477937;
         double b4 = -1.821255978;
         double b5 = 1.330274429;


        double t = 1 / (1 + P * Math.abs(x));
        double t1 = t;
        double t2 = t1 * t;
        double t3 = t2 * t;
        double t4 = t3 * t;
        double t5 = t4 * t;
        double B1 = b1 * t1;
        double B2 = b2 + t2;
        double B3 = b3 + t3;
        double B4 = b4 + t4;
        double B5 = b5 + t5;
        double sum = B1 + B2 + B3 + B4 + B5;
        double CD = 1 - SD * sum;
        return (x < 0) ? 1 - CD : CD;
}

    // Calculating the call option
    private double CallOption(double CD1call, double CD2call) {
        return (F * CD1call - K * CD2call) * Math.exp(-R * T);

    }
    // Calculating the put option
    private double PutOption(double CD1put, double CD2put) {
        return (K * Math.exp(-R * T) * CD2put) - (S * CD1put);

    }
    // the cumulative density function of normal distribution, is the risk-adjusted probability that the option will be exercised
    private double d1(){return (Math.log(F / K) + ((Math.pow(V, 2) * T) / 2)) / (Math.pow(T, 0.5) * V);}

    // the cumulative density function of normal distribution, is the probability of receiving the stock at expiration of the option
    private double d2(){

        return d1() - (V * Math.pow(T, 0.5));


    }

}
