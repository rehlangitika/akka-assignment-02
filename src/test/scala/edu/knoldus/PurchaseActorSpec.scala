package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{CallingThreadDispatcher, EventFilter, ImplicitSender, InfoFilter, TestKit}
import com.typesafe.config.ConfigFactory
import edu.knoldus.actors.{PurchaseActor, ValidationActor}
import edu.knoldus.models.{Booking, Checking}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}
import PurchaseActorSpec._
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

class PurchaseActorSpec extends TestKit(ActorSystem("testSystem")) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A Purchase Actor" must {
    "send a message to another actor when it is finished processing" in {

      val purchaseProps = PurchaseActor.purchaseProps
      val purchaseRef = system.actorOf(purchaseProps)

      purchaseRef ! Checking

      expectMsg(true)

      purchaseRef ! "some message"

      expectMsg(false)
    }

    /*"say PurchaseActor: Booking can be done! when receive 'Checking' " in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = PurchaseActor.purchaseProps.withDispatcher(dispatcherId)
      val ref = system.actorOf(props)

      /*EventFilter.info(message = "PurchaseActor: Booking can be done!", occurrences = 1).intercept{
        ref ! "PurchaseActor: Booking can be done!"
      }*/

    }*/
  }
}
