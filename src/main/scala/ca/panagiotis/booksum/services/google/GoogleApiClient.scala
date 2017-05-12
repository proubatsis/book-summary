package ca.panagiotis.booksum.services.google

import com.twitter.finagle.Http
import com.twitter.finagle.http.Request

/**
  * Created by panagiotis on 09/05/17.
  */
object GoogleApiClient {
  val client = Http.client.withTls("www.googleapis.com").newService("www.googleapis.com:443")
  def call(endpoint: String, query: (String, String)) = {
    client(Request(endpoint, query))
  }
}
