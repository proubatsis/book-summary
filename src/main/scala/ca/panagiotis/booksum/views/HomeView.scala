package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.Book
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 22/04/17.
  */

@Mustache("home")
case class HomeView(title: String, books: Seq[Book])
