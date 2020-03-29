package CoinbaseObjects;

/*
{
   "id":"0b85daad-4a83-4dd8-bcb2-98755efa1cba",
   "currency":"ZRX",
   "balance":"147.0588200000000000",
   "available":"147.05882",
   "hold":"0.0000000000000000",
   "profile_id":"5b32e63e-5965-49d7-a9bc-4da1b0f2c296",
   "trading_enabled":true
}
 */
public class Account {
  private String id;
  private String currency;
  private double balance;
  private double available;
  private double hold;
  private String profile_id;
  private boolean trading_enabled;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public double getAvailable() {
    return available;
  }

  public void setAvailable(double available) {
    this.available = available;
  }

  public double getHold() {
    return hold;
  }

  public void setHold(double hold) {
    this.hold = hold;
  }

  public String getProfile_id() {
    return profile_id;
  }

  public void setProfile_id(String profileId) {
    this.profile_id = profileId;
  }

  public boolean isTrading_enabled() {
    return trading_enabled;
  }

  public void setTrading_enabled(boolean tradingEnabled) {
    this.trading_enabled = tradingEnabled;
  }
}
