package ca.panagiotis.booksum.models

/**
  * Created by panagiotis on 22/04/17.
  */

case class Book(id: Int, isbn: String,  title: String, author: String, description: Option[String])
