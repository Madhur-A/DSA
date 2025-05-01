



#include <defines>

using namespace av;

namespace change_making::dp::bottom_up::ways::d2::permutations {
struct zero_to_target {
public:
	inline static int get(std::vector<int> const &coins, int target) {
		int const n = coins.size();
		std::vector<int> dp(target + 1, 0);
		dp[0] = 1;
		for(int sub_target = 0; sub_target < target + 1; ++sub_target) {
			for(int j = 0; j < n; ++j) {
				if(coins[j] <= sub_target) {
					dp[sub_target] += dp[sub_target - coins[j]];
				}
			}
		}

		return dp[target];
	}
};
}

namespace change_making::dp::bottom_up::ways::d2::combinations {
struct zero_to_target {
public:
	inline static int get(std::vector<int> const &coins, int target) {
		int const n = coins.size();
		std::vector<std::vector<int>> dp(n + 1, std::vector<int>(target + 1, 0));
		for(int i = 0; i < n + 1; ++i) { dp[i][0] = 1; }

		for(int i = 1; i < n + 1; ++i) {
			for(int sub_target = 1; sub_target < target + 1; ++sub_target) {
				dp[i][sub_target] = dp[i - 1][sub_target] + ((coins[i - 1] <= sub_target) ? dp[i][sub_target - coins[i - 1]] : 0);
			}
		}

		return dp[n][target];
	}
};
} // change_making::dp::bottom_up::ways [-]
	
namespace change_making::dp::bottom_up::listing::d2 {
struct zero_to_target { // builds incrementally from 0 to target
public:
	inline static std::vector<int> get(std::vector<int> const &coins, int target) {
		int const n = coins.size(), invalid = target + 1;
		std::vector<std::vector<int>> dp(target + 1, std::vector<int>(1, invalid));
		dp[0].clear();

		for(int i = 0; i < n; ++i) {
			for(int sub_target = coins[i]; sub_target <= target; ++sub_target) {
				if(dp[sub_target - coins[i]].empty() or dp[sub_target - coins[i]].front() != invalid) {
					if(dp[sub_target].front() == invalid or dp[sub_target].size() > dp[sub_target - coins[i]].size() + 1) {
						std::vector<int> new_coins_list(dp[sub_target - coins[i]].begin(), dp[sub_target - coins[i]].end());
						new_coins_list.push_back(coins[i]);
						dp[sub_target] = new_coins_list;
					}
				}
			}
		}

		return dp[target];
	}
};
}

namespace change_making::dp::top_down::listing {
struct zero_to_target {
public:
    inline static std::vector<int> get(std::vector<int> const &coins, int target) {
        int const n = coins.size(), invalid = target + 1;
        std::vector<std::vector<int>> dp(target + 1, std::vector<int>(1, invalid));
        dp[target].clear();

        std::function<std::vector<int>(int const&)> rx = [&](int const &remaining) {
            if(remaining  > target) { return std::vector<int>(1, invalid);  }
            if(dp[remaining].empty() || dp[remaining].front() != invalid) { return dp[remaining]; }

            for(int i = 0; i < n; ++i) {
                std::vector<int> transit_res = rx(remaining + coins[i]);
                if(transit_res.empty() or transit_res.front() != invalid) {
                    std::vector<int> possible(transit_res.begin(), transit_res.end());
                    possible.push_back(coins[i]);
                    if(dp[remaining].front() == invalid or dp[remaining].size() > possible.size()) {
                        dp[remaining] = possible;
                    }
                }
            }

            return dp[remaining];
        };

        return rx(0);
    }
};

struct target_to_zero {
public:
    inline static std::vector<int> get(std::vector<int> const &coins, int const &target) {
        int const n = coins.size();
        std::vector<std::vector<int>> dp(target + 1, std::vector<int>(1, -1));
        dp[0].clear();

        std::function<std::vector<int>(int const&)> rx = [&](int const &remaining) {
            if(remaining  < 0) { return std::vector<int>(1, -1); }
            if(dp[remaining].empty() or dp[remaining].front() != -1) { return dp[remaining]; }

            for(int index = 0; index < n; ++index) { // this loop is necessary due to c++'s optimization short circuits the otherwise Scala like recursion
                std::vector<int> possible = rx(remaining - coins[index]);
                if(possible.empty() or possible.front() != -1) {
                    std::vector<int> transit_res(possible.begin(), possible.end());
                    transit_res.push_back(coins[index]);
                    if(dp[remaining].front() == -1 or dp[remaining].size() > transit_res.size()) {
                        dp[remaining] = transit_res;
                    }
                }
            }

            return dp[remaining];
        };

        return rx(target);
    }
};
}


namespace change_making::dp::bottom_up::listing::d2 {
struct target_to_zero { // builds incrementally from target to 0 -**-
public:
	inline static std::vector<int> get(std::vector<int> const &coins, int target) {
		int const n = coins.size(), invalid = target + 1;
		std::vector<std::vector<int>> dp(target + 1, std::vector<int>(1, invalid));
		dp[target].clear();

		for(int i = 0; i < n; ++i) {
			for(int sub_target = target; sub_target <= coins[i]; --sub_target) {
				if(dp[sub_target - coins[i]].empty() or dp[sub_target - coins[i]].front() != invalid) {
					if(dp[sub_target].front() == invalid or dp[sub_target].size() > dp[sub_target - coins[i]].size() + 1) {
						std::vector<int> new_coins_list(dp[sub_target - coins[i]].begin(), dp[sub_target - coins[i]].end());
						new_coins_list.push_back(coins[i]);
						dp[sub_target] = new_coins_list;
					}
				}
			}
		}

		return dp[0];
	}
};
}

namespace change_making::dp::top_down::ways::d1::permutations {
struct zero_to_target {
public:
	inline static int get(std::vector<int> const &coins, int target) { // target to 0
		int const n = coins.size();
		std::vector<int> dp(target + 1, -1);
		dp[0] = 1;
		std::function<int(int const&)> rx = [&](int const &remaining) {
			if(remaining      <  0) { return 0; }
			if(dp[remaining] != -1) { return dp[remaining]; }

			int res = 0;
			for(int i = 0; i < n; ++i) {
				if(coins[i] <= remaining) {
					res += rx(remaining - coins[i]);
				}
			}
			
			return dp[remaining] = res;
		};
		
		return rx(target);
	}
};
}
	
namespace change_making::dp::top_down::ways::d2::permutations {
struct zero_to_target {
public:
	inline static int get(std::vector<int> const &coins, int target) {
		std::unordered_map<int, int> dp;
		dp[0] = 1;
		std::function<int(int const&)> rx = [&](int const &needed) {
			if(dp.count(needed)) { return dp[needed]; }
			if(needed < 0)       { return 0; }

			for(int const &coin: coins) {
				if(coin <= needed) {
					dp[needed] += rx(needed - coin);
				}
			}
			return dp[needed];
		};

		return rx(target);
	}
};
}


namespace change_making::dp::top_down::ways::d2::combinations {
struct target_to_zero {
public:
	inline static int get(std::vector<int> const &coins, int target) {
		int const n = coins.size();
		std::vector<std::vector<int>> dp(n + 1, std::vector<int>(target + 1, -1));
		std::function<int(int const&, int const&)> rx = [&](int const &index, int const &required) {
			if(index    ==      n) { return 0; }
			if(required == target) { return 1; }
			if(required  > target) { return 0; }
			if(dp[index][required] != -1) { return dp[index][required]; }

			return dp[index][required] = rx(index + 1, required) + rx(index, required + coins[index]);
		};

		return rx(0, 0);
	}
};
}


namespace change_making::dp::bottom_up::top_down::d2 {
struct target_to_zero {
public:
	inline static std::vector<int> get(std::vector<int> const &coins, int target) {
		int const n = coins.size(), invalid = target + 1;
		std::vector<std::vector<int>> dp(target + 1, std::vector<int>(1, invalid));
		dp[0].clear();

		std::function<std::vector<int>(int const&)> rx = [&](int const &remaining) {
			if(remaining  < 0) { return std::vector<int>(1, invalid);  }
			if(dp[remaining].empty() || dp[remaining].front() != invalid) { return dp[remaining]; }

			for(int i = 0; i < n; ++i) {
				std::vector<int> transit_res = rx(remaining - coins[i]);
				if(transit_res.empty() or transit_res.front() != invalid) {
					std::vector<int> possible(transit_res.begin(), transit_res.end());
					possible.push_back(coins[i]);
					if(dp[remaining].front() == invalid or dp[remaining].size() > possible.size()) {
						dp[remaining] = possible;
					}
				}
			}

			return dp[remaining];
		};

		return rx(target);
	}
};
} // dp::top_down::listing

