package edu.knoldus

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import edu.knoldus.actors.ValidationActor
import edu.knoldus.models.{Booking, Checking}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}


class ValidationActorSpec extends TestKit(ActorSystem("BookingPhone")) with WordSpecLike
with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A Validation Actor" must {
    "send a message to another actor when it is finished processing" in {

      val validationProps = ValidationActor.validationProps(testActor)
      val validationRef = system.actorOf(validationProps)

      validationRef ! Booking
      expectMsg(Checking)

    }
  }

}
