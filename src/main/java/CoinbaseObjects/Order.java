package CoinbaseObjects;

/*{
    "id": "68e6a28f-ae28-4788-8d4f-5ab4e5e5ae08",
    "size": "1.00000000",
    "product_id": "BTC-USD",
    "side": "buy",
    "stp": "dc",
    "funds": "9.9750623400000000",
    "specified_funds": "10.0000000000000000",
    "type": "market",
    "post_only": false,
    "created_at": "2016-12-08T20:09:05.508883Z",
    "done_at": "2016-12-08T20:09:05.527Z",
    "done_reason": "filled",
    "fill_fees": "0.0249376391550000",
    "filled_size": "0.01291771",
    "executed_value": "9.9750556620000000",
    "status": "done",
    "settled": true
}*/
/*
{
        "id":"574408ff-33f1-41b8-8de5-6741347e4fbf",
        "price":"6000",
        "size":"0.01",
        "product_id":"BTC-EUR",
        "side":"buy",
        "stp":"dc",
        "type":"limit",
        "time_in_force":"GTC",
        "post_only":false,
        "created_at":"2020-03-07T19:32:09.370689Z",
        "fill_fees":"0",
        "filled_size":"0",
        "executed_value":"0",
        "status":"pending",
        "settled":false
        }
*/
public class Order {
  private String id;
  private double price;
  private double size;
  private String product_id;
  private String side;
  private String type;
  private String done_reason;
  private double fill_fees; // in the same currency as price
  private double filled_size;
  private double executed_value;
  private String status;
  private boolean settled;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String productId) {
    this.product_id = productId;
  }

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDone_reason() {
    return done_reason;
  }

  public void setDone_reason(String doneReason) {
    this.done_reason = doneReason;
  }

  public double getFill_fees() {
    return fill_fees;
  }

  public void setFill_fees(double fillFees) {
    this.fill_fees = fillFees;
  }

  public double getFilled_size() {
    return filled_size;
  }

  public void setFilled_size(double filledSize) {
    this.filled_size = filledSize;
  }

  public double getExecuted_value() {
    return executed_value;
  }

  public void setExecuted_value(double executedValue) {
    this.executed_value = executedValue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isSettled() {
    return settled;
  }

  public void setSettled(boolean settled) {
    this.settled = settled;
  }

  public String toString() {
    return String.format("Side: %s, size: %s, price: %s, done_reason: %s, fill_fees: %s, filled_size: %s, " +
                    "executed_value: %s, status: %s, Id: %s", side, size, price, done_reason, fill_fees,
            filled_size, executed_value, status, id);
  }
}
