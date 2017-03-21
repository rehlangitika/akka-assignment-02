package edu.knoldus.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.models.{Booking, Customer, GalaxyS8}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class PurchaseRequestHandler(validationRef: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(1000.seconds)
  var senderRef: Option[ActorRef] = None

  override def receive = {
    case customer: Customer =>
      senderRef = Some(sender)
      log.info(s"Purchase Request from Customer: ${customer.name}, ${customer.address}, ${customer.mobileNumber}")
      //val validationRef = context.actorOf(Props(classOf[ValidationActor], purchaseRef))
      val request = validationRef ? Booking
      request.map {
        case GalaxyS8 =>
          log.info("PurchaseRequestHandler: Booking Request Processed")
          senderRef.foreach(_ ! s"${customer.name}, Your Booking Done Successfully!!")
        case x =>
          log.info(s"PurchaseRequestHandler: $x")
          senderRef.foreach(_ ! s"Booking not done!")
      }

    case _ =>
      log.info(s"PurchaseRequestHandler: Invalid Request!")
  }

}

object PurchaseRequestHandler {

  def purchaseRequestProps(validationRef: ActorRef) = Props(classOf[PurchaseRequestHandler], validationRef)

}


