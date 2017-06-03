package ca.panagiotis.booksum.servers

import ca.panagiotis.booksum.controllers.{BookController, ContentController, HomeController, UserController}
import ca.panagiotis.booksum.filters.{ContentFilter, UserFilter}
import ca.panagiotis.booksum.modules.ServiceModule
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object BookSumServerMain extends BookSumServer

/**
  * Created by panagiotis on 22/04/17.
  */
class BookSumServer extends HttpServer {
  override val modules = Seq(ServiceModule)

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .filter[UserFilter]
      .add[HomeController]
      .add[BookController]
      .add[UserController]
      .add[ContentFilter, ContentController]
  }
}
