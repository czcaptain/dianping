import java.util.Arrays;

class Solution {

    // nums 1 2 3 4 k = 2
    //res 3 4 1 2
    // 1 2 3 4 5 6 7 原
    // 5 6 7 1 2 3 4
    public void rotate(int[] nums, int k) {
        // 1
        int length = nums.length;
        int []res = new int[length];  //o(n)
        for (int i = 0; i < length; i++) {
            res[i] = nums[i];
        }
        for (int i = 0; i < length; i++) {
            nums[i] = res[(i+k) % length];
            // nums[(i+k)%length] = res[i];
        }
//        nums = res;
//        Arrays.stream(nums).forEach(System.out::println);
    }

    public static void main(String[] args) {
        System.out.println(3 % 7);
    }
}

