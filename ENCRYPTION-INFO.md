This program uses SSL/TLS encrypted sockets  .
keyStore path and password are read from program arguments as described in README

Those parameters can be setup in code using    

    System.setProperty("javax.net.ssl.keyStore", "KEY_STORE_PATH");
    System.setProperty("javax.net.ssl.keyStorePassword", "KEY_STORE_PASSWORD");
  
or as VM options  
    -Djavax.net.ssl.keyStorePassword="KEY_STORE_PATH" -Djavax.net.ssl.keyStore="KEY_STORE_PASSWORD"


   
   
