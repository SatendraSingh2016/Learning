
## TCP/IP Server implementation to get the quoted price

This server is accepting the multiple clients and handling the quore requests as mentioned below
1. creating a socket read thread for each clinet
2. Linked blocking queue is used to communication between Socket reader and Socket writer thread
3. Accepted message format is changed from clinet
       clinet need to send below format of message
       1234_B_2000SOH
       where 1234 - securityId
             B for BUY indicator S for SELL indicator
             2000 - quantity
             SOH - ascii value of int 1 (which is treated as end of message)
     Note: no other format is suppotred.
     
 4. QuoteProcessor is processing all request in sindle thread.
 
 ########################
 Assumtion
 ########################
 The business requirement was not much clear (before I wanted to discuss for all business requirement), 
 later I though to do dummy implementation as per my current understanding.
 
 1. Not sure about the calculation of quote price with this available information so did dummy implementation
 2. Not sure about how to get bid/ask prices - so implemented in a way to external pary can set price
 3. Need more clearification on part of implementation of geting the market prices and calculation of quote price.
 4. I change the quote request format to make minum length for transimitted data over network - but not sure about this flexibility
    asume that I ma free to guide client about format.
    
 
 #####################
 Testing
 #####################
 Two test classes are created
 
 1. MarketMakerQPTest.java :  setting the reference quote price with the help of ReferencePriceSource.java implementation
    and validating same 
 2. MarketMakerServerTest.java: here creating the test method to tests multiple clients connection and able to send quote request to Server.
