package ca.panagiotis.booksum.util

import ca.panagiotis.booksum.models.SearchPaginationModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.util.Try

/**
  * Created by panagiotis on 20/05/17.
  */
object Token {
  private val paginationAlgorithm = JwtAlgorithm.HMD5
  private val paginationSecret = "JkjHCi3nw8*#W*(YhIUY#@ueshjD(OI#e"
  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def encodeSearchPagination(searchPaginationModel: SearchPaginationModel): String = {
    val spmJson = mapper.writeValueAsString(searchPaginationModel)
    Jwt.encode(spmJson, paginationSecret, paginationAlgorithm)
  }

  def decodeSearchPagination(token: String): Option[SearchPaginationModel] = {
    (for {
      spmJson <- Jwt.decode(token, paginationSecret, Seq(paginationAlgorithm))
      searchPaginationModel <- Try(mapper.readValue[SearchPaginationModel](spmJson))
    } yield searchPaginationModel).toOption
  }
}
