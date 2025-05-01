



object AssignmentProblem {
  def main(args: Array[String]): Unit = {
    // Define the cost matrix
    val costMatrix = Array(
      Array(0, 0, 0, 0, 0, 0), // dummy row for simplicity
      Array(0, 13, 8, 16, 18, 19),
      Array(0, 9, 15, 24, 9, 12),
      Array(0, 12, 9, 4, 4, 4),
      Array(0, 6, 12, 10, 8, 13),
      Array(0, 15, 17, 18, 12, 20)
    )

    // Initialize the result matrix with zeros
    val rows = costMatrix.length
    val cols = costMatrix(0).length
    val result = Array.ofDim[Int](rows, cols)

    // Fill the result matrix using Dynamic Programming
    for {
      i <- 1 until rows
      j <- 1 until cols
    } result(i)(j) = costMatrix(i)(j) + (
      if (i == 1 && j == 1) 0
      else if (i == 1) result(i)(j - 1)
      else if (j == 1) result(i - 1)(j)
      else result(i - 1)(j - 1)
    )

    // Find the optimal assignment
    val assignedJobs = Array.ofDim[Int](rows - 1)
    var i = rows - 1
    var j = cols - 1
    for {
      k <- (rows - 2 to 0 by -1)
    } {
      assignedJobs(k) = j - 1
      if (j > 1 && result(i)(j - 1) < result(i)(j)) j -= 1
      else i -= 1
    }

    // Print the optimal assignment and total cost
    println("Optimal Assignment:")
    for {
      (job, machine) <- assignedJobs.zipWithIndex
    } println(s"Job ${job + 1} -> Machine $machine")
    println(s"Total Cost: ${result.last.last}")
  }
}
