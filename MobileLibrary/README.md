# Demo-YouTuBe-Android
---
An android demo about searching and displaying books details using Google book api. You can set search condition. 

----------

# Screenshot #

The search result list.  
![search result list][1]

Set search condition.  
![set search condition][2]

Book detail  
![played by videoview][3]



----------

# Get start  #
In the Search.java file:
 1. Replace the `"YOURAPIKEY"` with your api key .
 2. Replace the `"YOURAPPLICATIONNAME"` with your application name .
 

        public class Search {
    
        public static String apiKey = "YOURAPIKEY";
    
        private static Books books;
    
        static {
            books = new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("YOURAPPLICATIONNAME").build();
        }
    
        ......
    
    }

  [1]: http://7qnau5.com1.z0.glb.clouddn.com/device-2014-12-15-144302.jpg
  [2]: http://7qnau5.com1.z0.glb.clouddn.com/device-2014-12-15-144358.jpg
  [3]: http://7qnau5.com1.z0.glb.clouddn.com/device-2014-12-15-144339.jpg