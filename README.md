
## TCP/IP Server implementation to get the quoted price

This server is accepting the multiple clients and handling the quote requests as mentioned below
1. creating a socket read thread for each client
2. Linked blocking queue is used to communication between Socket reader and Socket writer thread
3. Accepted message format is changed from client
       client need to send below format of message
       1234_B_2000SOH
       where 1234 - securityId
             B for BUY indicator S for SELL indicator
             2000 - quantity
             SOH - ascii value of int 1 (which is treated as end of message)
     Note: no other format is supported.
     
 4. QuoteProcessor is processing all request in single thread.
 
 ########################
 Assumption
 ########################
 The business requirement was not much clear (before I wanted to discuss for all business requirement), 
 later I though to do dummy implementation as per my current understanding.
 
 1. Not sure about the calculation of quote price with this available information so did dummy implementation
 2. Not sure about how to get bid/ask prices - so implemented in a way to external party can set price
 3. Need more clarification on part of implementation of getting the market prices and calculation of quote price.
 4. I change the quote request format to make minimum length for transmitted data over network - but not sure about this flexibility
    assume that I am free to guide client about the accepted format hence I change it.
    
 
 #####################
 Testing
 #####################
 Two test classes are created
 
 1. MarketMakerQPTest.java :  setting the reference quote price with the help of ReferencePriceSource.java implementation
    and validating same 
 2. MarketMakerServerTest.java: here creating the test method to tests multiple clients connection and able to send quote request to Server.
