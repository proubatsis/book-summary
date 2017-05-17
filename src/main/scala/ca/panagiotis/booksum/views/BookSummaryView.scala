package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.BookSummary
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 23/04/17.
  */

@Mustache("book")
case class BookSummaryView(title: String, author: String, description: String, summaries: List[BookSummary])
