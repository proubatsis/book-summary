package ca.panagiotis.booksum.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.inject.TwitterModule
import com.typesafe.config.ConfigValueFactory
import io.getquill.util.LoadConfig
import io.getquill.{FinaglePostgresContext, SnakeCase}

/**
  * Created by panagiotis on 22/04/17.
  */
object QuillModule extends TwitterModule {
  private val baseConfig = LoadConfig("ctx")
  private val dbName = sys.env.getOrElse("BOOKSUM_DB", baseConfig.getString("database"))
  private val dbHost = sys.env.getOrElse("BOOKSUM_DB_HOST", baseConfig.getString("host"))
  private val dbUser = sys.env.getOrElse("BOOKSUM_DB_USER", baseConfig.getString("user"))
  private val dbPassword = sys.env.getOrElse("BOOKSUM_DB_PASSWORD", baseConfig.getString("password"))
  lazy val fpctx = new FinaglePostgresContext[SnakeCase](
    baseConfig
      .withValue("host", ConfigValueFactory.fromAnyRef(dbHost))
      .withValue("user", ConfigValueFactory.fromAnyRef(dbUser))
      .withValue("password", ConfigValueFactory.fromAnyRef(dbPassword))
      .withValue("database", ConfigValueFactory.fromAnyRef(dbName))
  )


  @Singleton
  @Provides
  def provideQuillContext(): FinaglePostgresContext[SnakeCase] = {
    fpctx
  }
}
