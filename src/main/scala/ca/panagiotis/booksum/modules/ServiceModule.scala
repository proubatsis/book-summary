package ca.panagiotis.booksum.modules

import ca.panagiotis.booksum.services.google.GoogleBookDataService
import ca.panagiotis.booksum.services.{BookDataService, BookService, UserService}
import ca.panagiotis.booksum.services.quill.{QuillBookService, QuillUserService}
import com.twitter.inject.TwitterModule
import com.twitter.util.Future

/**
  * Created by panagiotis on 22/04/17.
  */
object ServiceModule extends TwitterModule {
  override val modules = Seq(QuillModule)

  override def configure(): Unit = {
    bind[BookService].to[QuillBookService]
    bind[BookDataService].to[GoogleBookDataService]
    bind[UserService].to[QuillUserService]
  }
}
