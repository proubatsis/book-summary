package ca.panagiotis.booksum.util

import ca.panagiotis.booksum.models.{Account, SearchPaginationModel}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.{Failure, Success, Try}

/**
  * Created by panagiotis on 20/05/17.
  */
object Token {
  private val genericAlgorithm = JwtAlgorithm.HMD5
  private val genericSecret = sys.env.getOrElse("BOOKSUM_GENERIC_SECRET", "JkjHCi3nw8*#W*(YhIUY#@ueshjD(OI#e")

  private val accessAlgorithm = JwtAlgorithm.HS512
  private val accessSecret = sys.env.getOrElse("BOOKSUM_ACCESS_SECRET", "fdeuwni*HF(OIjrkfdkjfhuehr8HF&I$#UHRIEUKfjd")
  private val accessExpiryInSeconds = 60 * 8

  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def encodeSearchPagination(searchPaginationModel: SearchPaginationModel): String = {
    val spmJson = mapper.writeValueAsString(searchPaginationModel)
    Jwt.encode(spmJson, genericSecret, genericAlgorithm)
  }

  def decodeSearchPagination(token: String): Option[SearchPaginationModel] = {
    (for {
      spmJson <- Jwt.decode(token, genericSecret, Seq(genericAlgorithm))
      searchPaginationModel <- Try(mapper.readValue[SearchPaginationModel](spmJson))
    } yield searchPaginationModel).toOption
  }

  def encodeUrl(url: String): String = Jwt.encode(url, genericSecret, genericAlgorithm)
  def decodeUrl(token: String): Option[String] = {
    (for {
      url <- Jwt.decode(token, genericSecret, Seq(genericAlgorithm))
    } yield url).toOption
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
