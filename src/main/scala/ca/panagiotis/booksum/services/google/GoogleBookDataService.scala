package ca.panagiotis.booksum.services.google

import ca.panagiotis.booksum.exceptions.IsbnException
import ca.panagiotis.booksum.models.BookData
import ca.panagiotis.booksum.models.google.{GoogleBookData, GoogleBookDataSearchResults}
import ca.panagiotis.booksum.services.BookDataService
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
  * Created by panagiotis on 11/05/17.
  */
class GoogleBookDataService extends BookDataService{
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  override def getBook(id: String) = {
    for {
      res <- GoogleApiClient.call("/books/v1/volumes/" + id, ("",""))
      gbd = mapper.readValue[GoogleBookData](res.contentString)
    } yield convertToBookData(gbd)
  }

  override def searchBook(query: String) = {
    for {
      res <- GoogleApiClient.call("/books/v1/volumes", ("q", query))
      books = mapper.readValue[GoogleBookDataSearchResults](res.contentString)
    } yield books.items.map(convertToBookData)
  }

  private def convertToBookData(b: GoogleBookData): BookData = {
    println(s"${b.id}: ${b.volumeInfo.title}")
    b.volumeInfo.industryIdentifiers find (_.identifier.length() == 13) match {
      case Some(x) =>
        BookData(
          b.id,
          b.volumeInfo.title,
          b.volumeInfo.authors.mkString(","),
          b.volumeInfo.description,
          b.volumeInfo.imageLinks.medium,
          x.identifier
        )
      case _ => throw IsbnException("ISBN-13 not found!")
    }
  }
}
