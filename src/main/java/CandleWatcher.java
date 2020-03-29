import CoinbaseObjects.Candle;
import com.fasterxml.jackson.core.JsonProcessingException;
import conf.WatcherConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CandleWatcher implements Runnable {
  private static final Logger logger = LogManager.getLogger(CandleWatcher.class);
  private static final int SECONDS_MINUTE = 60;

  private CoinbaseClient client;
  private Broker broker;
  private WatcherConfiguration watcherConf;
  private int numberOfCandles;
  private boolean hasPosition = false;

  public CandleWatcher(WatcherConfiguration watcherConf, CoinbaseClient client, Broker broker) {
    this.watcherConf = watcherConf;
    this.numberOfCandles = watcherConf.getMovingAverageWindow().intValue() + 1;
    this.client = client;
    this.broker = broker;
  }

  public void run() {
    // check if the broker has any pending orders
    broker.checkPendingOrder();

    // get recent candles
    LocalDateTime end = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MINUTES);
    LocalDateTime start = end.minus(watcherConf.getCandlePeriod() / SECONDS_MINUTE * this.numberOfCandles,
            ChronoUnit.MINUTES);
    List<Candle> candles;
    try {
      candles = client.getHistoricalRates(
              watcherConf.getTradingPair(),
              start.toString(),
              end.toString(),
              String.valueOf(watcherConf.getCandlePeriod()))
              .getCandles();
    } catch (JsonProcessingException e) {
      // just return and try again later
      return;
    }

    // convert into HA candles
    List<Candle> HACandles = CandleUtil.getHeikinAshiCandles(candles);

    // get moving average
    double ma = CandleUtil.getMovingAverage(HACandles, watcherConf.getMovingAverageWindow());

    Candle ha = HACandles.get(0);

    if (ha.getColor() == Candle.Color.GREEN && ha.getClose() > ma && !hasPosition) {
      logger.info("Enter position at {}", candles.get(0).getClose());

      broker.buy(watcherConf.getTradingPair(), candles.get(0).getClose());
      hasPosition = true;
    }
    if (hasPosition && ha.getColor() == Candle.Color.RED) {
      logger.info("Exit position at {}", candles.get(0).getClose());

      broker.sell(watcherConf.getTradingPair(), candles.get(0).getClose());
      hasPosition = false;
    }
  }
}
