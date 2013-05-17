package code 
package snippet 

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.common._
import java.util.Date
import code.model._
import Helpers._

class Account {
	var l: String = _


	def login = {
		User.currentUser match {
			case Full(user) => "*" #> <span>{user.name.get}</span>
			case _ =>
				"@login" #> SHtml.text("", l = _) &
				"@button" #> SHtml.button("Submit", () => {
					User.login(User.create.name(l).saveMe)
				})
		}
	}

	def logSession = {
		"button" #> SHtml.button("Show Session", () => {
			for {
	      req <- S.containerRequest
	    } {
	    	println("SESSION VALUES")
	    	val session = req.asInstanceOf[net.liftweb.http.provider.servlet.HTTPRequestServlet].req.getSession(false)
	    	val attributeNames = session.getAttributeNames
	    	while(attributeNames.hasMoreElements) {
	    		val attribute = attributeNames.nextElement
	    		val value = session.getAttribute(attribute)
	    		println(s"ATTRIBUTE $attribute = $value")
	    		if(attribute == "_lift_sv_code.model.User$curUserId$_") {
	    			val l = value match {
	    				case a : Array[Byte] => ContainerSerializer.longSerializer.deserialize(a)
	    				case _ => -1l
	    			}
	    			println(s"as long $l")
	    		}
	    	}
	    }
		})
	}

	def instanceName = {
		"*" #> Account.name
	}
}

object Account {
	var name: String = ""
}