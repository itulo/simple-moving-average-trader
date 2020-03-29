package CoinbaseObjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = CandlesDeserializer.class)
public class Candles {
  private List<Candle> candles;

  public List<Candle> getCandles() {
    return candles;
  }

  public void setCandles(List<Candle> candles) {
    this.candles = candles;
  }
}
