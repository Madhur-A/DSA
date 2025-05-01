



#include <functional>
#include <defines>
#include <changed.hpp>



int main() {
    std::vector<int> coins = {2,5,7};
    int target = 41;

    println(change_making::dp::top_down::listing::zero_to_target::get(coins, target));
    println(change_making::dp::top_down::listing::target_to_zero::get(coins, target));


//    av::println(change_making::dp::top_down::listing::target_to_zero::get({2,5,7}, 41));

//    av::println(change_making::dp::bottom_up::listing::zero_to_target::get({2,5,7}, 41));
//    av::println(change_making::dp::bottom_up::listing::target_to_zero::get({2,5,7}, 41));

//    av::println(change_making::dp::bottom_up::ways::permutations::get({2,5,7}, 41));
//    av::println(change_making::dp::top_down::ways::permutations::get({2,5,7}, 41));

//    av::println(change_making::dp::bottom_up::ways::combinations::get({2,5,7}, 41));
//    av::println(change_making::dp::top_down::ways::combinations::get({2,5,7}, 41));



    return 0;
}
