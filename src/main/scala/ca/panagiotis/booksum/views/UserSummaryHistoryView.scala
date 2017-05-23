package ca.panagiotis.booksum.views

import java.text.SimpleDateFormat

import ca.panagiotis.booksum.models.{Book, BookSummary}
import ca.panagiotis.booksum.util.Endpoint
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 21/05/17.
  */
@Mustache("summary_history")
case class UserSummaryHistoryView(username: String, summaries: List[SummaryHistoryViewItem])
case class SummaryHistoryViewItem(date: String, book_title: String, author: String, summary_link: String)

object UserSummaryHistoryView {
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd")

  def fromUserAndSummaries(username: String, booksAndSummaries: List[(Book, BookSummary)]): UserSummaryHistoryView = {
    val items = booksAndSummaries map {
      case (book, summary) => {
        SummaryHistoryViewItem(dateFormat.format(summary.postedDate), book.title, book.author, Endpoint.Book.summary(book.id))
      }
    }

    UserSummaryHistoryView(username, items)
  }
}
