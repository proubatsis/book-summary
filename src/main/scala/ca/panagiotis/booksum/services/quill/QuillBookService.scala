package ca.panagiotis.booksum.services.quill

import java.util.Date
import javax.inject.Inject

import ca.panagiotis.booksum.models.{Account, Book, BookExternalMapping, BookSummary}
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

  override def findAccountSummaryHistory(accountId: Index): Future[Option[(Account, List[(Book, BookSummary)])]] = {
    val q = quote {
      for {
        account <- query[Account] filter (_.id == lift(accountId))
        summary <- query[BookSummary] join (_.accountId == account.id)
        book <- query[Book] join (_.id == summary.bookId)
      } yield (account, book, summary)
    }

    for {
      result <- ctx.run(q)
      (accounts, books, summaries) = result.unzip3
    } yield {
      accounts.headOption match {
        case Some(account) => Some((account, books zip summaries))
        case None => None
      }
    }
  }

  override def findBook(bookId: Index): Future[Option[Book]] = {
    for {
      book <- ctx.run(query[Book] filter (_.id == lift(bookId)))
    } yield book.headOption
  }

  override def findBookSummaryAccount(bookId: Index): Future[Option[(Book, List[(BookSummary, Account)])]] = {
    val q = quote {
      for {
        b <- query[Book].filter(_.id == lift(bookId)).distinct
        s <- query[BookSummary].join(_.bookId == b.id)
        a <- query[Account].join(_.id == s.accountId)
      } yield (b, s, a)
    }

    for {
      results <- ctx.run(q)
      (books, summaries, accounts) = results.unzip3
      book = books.headOption
    } yield {
      book match {
        case Some(b) => Some((b, summaries zip accounts))
        case None => None
      }
    }
  }

  override def findBookFromExternalId(externalId: String): Future[Option[Book]] = {
    val q = quote {
      for {
        mapping <- query[BookExternalMapping].filter(_.externalId == lift(externalId)).distinct
        book <- query[Book].filter(_.id == mapping.bookId)
      } yield book
    }

    for {
      books <- ctx.run(q)
    } yield books.headOption
  }

  override def search(searchQuery: String, startIndex: Int, maxResults: Int): Future[List[Book]] = {
    val ql = s"%${searchQuery.toLowerCase()}%"

    ctx.run(
      query[Book]
        .filter(b => (b.title.toLowerCase() like lift(ql)) || (b.author.toLowerCase() like lift(ql)) || (b.description.toLowerCase() like lift(ql)))
        .drop(lift(startIndex))
        .take(lift(maxResults))
    )
  }

  override def createBook(title: String, author: String, description: String, image: String): Future[Int] = {
    val book = Book(0, title, author, description, image)
    ctx.run(query[Book].insert(lift(book)).returning(_.id))
  }

  override def createSummary(bookId: Int, accountId: Int, summary: String): Future[Int] = {
    val s = BookSummary(0, accountId, new Date(), bookId, summary)
    ctx.run(query[BookSummary].insert(lift(s)).returning(_.id))
  }

  override def registerExternalMapping(bookId: Int, externalId: String): Future[Int] = {
    val mapping = BookExternalMapping(0, externalId, bookId)
    ctx.run(query[BookExternalMapping].insert(lift(mapping)).returning(_.id))
  }
}
