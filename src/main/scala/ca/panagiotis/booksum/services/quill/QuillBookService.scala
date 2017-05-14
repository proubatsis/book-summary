package ca.panagiotis.booksum.services.quill

import javax.inject.Inject

import ca.panagiotis.booksum.models.{Book, BookSummary}
import ca.panagiotis.booksum.services.BookService
import com.twitter.util.Future
import io.getquill.{FinaglePostgresContext, SnakeCase}

/**
  * Created by panagiotis on 22/04/17.
  */
class QuillBookService @Inject() (ctx: FinaglePostgresContext[SnakeCase]) extends BookService {
  import ctx._

  override def findBooks(): Future[List[Book]] = {
    ctx.run(query[Book])
  }

  override def findBook(bookId: Index) = {
    for {
      book <- ctx.run(query[Book] filter (_.id == lift(bookId)))
    } yield book.headOption
  }

  override def findBookSummary(bookId: Index) = {
    val q = quote {
      for {
        b <- query[Book].filter(_.id == lift(bookId)).distinct
        s <- query[BookSummary].join(_.bookId == b.id)
      } yield (b, s)
    }

    for {
      results <- ctx.run(q)
      (books, summaries) = results.unzip
      book = books.headOption
    } yield {
      book match {
        case Some(b) => Some((b, summaries))
        case None => None
      }
    }
  }
}
