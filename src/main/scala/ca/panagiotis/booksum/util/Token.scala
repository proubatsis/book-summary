package ca.panagiotis.booksum.util

import ca.panagiotis.booksum.models.{Account, SearchPaginationModel}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.Try

/**
  * Created by panagiotis on 20/05/17.
  */
object Token {
  private val paginationAlgorithm = JwtAlgorithm.HMD5
  private val paginationSecret = "JkjHCi3nw8*#W*(YhIUY#@ueshjD(OI#e"

  private val accessAlgorithm = JwtAlgorithm.HS512
  private val accessSecret = "fdeuwni*HF(OIjrkfdkjfhuehr8HF&I$#UHRIEUKfjd"
  private val accessExpiryInSeconds = 60 * 8

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

  def encodeAccessToken(account: Account): String = {
    val aJson = mapper.writeValueAsString(account)
    Jwt.encode(JwtClaim(aJson).expiresIn(accessExpiryInSeconds), accessSecret, accessAlgorithm)
  }

  def decodeAccessToken(accessToken: String): Option[Account] = {
    (for {
      aJson <- Jwt.decode(accessToken, accessSecret, Seq(accessAlgorithm))
      account <- Try(mapper.readValue[Account](aJson))
    } yield account).toOption
  }
}
