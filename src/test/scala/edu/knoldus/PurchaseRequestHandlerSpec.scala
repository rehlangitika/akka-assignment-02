package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import edu.knoldus.actors.PurchaseRequestHandler
import edu.knoldus.models.{Booking, Customer}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class PurchaseRequestHandlerSpec extends TestKit(ActorSystem("BookingPhone")) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A PurchaseRequestHandler Actor" must {
    "send a message to another actor when it is finished processing" in {

      val purchaseRequestProps = PurchaseRequestHandler.purchaseRequestProps(testActor)
      val purchaseRequestRef = system.actorOf(purchaseRequestProps)

      purchaseRequestRef ! Customer("Gitika", "Paschim Vihar", 78996, 9999)
      expectMsg(Booking)
    }
  }
}
