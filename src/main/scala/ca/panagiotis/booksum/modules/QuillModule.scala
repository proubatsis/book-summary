package ca.panagiotis.booksum.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import io.getquill.{FinaglePostgresContext, SnakeCase}

/**
  * Created by panagiotis on 22/04/17.
  */
object QuillModule extends TwitterModule {
  lazy val fpctx = new FinaglePostgresContext[SnakeCase]("ctx")

  @Singleton
  @Provides
  def provideQuillContext(): FinaglePostgresContext[SnakeCase] = {
    fpctx
  }
}
