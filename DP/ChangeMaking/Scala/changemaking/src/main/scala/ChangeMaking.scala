



import scala.collection.mutable.{Queue, Set, PriorityQueue, Map, ArrayBuffer}

// please note:
// 1) -1 indicates that no combination has been found for the target
// 2)  0 indicates that the target is 0

object ChangeMaking {
    // number of ways to get the target
    def numberOfWaysCombinations(coins: Array[Int], target: Int): String = {
        // of course a 2D array is needed, dp(i)(j) = the number of ways to reach 'j' with first 'i' coins
        // this immediately gives dp(j)(0) = 1, by choosing first 0 coins (base case)
        val n = coins.length
        val dp = Array.fill(n + 1, target + 1)(0)
        (0 to n).foreach { j => dp(j)(0) = 1 }

        (1 to n).foreach { coinIndex =>
            (1 to target).foreach{ subTarget =>
                // print(s"($coin)($subTarget)  ")
                if (coins(coinIndex - 1) <= subTarget) {
                    dp(coinIndex)(subTarget) = dp(coinIndex - 1)(subTarget) + dp(coinIndex)(subTarget - coins(coinIndex - 1))
                } else {
                    dp(coinIndex)(subTarget) = dp(coinIndex - 1)(subTarget)
                }
            }
            // println(" ")
        }


        dp(n)(target).toString
    }

    def numberOfWaysPermutations(coins: Array[Int], target: Int): String = {
        val dp = Array.fill(target + 1)(0L)
        val mod = 1_000_000_007;
        dp(0) = 1L

        (0 to target).foreach { subTarget =>
            for (coin <- coins) {
                if (coin <= subTarget) {
                    dp(subTarget) += dp(subTarget - coin) % mod
                }
            }
        }
        dp(target).toString
    }

    def main(args: Array[String]): Unit = {
        val functionsList: Array[(Array[Int], Int) => String] =  Array(numberOfWaysCombinations, getCoinsDPBottomUpVersionII)
        Driver.execute(functionsList)
    }

    def getCoinsDPTopDownVersionII(coins: Array[Int], target: Int): String = {
        val invalid = target + 1
        val dp = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](invalid))
        dp(0) = ArrayBuffer[Int]()

        def dfs(curr: Int): ArrayBuffer[Int] = {
            if (curr < 0) { return ArrayBuffer[Int](invalid) }
            if (dp(curr).isEmpty || dp(curr).head != invalid) { return dp(curr) }

            for (coin <- coins) {
                val transitResult = dfs(curr - coin)
                if (transitResult.isEmpty || transitResult.head != invalid) { // found legitimate result
                    val tempAns = transitResult.appended(coin)
                    if (dp(curr).head == invalid || dp(curr).length > tempAns.length) {
                        dp(curr) = tempAns
                    }
                }
            }

            return dp(curr)
        }

        dfs(target).toArray.mkString("[",",","]")
    }

    def getCoinsDPBottomUpVersionII(coins: Array[Int], target: Int): String = {
        val invalid = target + 1
        val dp = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](invalid))
        dp(0) = ArrayBuffer[Int]()

        coins.foldLeft(0) { case (index, coin) =>
            (coin to target).foreach { subTarget =>
                if (dp(subTarget - coin).isEmpty || dp(subTarget - coin).head != invalid) {
                    if (dp(subTarget).head == invalid || dp(subTarget).length > dp(subTarget - coin).length + 1) {
                        dp(subTarget) = dp(subTarget - coin).appended(coin)
                    }
                }
            }
            index + 1
        }

        dp(target).toArray.mkString("[", ",", "]")
    }

    def getCoinsBFS(coins: Array[Int], target: Int): String = {
        val dp = Map[Int, ArrayBuffer[Int]]()
        dp.getOrElseUpdate(0, ArrayBuffer[Int]())

        val t = Queue[Int]()
        t.enqueue(target)
        while (! t.isEmpty) {
            val curr = t.dequeue()
            if (curr == 0) { return dp.getOrElse(target, ArrayBuffer[Int]()).toString }

            for (coin <- coins) {
                if (coin <= curr && !dp.contains(curr - coin)) {
                    // dp.getOrElseUpdate(curr - coin, ArrayBuffer[Int]()).addAll(dp.getOrElseUpdate())
                }
            }
        }
        "";
    }

    def getCoinsDPTopDown(coins: Array[Int], target: Int): String = {
        val invalid = target + 1;
        val dp = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](invalid))
        dp(0) = ArrayBuffer[Int]()

        def innerRecursive(index: Int, curr: Int): ArrayBuffer[Int] = {
            if (curr  < 0)              { return ArrayBuffer[Int](invalid)   }
            if (index ==  coins.length) { return ArrayBuffer[Int](invalid)   }
            if (dp(curr).isEmpty || dp(curr).head != invalid) { return dp(curr); }

            val pick = innerRecursive(index, curr - coins(index))
            val skip = innerRecursive(index + 1, curr)

            if (pick.isEmpty || pick.head != invalid) {
                val tempAns = pick.appended(coins(index))
                if (skip.head == invalid || skip.length > tempAns.length) {
                    dp(curr) = tempAns
                }
            }

            return dp(curr)
        }

        innerRecursive(0, target).toString
    }

    def getCoinsDPBottomUp(coins: Array[Int], target: Int): String = {
        val invalid = target + 1
        val dp = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](invalid))
        dp(0) = ArrayBuffer[Int]()
        coins.foldLeft(dp(target)) { case (row, coin) =>
            (coin to target).foreach( subTarget =>
                if (subTarget - coin >= 0
                    && (dp(subTarget - coin).isEmpty || dp(subTarget - coin).head != invalid)
                    && (dp(subTarget).head == invalid || dp(subTarget).length > dp(subTarget - coin).length + 1)) {
                    dp(subTarget) = dp(subTarget - coin).appended(coin)
                }
            ); dp(target)
        } match {
            case res if res.isEmpty         => "not applicable"
            case res if res.head == invalid => "not found"
            case res                        =>  res.toString
        }
    }

    def vanillaBFSWithCoins(coins: Array[Int], target: Int): Array[Int] = {
        val (t, seen) = (Queue[Int](), Map[Int, ArrayBuffer[Int]]())
        seen(0) = ArrayBuffer[Int]()
        t.enqueue(0)
        while (! t.isEmpty) {
            val currTarget = t.dequeue()
            seen.getOrElseUpdate(currTarget, ArrayBuffer[Int]());
            if (currTarget == target) { return seen(currTarget).toArray }
            for (coin <- coins) {
                if (coin + currTarget <= target && ! seen.contains(currTarget + coin)) {
                    seen.getOrElseUpdate(currTarget + coin, ArrayBuffer[Int]()).addAll(seen.getOrElseUpdate(currTarget, ArrayBuffer[Int]())).addAll(Array(coin))
                    t.enqueue(currTarget + coin)
                }
            }
        }

        return seen(0).toArray
    }

    def vanillaBFS(coins: Array[Int], target: Int): Int = {
        val (t, seen) = (Queue[(Int, Int)](), Set[Int]())
        t.enqueue((target, 0))
        while (! t.isEmpty) {
            val (currTarget, count) = t.dequeue()
            if (currTarget == 0) { return count }
            for (coin <- coins) {
                if (0 <= (currTarget - coin) && ! seen.contains(currTarget - coin)) {
                    seen.add(currTarget - coin)
                    t.enqueue((currTarget - coin, count + 1))
                }
            }
        }
        return -1;
    }

    def changeMakingCountDPTopDown(coins: Array[Int], target: Int): Int = {
        val (invalid, n) = (target + 1, coins.length)
        val           dp = Array.fill(target + 1)(invalid)
        dp.update(0, 0)
        def dfs(index: Int, remains: Int): Int = {
            if (index   >= n) { return invalid  }
            if (remains  < 0) { return invalid  }
            if (dp(remains) != invalid) { return dp(remains) }

            dp(remains) = math.min(dfs(index + 1, remains), 1 + dfs(index, remains - coins(index)))
            return dp(remains)
        }
        val res = dfs(0, target)

        if (res == invalid) -1 else res
    }

    def changeMakingCountDPBottomUp(coins: Array[Int], target: Int): Int = {
        val (invalid, n) = (target + 1, coins.length)
        val           dp = Array.fill(target + 1)(invalid)
        dp.update(0, 0)
        for (coin <- coins) {
            (coin to target).foreach { subTarget =>
                if (coin <= subTarget && dp(subTarget - coin) != invalid) {
                    dp(subTarget) = math.min(dp(subTarget), dp(subTarget - coin) + 1)
                }
            }
        }

        if (dp(target) == invalid) -1 else dp(target)
    }



    def changeMakingListingDPTopDown(coins: Array[Int], target: Int): Array[Int] = {
        val seen = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](-999))
        seen(0) = ArrayBuffer[Int]()

        def dfs(remains: Int): ArrayBuffer[Int] = {
            if (remains < 0)  {
                ArrayBuffer[Int](-999)
            } else if (seen(remains).isEmpty || seen(remains).head != -999)  {
                seen(remains)
            } else {
                var res = ArrayBuffer[Int](-999)
                for (coin <- coins) {
                    val aux = dfs(remains - coin)
                    if (aux.isEmpty || aux.head != -999) {
                        val possible = aux.appended(coin)
                        if (res.head == -999 || res.length > possible.length) {
                            res = possible
                        }
                    }
                }
                seen(remains) = res
                res
            }
        }

        dfs(target).toArray
    }

    def changeMakingListingDPBottmUp(coins: Array[Int], target: Int): Array[Int] = {
        val seen = ArrayBuffer.fill(target + 1)(ArrayBuffer[Int](-999))
        seen(0) = ArrayBuffer[Int]();

        coins.foldLeft(()) { case (_, coin) =>
            seen.indices.foreach { subTarget =>
                if (subTarget >= coin && (seen(subTarget - coin).isEmpty || (seen(subTarget - coin).head != -999))) {
                    val current = seen(subTarget - coin).appended(coin)
                    if (seen(subTarget).head == -999 || (seen(subTarget).length > current.length)) {
                        seen(subTarget) = current
                    }
                }
            }; ()
        }
        seen(target).toArray
    }

    def changeMakingListingBFS(coins: Array[Int], target: Int): Array[Int] = {
        val (seen, t) = (Map[Int, Array[Int]]((0, Array())), Queue[Int](0))
        while (t.nonEmpty) {
            val popped = t.dequeue()
            coins.foldLeft(((popped == target), seen, t)) { case ((found, seen, t), coin) =>
                if(!found) {
                    if(coin + popped <= target && !seen.contains(coin + popped)) {
                        seen(coin + popped) = seen.getOrElse(popped, Array[Int]()).appended(coin)
                        t.enqueue(coin + popped)
                    }
                }
                (found, seen, t)
            }
        }
        seen.getOrElse(target, Array[Int]())
    }

    def changeMakingCountBFS(coins: Array[Int], target: Int): Int = {
        val (seen, t) = (Map[Long, Int]((0L, 0)), Queue[Long](0L))
        while (t.nonEmpty) {
            val popped = t.dequeue()
            coins.foldLeft(((popped == target), seen, t)) { case ((found, seen, t), coin) =>
                if(!found) {
                    if(coin + popped <= target && !seen.contains(coin + popped)) {
                        seen(coin + popped) = seen.getOrElse(popped, 0) + 1
                        t.enqueue(coin + popped)
                    }
                }
                (found, seen, t)
            }
        }
        seen.getOrElse(target, -1)
    }


}
