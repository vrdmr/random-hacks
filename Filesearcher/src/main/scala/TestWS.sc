object TestWS {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  val aa = List(1, 2, 3)                          //> aa  : List[Int] = List(1, 2, 3)
  aa map(num => num*2)                            //> res0: List[Int] = List(2, 4, 6)
  aa map(num => num/2.0)                          //> res1: List[Double] = List(0.5, 1.0, 1.5)
}