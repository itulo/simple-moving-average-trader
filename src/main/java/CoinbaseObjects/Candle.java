package CoinbaseObjects;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Candle {
  public enum Color {
    GREEN, RED
  }

  private long time;
  private double low;
  private double high;
  private double open;
  private double close;
  private double volume;

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public Color getColor() {
    return (this.close >= this.open) ? Color.GREEN : Color.RED;
  }

  public String toString() {
    return "time: " + LocalDateTime.ofEpochSecond(this.time, 0, ZoneOffset.UTC) +
            " open: " + this.open +
            "\tclose: " + this.close +
            "\thigh: " + this.high +
            "\tlow: " + this.low +
            "\tcolor: " + this.getColor();
  }
}
