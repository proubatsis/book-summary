package ca.panagiotis.booksum.services.quill

import javax.inject.Inject

import ca.panagiotis.booksum.models.Book
import ca.panagiotis.booksum.services.BookService
import com.twitter.util.Future
import io.getquill.{FinaglePostgresContext, SnakeCase}

/**
  * Created by panagiotis on 22/04/17.
  */
class QuillBookService @Inject() (ctx: FinaglePostgresContext[SnakeCase]) extends BookService {
  import ctx._

  override def findBooks(): Future[Seq[Book]] = {
    ctx.run(query[Book])
  }
}
