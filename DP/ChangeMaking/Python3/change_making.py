



from functools import lru_cache
from collections import deque, defaultdict

def change_making_dp_top_down(coins, target):
    @lru_cache(None)
    def dfs(amount):
        if amount == 0: return []
        if amount  < 0: return None

        res = None
        for coin in coins:
            transit = dfs(amount - coin)
            if transit is not None:
                possible = transit + [coin]
                if res is None or len(res) > len(possible):
                    if res is not None: res.clear()
                    res = possible[:]
        return res

    return dfs(target)

print(change_making_dp_top_down([1,2,5,10,20,50],   51))
print(change_making_dp_top_down([1,2,5,10,20,50],   50))
print(change_making_dp_top_down([1,2,5,10,20,50],    1))
print(change_making_dp_top_down([1,2,5,10,20,50],   44))
print(change_making_dp_top_down([1,2,5,10,20,50],    0))
print(change_making_dp_top_down([1,2,5,10,20,50],  101))

def change_making_dp_bottom_up(coins, target):
    dp = [None] * (target + 1)
    dp[0] = []
    for coin in coins:
        for t in range(0, target):
            if dp[t] is not None and t + coin <= target:
                aux = t + coin
                if dp[aux] is None or len(dp[aux]) > len(dp[t]) + 1:
                    dp[aux] = dp[t] + [coin]

    return dp[target]


print()
print(change_making_dp_bottom_up([1,2,5,10,20,50],  51))
print(change_making_dp_bottom_up([1,2,5,10,20,50],  50))
print(change_making_dp_bottom_up([1,2,5,10,20,50],   1))
print(change_making_dp_bottom_up([1,2,5,10,20,50],  44))
print(change_making_dp_bottom_up([1,2,5,10,20,50],   0))
print(change_making_dp_bottom_up([1,2,5,10,20,50], 101))


def change_making_vanilla_bfs(coins, target):
    seen, t = defaultdict(list), deque([0])
    while t:
        curr = t.popleft()
        if curr == target:
            return seen[curr]
        for coin in coins:
            if curr + coin <= target and curr + coin not in seen:
                seen[curr + coin] = seen[curr] + [coin]
                t.append(curr + coin)
    return []

def count_change(coins, target):
    seen, t = defaultdict(int), deque([0])
    while t:
        curr = t.popleft()
        if curr == target: return seen[curr]
        for coin in coins:
            if curr + coin <= target and curr + coin not in seen:
                seen[curr + coin] = seen[curr] + 1
                t.append(curr + coin)
    return -1

print()
print(change_making_vanilla_bfs([1,2,5,10,20,50],  51))
print(change_making_vanilla_bfs([1,2,5,10,20,50],  50))
print(change_making_vanilla_bfs([1,2,5,10,20,50],   1))
print(change_making_vanilla_bfs([1,2,5,10,20,50],  44))
print(change_making_vanilla_bfs([1,2,5,10,20,50],   0))
print(change_making_vanilla_bfs([1,2,5,10,20,50], 101))
