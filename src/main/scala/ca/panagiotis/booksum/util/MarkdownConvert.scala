package ca.panagiotis.booksum.util

import laika.api.Transform
import laika.parse.markdown.Markdown
import laika.render.HTML

/**
  * Created by panagiotis on 23/05/17.
  */
object MarkdownConvert {
  def toHtml(md: String): String = {
    Transform from Markdown to HTML fromString md toString
  }
}
