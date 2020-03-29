package conf;

public class BrokerConfiguration {

  private String tradingCurrency;
  private int productScale;
  private double portfolioInvest;

  public String getTradingCurrency() {
    return tradingCurrency;
  }

  public int getProductScale() {
    return productScale;
  }

  public double getPortfolioInvest() {
    return portfolioInvest;
  }

  @Override
  public String toString() {
    return "Trading currency: " + tradingCurrency +
            " Product scale: " + productScale +
            " Portfolio Invest: " + portfolioInvest;
  }
}
