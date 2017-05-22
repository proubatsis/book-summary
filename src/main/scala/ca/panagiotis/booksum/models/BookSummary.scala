package ca.panagiotis.booksum.models

import java.util.Date

/**
  * Created by panagiotis on 23/04/17.
  */

case class BookSummary(id: Int, accountId: Int, postedDate: Date, bookId: Int, summary: String)
