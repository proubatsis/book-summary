package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.Book
import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 22/04/17.
  */

@Mustache("home")
case class HomeView(title: String, books: Seq[BookItemView], req: Request) extends PageView(req)
