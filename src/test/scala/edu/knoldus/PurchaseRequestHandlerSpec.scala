package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{CallingThreadDispatcher, EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import edu.knoldus.actors.PurchaseRequestHandler
import edu.knoldus.models.Customer
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

object PurchaseRequestHandlerSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import edu.knoldus.PurchaseRequestHandlerSpec._


class PurchaseRequestHandlerSpec extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A PurchaseRequestHandler Actor" must {

    "send a message to another actor when it is finished processing" in {

      val purchaseRequestProps = PurchaseRequestHandler.purchaseRequestProps(testActor)
      val purchaseRequestRef = system.actorOf(purchaseRequestProps)

      purchaseRequestRef ! Customer("Gitika", "Paschim Vihar", 78996, 9999)
      expectMsg(Customer("Gitika", "Paschim Vihar", 78996, 9999))
    }

    "log about invalid customer " in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = PurchaseRequestHandler.purchaseRequestProps(testActor).withDispatcher(dispatcherId)
      val ref = system.actorOf(props)

      EventFilter.info(message = "PurchaseRequestHandler: Invalid Request!", occurrences = 1)
        .intercept {
          ref ! "PurchaseRequestHandler: Invalid Request!"
        }
    }

  }
}
