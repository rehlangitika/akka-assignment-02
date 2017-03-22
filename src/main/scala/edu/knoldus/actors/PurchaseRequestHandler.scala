package edu.knoldus.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.models.Customer

import scala.concurrent.duration._

class PurchaseRequestHandler(validationRef: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(1000.seconds)
  var senderRef: Option[ActorRef] = None

  override def receive: PartialFunction[Any, Unit] = {
    case customer: Customer =>
      senderRef = Some(sender)
      log.info(s"Purchase Request from Customer: ${customer.name}, ${customer.address}, ${customer.mobileNumber}")
      validationRef ? customer

    case _ =>
      log.info(s"PurchaseRequestHandler: Invalid Request!")
  }

}

object PurchaseRequestHandler {

  def purchaseRequestProps(validationRef: ActorRef) = Props(classOf[PurchaseRequestHandler], validationRef)

}


