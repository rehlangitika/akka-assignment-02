package edu.knoldus

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.routing.FromConfig
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import edu.knoldus.actors.{PurchaseActor, PurchaseRequestHandler, ValidationActor}
import edu.knoldus.models.Customer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object BookingPhone extends App {

  val config = ConfigFactory.parseString(
    """
      |akka.actor.deployment {
      | /poolRouter {
      |   router = balancing-pool
      |   nr-of-instances = 5
      | }
      |}
    """.stripMargin
  )
  implicit val timeout = Timeout(1000.seconds)
  val actorSystem = ActorSystem("BookingPhone", config)
  val purchaseRef = actorSystem.actorOf(FromConfig.props(PurchaseActor.purchaseProps), "poolRouter")
  //val validationRef = actorSystem.actorOf(Props(classOf[ValidationActor], purchaseRef))
  val validationRef = actorSystem.actorOf(ValidationActor.validationProps(purchaseRef))
  //val phoneRequestRef = actorSystem.actorOf(Props(classOf[PurchaseRequestHandler], validationRef))
  val phoneRequestRef = actorSystem.actorOf(PurchaseRequestHandler.purchaseRequestProps(validationRef))
  //val phoneRequestRef1 = actorSystem.actorOf(Props(classOf[PurchaseRequestHandler], validationRef))
  val phoneRequestRef1 = actorSystem.actorOf(PurchaseRequestHandler.purchaseRequestProps(validationRef))
  val request = phoneRequestRef ? Customer("Gitika", "Paschim Vihar", 3455, 9999)
  request.map { result =>
    println(result)
  }

  val request1 = phoneRequestRef1 ? Customer("Nikita", "Paschim Vihar", 3455, 9999)
  request.map { result =>
    println(result)
  }

}


