package eventstore.util

import org.specs2.mutable.Specification
import org.specs2.specification.{ Scope, Step, Fragments }
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }

/**
 * @author Yaroslav Klymko
 */
abstract class ActorSpec extends Specification with NoConversions {
  implicit val system = ActorSystem()

  override def map(fs: => Fragments) = super.map(fs) ^ Step(system.shutdown())

  abstract class ActorScope extends TestKit(system) with ImplicitSender with Scope
}