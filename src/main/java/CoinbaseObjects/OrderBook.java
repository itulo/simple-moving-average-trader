package CoinbaseObjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = OrderBookDeserializer.class)
public class OrderBook {
  private OrderBookDetails bid;
  private OrderBookDetails ask;

  public OrderBookDetails getBid() {
    return bid;
  }

  public void setBid(OrderBookDetails bid) {
    this.bid = bid;
  }

  public OrderBookDetails getAsk() {
    return ask;
  }

  public void setAsk(OrderBookDetails ask) {
    this.ask = ask;
  }
}
