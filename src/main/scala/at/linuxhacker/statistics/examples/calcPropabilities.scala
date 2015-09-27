package at.linuxhacker.statistics.examples

import scala.annotation.tailrec


object PropabilityGraph {
  case class Situation( val white: Integer, black: Integer, probability: Double, deep: Integer )
  case class Node( mySituation: Situation, left: Option[Node], right: Option[Node] )
  case class Root( white: Integer, black: Integer, left: Node, right: Node )
  
  private def calcGraphRec( white: Integer, black: Integer, probablity: Double, depth: Integer): Node = {
    
    val mySituation = Situation( white, black, probablity, depth )
    val sum = white.toDouble + black.toDouble
    val probLeft = white.toDouble / sum
    val probRight = black.toDouble / sum
    
    val resLeft = {
      if ( white > 0 )
        Some( calcGraphRec( white - 1, black, probLeft, depth + 1 ) )
      else
        None
    }
    val resRight = {
      if ( black > 0 )
        Some( calcGraphRec( white, black - 1, probRight, depth + 1 ) )
      else
        None
    }
    
    Node( mySituation, resLeft, resRight )
  }
  
  def calcGraph( white: Integer, black: Integer ): Node = {
    val sum = white.toDouble + black.toDouble
     val resLeft = calcGraphRec( white - 1, black, white.toDouble / sum, 1 )
     val resRight = calcGraphRec( white, black - 1, black.toDouble / sum, 1 )
     Node( Situation( white, black, 1.0, 0 ), Some( resLeft ), Some( resRight ) )
  }
  
  case class Probability( value: Double, node: Node )
  private def transformCall( node: Option[Node], p: List[Double], query: ( Situation ) => Boolean ): List[Probability] = {
      node match {
        case Some( o ) => findNodesRec( o, p )( query )
        case None => List[Probability]( )
      }
    }
  
  private def findNodesRec( current: Node, pathPosabilityValue: List[Double] )( f: ( Situation ) => Boolean ): List[Probability] = {
    //println( current.mySituation + " value: " + pathPosabilityValue )
    val p = pathPosabilityValue :+ current.mySituation.probability
    val left = transformCall( current.left, p, f )
    val right = transformCall( current.right, p, f )
    val found = {
      if ( f( current.mySituation ) ) {
        val x = p.foldLeft( 0.toDouble )( (r, e ) => if( r != 0 ) r * e else e )
        //println( "found: " + current.mySituation + " x: " + x + " list: " + p )
        List( Probability( x, current ) )
      } else
        List[Probability]( )
    }
    found ++ left ++ right
  }
  
  def findNodes( root: Node )( f: ( Situation ) => Boolean ): List[Probability] = {
    val left = transformCall( root.left, List[Double]( ), f )
    val right = transformCall( root.right, List[Double]( ), f )
    left ++ right 
  }
}

object calcPropabilities extends App {
  
  val res = PropabilityGraph.calcGraph( args(0).toInt, args(1).toInt )
  
  val f1: ( PropabilityGraph.Situation ) => Boolean = { x => ( x.white == 0 && x.black >= 0 ) }
  val found1 = PropabilityGraph.findNodes( res )( f1 )
  // found1.foreach{ x => println( x.value + " " + x.node.mySituation ) }
  
  println( "Table" )
  val deepGroups = found1.groupBy { x => x.node.mySituation.deep }
  deepGroups.map { x => x._1 }.toList.sorted.foreach { i =>
    val p = deepGroups(i).map { x => x.value }.sum
    println( s"Depth: $i with $p" )
    /* 
    deepGroups(i).foreach { g => 
      println( s"  Propability: ${g.value} situation ${g.node.mySituation}" )
    }
    println( )
    */

  }
  
  
  
}