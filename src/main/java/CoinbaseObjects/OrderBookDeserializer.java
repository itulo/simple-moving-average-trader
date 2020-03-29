package CoinbaseObjects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class OrderBookDeserializer extends StdDeserializer<OrderBook> {

  // needed for deserialization
  public OrderBookDeserializer() {
    this(null);
  }

  public OrderBookDeserializer(Class<?> vc) {
    super(vc);
  }

  private OrderBook deserializeCandles(JsonNode nodes) {
    OrderBook orderBook = new OrderBook();

    JsonNode buyNode = nodes.get("bids").get(0);
    OrderBookDetails bid = new OrderBookDetails();
    bid.setPrice(buyNode.get(0).asDouble());
    bid.setSize(buyNode.get(1).asDouble());
    bid.setNumOrders(buyNode.get(2).asDouble());
    orderBook.setBid(bid);

    JsonNode askNode = nodes.get("asks").get(0);
    OrderBookDetails ask = new OrderBookDetails();
    ask.setPrice(askNode.get(0).asDouble());
    ask.setSize(askNode.get(1).asDouble());
    ask.setNumOrders(askNode.get(2).asDouble());
    orderBook.setAsk(ask);

    return orderBook;
  }

  @Override
  public OrderBook deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    return deserializeCandles(node);
  }
}
