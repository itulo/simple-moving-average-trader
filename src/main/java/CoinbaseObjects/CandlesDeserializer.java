package CoinbaseObjects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CandlesDeserializer extends StdDeserializer<Candles> {

  // needed for deserialization
  public CandlesDeserializer() {
    this(null);
  }

  public CandlesDeserializer(Class<?> vc) {
    super(vc);
  }

  private List<Candle> deserializeCandles(JsonNode nodes) {
    List<Candle> candlesList = new ArrayList<>();

    for (JsonNode node : nodes) {
      Candle candle = new Candle();
      // [ time, low, high, open, close, volume]
      candle.setTime(node.get(0).asLong());
      candle.setLow(node.get(1).asDouble());
      candle.setHigh(node.get(2).asDouble());
      candle.setOpen(node.get(3).asDouble());
      candle.setClose(node.get(4).asDouble());
      candle.setVolume(node.get(5).asDouble());

      candlesList.add(candle);
    }

    return candlesList;
  }

  @Override
  public Candles deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    List<Candle> candleList = deserializeCandles(node);

    Candles candles = new Candles();
    candles.setCandles(candleList);

//    JavaType customClassCollection = objectMapper.getTypeFactory().constructCollectionType(List.class, CustomClass.class);
//    List<CustomClass> beanList = (List<CustomClass>)objectMapper.readValue(stringBean, customClassCollection);
    return candles;
  }
}
