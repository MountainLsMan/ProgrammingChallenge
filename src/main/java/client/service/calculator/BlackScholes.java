package client.service.calculator;

// reference
// https://introcs.cs.princeton.edu/java/22library/Gaussian.java.html
// https://introcs.cs.princeton.edu/java/22library/BlackScholes.java.html

public class BlackScholes {

    public static double calculateCallOptionPrice(double spotPrice, double strikePrice, double interestRate, double volatility, double timeToExpiration) {
        double d1 = (Math.log(spotPrice / strikePrice) + (interestRate + volatility * volatility / 2) * timeToExpiration) / (volatility * Math.sqrt(timeToExpiration));
        double d2 = d1 - volatility * Math.sqrt(timeToExpiration);
        return spotPrice * Gaussian.cdf(d1) - strikePrice * Math.exp(-interestRate * timeToExpiration) * Gaussian.cdf(d2);
    }

    public static double calculatePutOptionPrice(double spotPrice, double strikePrice, double interestRate, double volatility, double timeToExpiration) {
        double d1 = (Math.log(spotPrice / strikePrice) + (interestRate + volatility * volatility / 2) * timeToExpiration) / (volatility * Math.sqrt(timeToExpiration));
        double d2 = d1 - volatility * Math.sqrt(timeToExpiration);
        return strikePrice * Math.exp(-interestRate * timeToExpiration) * Gaussian.cdf(-d2) - spotPrice * Gaussian.cdf(-d1);
    }

}

class Gaussian {

    // return pdf(x) = standard Gaussian pdf
    public static double pdf(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    // return pdf(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static double pdf(double x, double mu, double sigma) {
        return pdf((x - mu) / sigma) / sigma;
    }

    // return cdf(z) = standard Gaussian cdf using Taylor approximation
    public static double cdf(double z) {
        if (z < -8.0) return 0.0;
        if (z > 8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * pdf(z);
    }

    // return cdf(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    public static double cdf(double z, double mu, double sigma) {
        return cdf((z - mu) / sigma);
    }

    // Compute z such that cdf(z) = y via bisection search
    public static double inverseCDF(double y) {
        return inverseCDF(y, 0.00000001, -8, 8);
    }

    // bisection search
    private static double inverseCDF(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (cdf(mid) > y) return inverseCDF(y, delta, lo, mid);
        else return inverseCDF(y, delta, mid, hi);
    }

    // return phi(x) = standard Gaussian pdf
    @Deprecated
    public static double phi(double x) {
        return pdf(x);
    }

    // return phi(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    @Deprecated
    public static double phi(double x, double mu, double sigma) {
        return pdf(x, mu, sigma);
    }

    // return Phi(z) = standard Gaussian cdf using Taylor approximation
    @Deprecated
    public static double Phi(double z) {
        return cdf(z);
    }

    // return Phi(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    @Deprecated
    public static double Phi(double z, double mu, double sigma) {
        return cdf(z, mu, sigma);
    }

    // Compute z such that Phi(z) = y via bisection search
    @Deprecated
    public static double PhiInverse(double y) {
        return inverseCDF(y);
    }

}