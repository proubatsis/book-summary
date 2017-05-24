package ca.panagiotis.booksum.views

import java.text.SimpleDateFormat

import ca.panagiotis.booksum.models.{Account, Book, BookSummary}
import ca.panagiotis.booksum.util.Endpoint
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 23/04/17.
  */

@Mustache("book")
case class BookSummaryView(title: String, author: String, description: String, summaries: List[BookSummaryItemView])
case class BookSummaryItemView(summary: String, posted_date: String, username: String, account_history_url: String)

object BookSummaryView {
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def create(book: Book, summaryAccountList: List[(BookSummary, Account)]): BookSummaryView = {
    val items = summaryAccountList map {
      case (s, a) => BookSummaryItemView(s.summary, dateFormat.format(s.postedDate), a.username, Endpoint.Account.summaryHistory(a.id))
    }

    BookSummaryView(book.title, book.author, book.description, items)
  }
}
