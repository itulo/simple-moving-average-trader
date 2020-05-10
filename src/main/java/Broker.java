import CoinbaseObjects.Account;
import CoinbaseObjects.OrderBook;
import CoinbaseObjects.Order;
import CoinbaseObjects.Product;
import conf.BrokerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * A simple broker that manages one order at the time
 */
public class Broker {
  private static final Logger logger = LogManager.getLogger(Broker.class);

  private static final String ORDER_PENDING = "pending";
  private static final String ORDER_DONE = "done";
  private static final String ORDER_OPEN = "open";
  private static final String BUY = "buy";
  private static final String SELL = "sell";

  private Optional<Order> order = Optional.empty();
  private Product tradingPairDetails;
  private CoinbaseClient client;
  private BrokerConfiguration brokerConf;

  public Broker(BrokerConfiguration brokerConf, CoinbaseClient client, String tradingPair) {
    this.brokerConf = brokerConf;
    this.client = client;
    this.tradingPairDetails = client.getProduct(tradingPair);
  }

  void setOrder(Order order) {
    this.order = Optional.of(order);
  }

  /**
   * Get the order book with the latest bid and ask, and return the average over them as price.
   * If cannot get the order book, return lastClosePrice
   * @param tradingPair
   * @param lastClosePrice: close price of the most recent candle
   * @return
   */
  private double getPrice(String tradingPair, double lastClosePrice) {
    final double price;

    OrderBook orderBook = client.getOrderBook(tradingPair);
    if (orderBook != null) {
      price = (orderBook.getBid().getPrice() + orderBook.getAsk().getPrice()) / 2;
    } else {
      price = lastClosePrice;
    }

    return new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  /**
   * Computes the size of the order based on the balance in your invest portfolio and the amount you want to invest.
   *
   * @param price
   * @return
   */
  private double getSize(double price) {
    // get the balance in my portfolio
    List<Account> accounts = client.getAccounts();
    Account eur = accounts.stream()
            .filter(a -> brokerConf.getTradingCurrency().equalsIgnoreCase(a.getCurrency()))
            .findFirst()
            .orElse(null);

    double size = (eur.getAvailable() * brokerConf.getPortfolioInvest()) / price;
    size = Math.min(size, tradingPairDetails.getBase_max_size());
    size = Math.max(size, tradingPairDetails.getBase_min_size());
    
    return new BigDecimal(size).setScale(brokerConf.getProductScale(), RoundingMode.HALF_UP).doubleValue();
  }

  /**
   * If order is in pending status, check the status again
   */
  public void checkPendingOrder() {
    if (order.isPresent() && (ORDER_PENDING.equals(order.get().getStatus()) || ORDER_OPEN.equals(order.get().getStatus()))) {
      Optional<Order> orderResponse = client.getOrder(order.get().getId());
      if (orderResponse.isPresent() && ORDER_DONE.equals(orderResponse.get().getStatus())) {
        logger.info("Order done: {}", orderResponse.get());
      } else {
        logger.info("Order not done yet: {}", orderResponse.get());
      }
      order = orderResponse;
    }
  }

  /**
   * Make a buy order only if we don't have a position yet
   *
   * @param tradingPair
   * @param lastClosePrice
   * @return
   */
  public void buy(String tradingPair, double lastClosePrice) {
    if (!order.isPresent() || (order.isPresent() && SELL.equalsIgnoreCase(order.get().getSide()))) {
      double price = getPrice(tradingPair, lastClosePrice);
      double size = getSize(price);

      logger.info("Sending order buy, size {}, at price {}", size, price);
      Optional<Order> orderResponse = client.postOrder(BUY, tradingPair, size, price);
      if (orderResponse.isPresent()) {
        order = orderResponse;
        logger.info(orderResponse.get());
      }
    }
  }

  /**
   * Make a sell order for the whole position
   * If order is pending or open, sell what has been bought and cancel the rest
   *
   * @param tradingPair
   * @param lastClosePrice
   */
  public void sell(String tradingPair, double lastClosePrice) {
    if (order.isPresent() && BUY.equalsIgnoreCase(order.get().getSide())) {
      Order currentOrder = order.get();
      if(ORDER_PENDING.equals(currentOrder.getStatus())){
        // if order still pending then cancel it
        logger.info("Order still pending, cancelling now");
        client.cancelOrder(currentOrder.getId());
        order = Optional.empty();
      } else {
        final double price = getPrice(tradingPair, lastClosePrice);
        double size = currentOrder.getFilled_size();
        logger.info("Sending order sell, size {}, at price {}", size, price);
        Optional<Order> orderResponse = client.postOrder(SELL, tradingPair, size, price);
        order = orderResponse;
        logger.info(orderResponse.get());

        if (currentOrder.getFilled_size() != currentOrder.getSize()){
          // if order still open (not filled completely) then cancel it
          logger.info("Order still open, cancelling now");
          client.cancelOrder(currentOrder.getId());
        }
      }
    }
  }
}
