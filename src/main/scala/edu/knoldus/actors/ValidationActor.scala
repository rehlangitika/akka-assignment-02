package edu.knoldus.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import edu.knoldus.models.{Checking, Customer}

import scala.concurrent.Await
import scala.concurrent.duration._

class ValidationActor(purchaseRef: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(1000.seconds)
  var senderRef: Option[ActorRef] = None

  override def receive: PartialFunction[Any, Unit] = {
    case customer: Customer =>
      senderRef = Some(sender)
      log.info("ValidationActor: Checking if Booking can be made or not")
      val status = (purchaseRef ? Checking).mapTo[Boolean]
      if (Await.result(status, 1.second)) {
        log.info("ValidationActor: Item in stock..proceeding towards booking")
        purchaseRef ! customer
      }
      else {
        log.info("ValidationActor: Item Out of Stock!")
        senderRef.foreach(_ ! "Not Available")
      }

  }

}

object ValidationActor {

  def validationProps(purchaseRef: ActorRef) = Props(classOf[ValidationActor], purchaseRef)

}
