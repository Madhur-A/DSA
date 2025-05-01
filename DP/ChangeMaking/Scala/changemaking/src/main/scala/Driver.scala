



object Driver {
    val testCases: List[(Array[Int], Int)] = List(
        (Array(1, 2, 5),  6),
        (Array(2, 5, 7), 41),
        (Array(1, 2, 5, 10, 20, 50), 51),
        (Array(1, 2, 5, 10, 20, 50), 50),
        (Array(20, 50), 17),
        (Array(1, 2, 5, 10, 20, 50), 10),
        (Array(1, 2, 5, 10, 20, 50), 11),
        (Array(1, 2, 5, 10, 20, 50), 1),
        (Array(1, 2, 5, 10, 50), 347),
        (Array(1, 2, 5, 10, 20, 50), 101),
        (Array(1, 2, 5, 10, 20, 50), 0)
    )

    def execute(functionsList: Array[(Array[Int], Int) => String]): Unit = {
        functionsList.foreach { funcName =>
            println(s"From \u001b[35m$funcName \u001b[0m")
            testCases.foreach { case (coins, target) =>
                val res = funcName(coins, target)
                println(f"\u001b[31m${coins.mkString("[",",","]")}%25s $target%5d\u001b[0m : \u001b[34m $res \u001b[0m")
            }
        }
    }
}
