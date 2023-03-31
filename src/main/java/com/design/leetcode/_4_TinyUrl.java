package com.design.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * TinyURL is a URL shortening service where you enter a URL such as https://leetcode.com/problems/design-tinyurl and it returns a short URL such as http://tinyurl.com/4e9iAk. Design a class to encode a URL and decode a tiny URL.

There is no restriction on how your encode/decode algorithm should work. You just need to ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded to the original URL.

Implement the Solution class:

Solution() Initializes the object of the system.
String encode(String longUrl) Returns a tiny URL for the given longUrl.
String decode(String shortUrl) Returns the original long URL for the given shortUrl. It is guaranteed that the given shortUrl was encoded by the same object.
 

Example 1:

Input: url = "https://leetcode.com/problems/design-tinyurl"
Output: "https://leetcode.com/problems/design-tinyurl"

Explanation:
Solution obj = new Solution();
string tiny = obj.encode(url); // returns the encoded tiny url.
string ans = obj.decode(tiny); // returns the original url after deconding it.
 

Constraints:

1 <= url.length <= 104
url is guranteed to be a valid URL.
 * 
 * @author sukh
 *
 */
public class _4_TinyUrl {
  
  /**
   * 62^6 unique URLs can be formed
   */

  private static String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static Integer LENGTH = 62;
  private Random rand;
  private String key;
  private static String URL_PREFIX = "http://tinyurl.com/";

  private Map<String, String> encodeDecode;

  public _4_TinyUrl() {
    rand = new Random();
    key = getRandom();
    encodeDecode = new HashMap<>();
  }

  private String getRandom() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      sb.append(CHARS.charAt(rand.nextInt(LENGTH)));
    }
    return sb.toString();
  }

  // Encodes a URL to a shortened URL.
  public String encode(String longUrl) {
    while (encodeDecode.containsKey(key)) {
      key = getRandom();
    }
    encodeDecode.put(key, longUrl);
    return URL_PREFIX + key;
  }

  // Decodes a shortened URL to its original URL.
  public String decode(String shortUrl) {
    return encodeDecode.get(shortUrl.replace(URL_PREFIX, ""));
  }

}
