# Trader on Coinbase exchange
This codebase implements the Simple Moving Average Strategy with Heikin Ashi candles described by Mr. Anderson in a [series of tweets](https://twitter.com/TrueCrypto28/status/1012359287706996736): from the daily chart, get Heikin Ashi candles and then compute the 10 moving average. If the current candle is green and rises above the 10MA, then take a long position; exit when the candle becomes red. If the strategy has a name please let me know.

This trader enters a long position by buying a cryptocurrency on Coinbase, and exits by selling the whole position.
It does not take short trades.

**Use it at your own risk. And do not invest money you cannot afford to lose**.

## Getting started
- [Create an account in coinbase](https://www.coinbase.com/join/arment_35) if you haven't already
- Get API keys with View and Trade permissions and copy them into the configuration file *trader.yaml*  
    - you can test whether the coinbase client works by running *CoinbaseClientTest.java*. Because the test uses Coinbase's sandbox, you need to create API keys in their [sandbox](https://public.sandbox.pro.coinbase.com) and copy them directly inside the class.
- Build with
 `mvn clean package`
 - Run with `java -jar target/trader-1.0-jar-with-dependencies.jar` (the configuration file must be in the same folder where you execute this command)

You can run the trader in test mode (with coinbase client using the sandbox) by setting it into the configuration file.

## Functionality
Logs are visible in the console and also saved to trader.log.
### Main.java
Runs the CandleWatcher 3 times during the candle period you set in the configuration file (if your candle period is 24h, it will run the CandleWatcher every 8 hours).
### CandleWatcher.java
Checks if a pending order has been executed.

Gets candles from coinbase API, then computes Heikin Ashi candles and the 10 window moving average on these. It makes a decision whether to buy or sell based on the last HA candle and the 10MA.
### Broker.java
Executes buy/sell orders that come from the CandleWatcher, and checks whether they have been fulfilled.

A few caveats:
- in order to determine the buy/sell price for an order, it checks the current order book and takes the average between the latest ask and bid. (if you know of a better way let me know)
- the size of a buy order is computed based on how much of your money you want to invest and the price. If the computed size is too small or too big, then the minimum or maximum size for the trading pair is used.
- it holds only one order at the time

## Donations
If the trader made you money remember about me and donate. If the trader lost your money donate so I can improve it.

Paypal: italo dot armenti at gmail dot com

BTC: bc1qg3qq885ztne4xaltqajv6zzh9ctq0m95ax6g5c

ETH: 0x4C6B3Fe9F7A790b74febc1cB6D1D7269eAcd96c1
