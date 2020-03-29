import CoinbaseObjects.Account;
import CoinbaseObjects.Candles;
import CoinbaseObjects.OrderRequest;
import CoinbaseObjects.OrderBook;
import CoinbaseObjects.Order;
import CoinbaseObjects.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import conf.ClientConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.exit;

public class CoinbaseClient {

  private static final String BASE_URL = "https://api.pro.coinbase.com";
  private static final String SANDBOX_URL = "https://api-public.sandbox.pro.coinbase.com";

  private static final Logger logger = LogManager.getLogger(CoinbaseClient.class);

  private String baseUrl;
  private String passphrase;
  private String apiKey;
  private String apiSecret;
  private Client client;
  private ObjectMapper mapper;

  public CoinbaseClient(ClientConfiguration clientConf) {
    if (clientConf.isUseSandbox()){
      baseUrl = SANDBOX_URL;
    } else {
      baseUrl = BASE_URL;
    }
    passphrase = clientConf.getPassphrase();
    apiKey = clientConf.getApiKey();
    apiSecret = clientConf.getApiSecret();
    client = ClientBuilder.newClient();
    mapper = new ObjectMapper();
  }

  //https://docs.pro.coinbase.com/#creating-a-request
  private String signMessage(long unixTime, String method, String requestPath, String body) {
    String message = unixTime + method + requestPath + body;

    Mac sha256HMAC = null;
    try {
      // Remember to first base64-decode the alphanumeric secret string ...
      byte[] decodedSecret = Base64.getDecoder().decode(apiSecret);
      sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(decodedSecret, "HmacSHA256");
      sha256HMAC.init(secretKey);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      // these should *never* occur
      logger.error(e);
      exit(0);
    }

    return Base64.getEncoder().encodeToString(sha256HMAC.doFinal(message.getBytes()));
  }

  private MultivaluedMap<String, Object> addCoinbaseHeader(String method, String requestPath, String body) {
    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    long unixTime = System.currentTimeMillis() / 1000L;

    headers.add("CB-ACCESS-KEY", apiKey);
    headers.add("CB-ACCESS-SIGN", signMessage(unixTime, method, requestPath, body));
    headers.add("CB-ACCESS-TIMESTAMP", unixTime);
    headers.add("CB-ACCESS-PASSPHRASE", passphrase);

    return headers;
  }

  private Response get(String requestPath) {
    final WebTarget target = client.target(baseUrl).path(requestPath);

    Response response = target.request().headers(addCoinbaseHeader("GET", requestPath, "")).get();

    if (response.getStatus() != Response.Status.OK.getStatusCode()) {
      logger.warn("Call to {} received response that was not 200 but was {}", requestPath,
              response.getStatus());
      System.out.println("Call to " + requestPath + " received response that was not 200 but was " + response.getStatus());
    }

    return response;
  }

  public Product getProduct(String productId) {
    final String requestPath = "/products/" + productId;
    return get(requestPath).readEntity(Product.class);
  }

  // https://docs.pro.coinbase.com/#get-historic-rates
  public Candles getHistoricalRates(String productId, String startDate, String endDate,
                                    String candlePeriod) throws JsonProcessingException {
    final String requestPath = "/products/" + productId + "/candles";

    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("start", URLEncoder.encode(startDate, StandardCharsets.UTF_8));
    queryParams.put("end", URLEncoder.encode(endDate, StandardCharsets.UTF_8));
    queryParams.put("granularity", candlePeriod);

    String candlesString =
            client.target(baseUrl).path(requestPath)
                    .queryParam("granularity", candlePeriod)
                    .queryParam("start", startDate)
                    .queryParam("end", endDate)
                    .request().get(String.class);

    return mapper.readValue(candlesString, Candles.class);
  }

  // https://docs.pro.coinbase.com/#get-product-order-book
  public OrderBook getOrderBook(String productId) {
    final String requestPath = "/products/" + productId + "/book";

    String orderBookString = client.target(baseUrl).path(requestPath).request().get(String.class);

    try {
      return mapper.readValue(orderBookString, OrderBook.class);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  // https://docs.pro.coinbase.com/#accounts
  public List<Account> getAccounts() {
    final String requestPath = "/accounts";
    Response response = get(requestPath);
    return response.readEntity(new GenericType<>() {
    });
  }

  // https://docs.pro.coinbase.com/#place-a-new-order
  public Optional<Order> postOrder(String action, String productId, double size, double price) {
    final String requestPath = "/orders";
    final OrderRequest o = new OrderRequest(action, productId, size, price);

    Response response;
    try {
      String body = mapper.writeValueAsString(o);
      WebTarget target = client.target(baseUrl);
      response = target.path(requestPath).request().headers(addCoinbaseHeader("POST", requestPath,
              body)).post(Entity.entity(body, MediaType.APPLICATION_JSON));

    } catch (JsonProcessingException e) {
      return Optional.empty();
    }

    if (response.getStatus() != Response.Status.OK.getStatusCode()) {
      logger.error("postOrder response was not 200 but was {}\n {}", response.getStatus(),
              response.readEntity(String.class));
      return Optional.empty();
    }

    return Optional.of(response.readEntity(Order.class));
  }

  // https://docs.pro.coinbase.com/#get-an-order
  public Order getOrder(String orderId) {
    final String requestPath = "/orders/" + orderId;

    Response response = get(requestPath);

    return response.readEntity(Order.class);
  }

}
