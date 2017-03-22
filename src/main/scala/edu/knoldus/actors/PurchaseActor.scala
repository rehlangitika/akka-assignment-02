package edu.knoldus.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.util.Timeout
import edu.knoldus.actors.PurchaseActor.GetState
import edu.knoldus.models.{Checking, Customer}

import scala.concurrent.duration._

class PurchaseActor extends Actor with ActorLogging {

  var totalMobiles = 2
  val timeout = Timeout(20.seconds)

  override def receive: PartialFunction[Any, Unit] = {
    case Checking if totalMobiles > 0 =>
      log.info("PurchaseActor: Booking can be done!")
      sender ! true
    case customer: Customer => {
      log.info("PurchaseActor: Processing Booking")
      totalMobiles -= 1
      log.info(s"${customer.name}, Item is booked!")
    }
    case GetState(ref) => ref ! totalMobiles
    case _ =>
      log.info("PurchaseActor: Item out of stock")
      sender ! false
  }

}

object PurchaseActor {

  case class GetState(ref: ActorRef)

  def purchaseProps: Props = Props[PurchaseActor]

}
