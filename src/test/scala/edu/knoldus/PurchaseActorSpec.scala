package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import edu.knoldus.actors.PurchaseActor
import edu.knoldus.actors.PurchaseActor.GetState
import edu.knoldus.models.{Checking, Customer}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

object PurchaseActorSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import edu.knoldus.PurchaseActorSpec._

class PurchaseActorSpec extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A Purchase Actor" must {

    val purchaseProps = PurchaseActor.purchaseProps
    val purchaseRef = system.actorOf(purchaseProps)
    val customer = Customer("Gitika", "Paschim Vihar", 93456, 99999)
    "send a message to another actor when it is finished processing" in {

      purchaseRef ! customer
      purchaseRef ! GetState(testActor)
      expectMsg(1)
    }

    "send true when it received Checking" in {

      purchaseRef ! Checking

      expectMsg(true)
    }

    "send false when it receives some other message" in {

      purchaseRef ! "some message"

      expectMsg(false)
    }
  }

}
