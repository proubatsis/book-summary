package ca.panagiotis.booksum.exceptions

/**
  * Created by panagiotis on 16/05/17.
  */
case class BookNotFoundException(message: String) extends Exception(message)
