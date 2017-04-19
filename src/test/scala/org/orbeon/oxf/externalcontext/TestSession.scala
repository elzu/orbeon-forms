package org.orbeon.oxf.externalcontext

import java.{util ⇒ ju}

import org.orbeon.oxf.common.OXFException
import org.orbeon.oxf.externalcontext.ExternalContext.{Session, SessionListener, SessionScope}

import scala.collection.JavaConverters._
import scala.collection.{immutable ⇒ i}

// TODO: Check if different from SimpleExternalContext.SessionImpl!
class TestSession(sessionId: String) extends Session {

  private val creationTime     = System.currentTimeMillis
  private val sessionListeners = new ju.LinkedHashSet[SessionListener]
  private var expired          = false

  private var sessionAtts = i.HashMap[String, AnyRef]()

  def expireSession(): Unit = {
    for (listener ← sessionListeners.asScala)
      listener.sessionDestroyed()
    expired = true
  }

  def addListener(sessionListener: SessionListener): Unit = {
    checkExpired()
    sessionListeners.add(sessionListener)
  }

  def removeListener(sessionListener: SessionListener): Unit = {
    checkExpired()
    sessionListeners.remove(sessionListener)
  }

  def getAttribute(name: String, scope: SessionScope): Option[AnyRef] = {
    checkExpired()
    require(scope == SessionScope.Application)
    sessionAtts.get(name)
  }

  def setAttribute(name: String, value: AnyRef, scope: SessionScope): Unit = {
    checkExpired()
    require(scope == SessionScope.Application)
    sessionAtts += name → value
  }

  def removeAttribute(name: String, scope: SessionScope): Unit = {
    checkExpired()
    require(scope == SessionScope.Application)
    sessionAtts -= name
  }


  def getCreationTime: Long = {
    checkExpired()
    creationTime
  }

  def getId: String = {
    checkExpired()
    sessionId
  }

  // TODO
  def getLastAccessedTime: Long = {
    checkExpired()
    0L
  }

  // TODO
  def getMaxInactiveInterval: Int = {
    checkExpired()
    0
  }

// TODO
    def invalidate(): Unit = {
    checkExpired()
  }

  // TODO
  def isNew: Boolean = {
    checkExpired()
    false
  }

  // TODO
  def setMaxInactiveInterval(interval: Int): Unit = {
    checkExpired()
  }

  private def checkExpired(): Unit =
    if (expired)
      throw new OXFException("Cannot call methods on expired session.")
}