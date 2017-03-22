package edu.knoldus

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{CallingThreadDispatcher, EventFilter, ImplicitSender, TestActor, TestActorRef, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import edu.knoldus.actors.ValidationActor
import edu.knoldus.models.{Checking, Customer}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

object ValidationActorSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import edu.knoldus.ValidationActorSpec._

class ValidationActorSpec extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "A Validation Actor" must {
    val customer = Customer("Anuj", "Noida", 3325, 9871)
    val probe = TestProbe()

    "test ask await condition for true" in {

      val ref = TestActorRef(new ValidationActor(probe.ref))

      probe.setAutoPilot(new TestActor.AutoPilot {
        def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
          msg match {
            case Checking =>
              sender.tell(true, self)
              TestActor.KeepRunning

            case c: Customer =>
              testActor.tell(c, sender)
              TestActor.KeepRunning
          }
      })

      ref ! customer
      expectMsg(customer)
    }

    "test ask await condition for false" in {

      probe.setAutoPilot(new TestActor.AutoPilot {
        override def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
          msg match {
            case Checking =>
              sender.tell(false, self)
              TestActor.KeepRunning
          }
      })

      val dispatcherId = CallingThreadDispatcher.Id
      val props = ValidationActor.validationProps(probe.ref).withDispatcher(dispatcherId)
      val ref = system.actorOf(props)

      EventFilter.info(message = "ValidationActor: Item Out of Stock!", occurrences = 1)
        .intercept {
          ref ! customer
        }
    }
  }

}

