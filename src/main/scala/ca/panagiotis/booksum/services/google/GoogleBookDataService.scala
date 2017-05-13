package ca.panagiotis.booksum.services.google

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
    } yield books.items.flatMap(convertToBookData)
  }

  private def convertToBookData(b: GoogleBookData): Option[BookData] = {
    for {
      isbn <- b.volumeInfo.industryIdentifiers find (_.identifier.length() == 13)
      authors <- b.volumeInfo.authors
      image <- (List(b.volumeInfo.imageLinks.medium, b.volumeInfo.imageLinks.small, b.volumeInfo.imageLinks.thumbnail) filter (_.isDefined)).head
    } yield BookData(
      b.id,
      b.volumeInfo.title,
      authors.mkString(","),
      b.volumeInfo.description,
      image,
      isbn.identifier
    )
  }
}
