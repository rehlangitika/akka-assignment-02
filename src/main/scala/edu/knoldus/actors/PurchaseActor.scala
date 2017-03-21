package edu.knoldus.actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.Timeout
import edu.knoldus.models.{Booking, Checking, GalaxyS8}

import scala.concurrent.duration._

class PurchaseActor extends Actor with ActorLogging {

  var totalMobiles = 2
  val timeout = Timeout(20.seconds)

  override def receive = {
    case Checking if totalMobiles > 0 =>
      log.info("PurchaseActor: Booking can be done!")
      sender ! true
    case Booking => {
      log.info("PurchaseActor: Processing Booking")
      totalMobiles -= 1
      log.info("Item is booked!")
    }
    case _ =>
      log.info("PurchaseActor: Item out of stock")
      sender ! false
  }

}

object PurchaseActor {
  def purchaseProps = Props[PurchaseActor]
}