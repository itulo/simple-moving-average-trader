package conf;

import java.math.BigDecimal;

public class WatcherConfiguration {

  private int candlePeriod;
  private BigDecimal movingAverageWindow;
  private String tradingPair;

  public int getCandlePeriod() {
    return candlePeriod;
  }

  public BigDecimal getMovingAverageWindow() {
    return movingAverageWindow;
  }

  public String getTradingPair() {
    return tradingPair;
  }

  @Override
  public String toString() {
    return "Candle period (s): " + candlePeriod +
            " Moving avg window: " + movingAverageWindow +
            " Trading pair: " + tradingPair;
  }
}
