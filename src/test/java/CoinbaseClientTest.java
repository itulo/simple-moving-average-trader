import CoinbaseObjects.Account;
import CoinbaseObjects.Candle;
import CoinbaseObjects.Order;
import CoinbaseObjects.OrderBook;
import CoinbaseObjects.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import conf.ClientConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoinbaseClientTest {
  private static final String TRADING_PAIR = "BTC-EUR";
  private static CoinbaseClient client;

  @BeforeAll
  static void setUp() {
    /**
     * Go to https://public.sandbox.pro.coinbase.com and generate passphrase, api key and api secret for
     * the sandbox.
     * Add funds to your EUR and BTC wallets
     */
    ClientConfiguration clientConf = new ClientConfiguration();
    clientConf.setUseSandbox(true);
    clientConf.setPassphrase("");
    clientConf.setApiKey("");
    clientConf.setApiSecret("");

    client = new CoinbaseClient(clientConf);
  }

  @Test
  public void getProduct() {
    Product p = client.getProduct(TRADING_PAIR);

    assertEquals(TRADING_PAIR, p.getId());
    assertTrue(p.getBase_max_size() > 0);
    assertTrue(p.getBase_min_size() > 0);
  }

  @Test
  public void getHistoricalRates() throws JsonProcessingException {
    final int candlePeriod = 900; // 45 minute candles
    final int numberOfCandles = 10;

    LocalDateTime end = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MINUTES);
    LocalDateTime start = end.minus(candlePeriod / 60 * numberOfCandles, ChronoUnit.MINUTES);
    List<Candle> candles = client.getHistoricalRates(TRADING_PAIR, start.toString(), end.toString(),
            String.valueOf(candlePeriod))
            .getCandles();

    assertEquals(numberOfCandles, candles.size());
    long candleTimeDifferenceInSeconds = candles.get(0).getTime() - candles.get(1).getTime();
    assertEquals(candlePeriod, candleTimeDifferenceInSeconds);
  }

  @Test
  public void getOrderBook() {
    OrderBook ob = client.getOrderBook(TRADING_PAIR);

    assertNotNull(ob.getAsk());
    assertNotNull(ob.getBid());
  }

  @Test
  public void getAccounts() {
    List<Account> accounts = client.getAccounts();

    assertTrue(accounts.size() > 0);
  }

  @Test
  public void postOrderAndGetOrder() {
    final String action = "sell";
    final double size = 1;
    final double price = 100000;
    Optional<Order> o = client.postOrder(action, TRADING_PAIR, size, price);

    Order order = o.get();
    assertEquals(action, order.getSide());
    assertEquals(size, order.getSize());
    assertEquals(price, order.getPrice());
    assertEquals(TRADING_PAIR, order.getProduct_id());
    assertEquals("pending", order.getStatus());

    Order checkOrder = client.getOrder(order.getId()).get();
    assertEquals(order.getId(), order.getId());
    assertEquals(order.getSide(), checkOrder.getSide());
    assertEquals(order.getSize(), checkOrder.getSize());
    assertEquals(order.getPrice(), checkOrder.getPrice());
    assertEquals(order.getProduct_id(), checkOrder.getProduct_id());
  }
}
