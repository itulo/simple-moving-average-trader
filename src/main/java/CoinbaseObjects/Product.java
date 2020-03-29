package CoinbaseObjects;

/*
{
   "id":"BTC-EUR",
   "base_currency":"BTC",
   "quote_currency":"EUR",
   "base_min_size":"0.00100000",
   "base_max_size":"200.00000000",
   "quote_increment":"0.01000000",
   "base_increment":"0.00000001",
   "display_name":"BTC/EUR",
   "min_market_funds":"10",
   "max_market_funds":"600000",
   "margin_enabled":false,
   "post_only":false,
   "limit_only":false,
   "cancel_only":false,
   "status":"online",
   "status_message":""
}
 */
public class Product {
  private String id;
  private double base_min_size;
  private double base_max_size;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getBase_min_size() {
    return base_min_size;
  }

  public void setBase_min_size(double base_min_size) {
    this.base_min_size = base_min_size;
  }

  public double getBase_max_size() {
    return base_max_size;
  }

  public void setBase_max_size(double base_max_size) {
    this.base_max_size = base_max_size;
  }
}
