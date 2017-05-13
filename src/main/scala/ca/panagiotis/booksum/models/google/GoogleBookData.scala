package ca.panagiotis.booksum.models.google

/**
  * Created by panagiotis on 11/05/17.
  */
case class GoogleBookData(id: String, volumeInfo: GoogleBookDataVolumeInfo)

case class GoogleBookDataVolumeInfo(
                                     title: String,
                                     description: String,
                                     authors: Option[List[String]],
                                     imageLinks: GoogleBookDataImageLinks,
                                     industryIdentifiers: List[GoogleBookDataIsbn]
                                   )

case class GoogleBookDataImageLinks(thumbnail: Option[String], small: Option[String], medium: Option[String], large: Option[String])
case class GoogleBookDataIsbn(identifier: String)
case class GoogleBookDataSearchResults(totalItems: Int, items: List[GoogleBookData])
