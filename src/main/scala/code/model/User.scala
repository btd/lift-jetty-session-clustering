package code
package model

import net.liftweb.mapper._
import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with LongKeyedMetaMapper[User] {

  object curUserId extends ClusterVar[Long]("currentUserId")

  //object curUserId extends SomeVar[Long](-1)
  object curUser extends RequestVar[Box[User]](curUserId.get.flatMap(User.find))

  def currentUser = curUser.get

  def loggedIn_? = currentUser.isDefined

  def login(who: User) {
    curUserId(who.id.get)
    curUser.set(Full(who))
  }
}


class User extends LongKeyedMapper[User] with IdPK {
  def getSingleton = User // what's the "meta" server

  object name extends MappedString(this, 200)
}


abstract class ClusterVar[T](val name: String) {
  var localValue: Box[T] = Empty

  def apply(v: => T) = {
    for {
      req <- S.containerRequest
    } {
      val session = req.asInstanceOf[net.liftweb.http.provider.servlet.HTTPRequestServlet].req.getSession(false)
      session.setAttribute(name, v)
      localValue = Full(v)
    }
  }

  def get: Box[T] = {
    localValue match {
      case s @ Full(_) => s
      case _ => get_!
    }
  }

  def get_! : Box[T] = {
    S.containerRequest match {
      case Full(req) => 
        val session = req.asInstanceOf[net.liftweb.http.provider.servlet.HTTPRequestServlet].req.getSession(false)
        localValue = (Box !! session.getAttribute(name)).map(_.asInstanceOf[T])
        localValue
      case _ => Empty
    }
  }
}
