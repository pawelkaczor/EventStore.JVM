package eventstore.cluster

import scala.collection.immutable.SortedSet

sealed trait NodeState extends Ordered[NodeState] {
  def id: Int
  def isReplica: Boolean
  def isAllowedToConnect: Boolean
  def compare(that: NodeState) = this.id compare that.id
}

object NodeState {
  val values: SortedSet[NodeState] = SortedSet(
    Initializing,
    Unknown,
    PreReplica,
    CatchingUp,
    Clone,
    Slave,
    PreMaster,
    Master,
    Manager,
    ShuttingDown,
    Shutdown)

  private val map: Map[String, NodeState] = values.toSet[NodeState].map(x => x.toString -> x).toMap

  def apply(x: String): NodeState = map.getOrElse(x, throw new IllegalArgumentException(s"No NodeState found for $x"))

  case object Initializing extends NodeState {
    def id = 0
    def isReplica = false
    def isAllowedToConnect = true
  }

  case object Unknown extends NodeState {
    def id = 1
    def isReplica = false
    def isAllowedToConnect = true
  }

  case object PreReplica extends NodeState {
    def id = 2
    def isReplica = false
    def isAllowedToConnect = true
  }

  case object CatchingUp extends NodeState {
    def id = 3
    def isReplica = true
    def isAllowedToConnect = true
  }

  case object Clone extends NodeState {
    def id = 4
    def isReplica = true
    def isAllowedToConnect = true
  }

  case object Slave extends NodeState {
    def id = 5
    def isReplica = true
    def isAllowedToConnect = true
  }

  case object PreMaster extends NodeState {
    def id = 6
    def isReplica = false
    def isAllowedToConnect = true
  }

  case object Master extends NodeState {
    def id = 7
    def isReplica = false
    def isAllowedToConnect = true
  }

  case object Manager extends NodeState {
    def id = 8
    def isReplica = false
    def isAllowedToConnect = false
  }

  case object ShuttingDown extends NodeState {
    def id = 9
    def isReplica = false
    def isAllowedToConnect = false
  }

  case object Shutdown extends NodeState {
    def id = 10
    def isReplica = false
    def isAllowedToConnect = false
  }
}