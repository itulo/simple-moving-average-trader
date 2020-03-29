import CoinbaseObjects.Candle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CandleUtil {
  private static final Logger logger = LogManager.getLogger(CandleUtil.class);

  public static List<Candle> getHeikinAshiCandles(List<Candle> candles) {
    List<Candle> HACandles = new ArrayList<>();

    for (int i = 0; i < candles.size() - 1; i++) {
      Candle HAcandle = getHeikinAshiCandle(candles.get(i), candles.get(i + 1));

      HACandles.add(HAcandle);
    }

    return HACandles;
  }

  public static Candle getHeikinAshiCandle(Candle current, Candle previous) {
    Candle HACandle = new Candle();

    HACandle.setTime(current.getTime());
    HACandle.setOpen((previous.getOpen() + previous.getClose()) / 2);
    HACandle.setClose((current.getOpen() + current.getHigh() + current.getLow() + current.getClose()) / 4);
    HACandle.setHigh(Math.max(Math.max(current.getHigh(), current.getOpen()), current.getClose()));
    HACandle.setLow(Math.max(Math.max(current.getLow(), current.getOpen()), current.getClose()));

    logger.debug("Heikin Ashi candle of: \n{} \n{} is \n{}", current, previous, HACandle);

    return HACandle;
  }

  public static double getMovingAverage(List<Candle> candles, BigDecimal movingAverage) {
    double sum = candles.stream()
            .limit(movingAverage.intValue())
            .map(Objects::requireNonNull)
            .map(Candle::getClose)
            .reduce(0.0, Double::sum);

    double ma = sum / movingAverage.longValue();

    logger.debug("MA10: {}", ma);

    return ma;
  }
}
