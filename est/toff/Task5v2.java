import java.util.Scanner;

public class Task5v2 {
    static final Long[] nums = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 11L, 22L, 33L, 44L, 55L, 66L, 77L, 88L, 99L, 111L, 222L, 333L, 444L, 555L, 666L, 777L, 888L, 999L, 1111L, 2222L, 3333L, 4444L, 5555L, 6666L, 7777L, 8888L, 9999L, 11111L, 22222L, 33333L, 44444L, 55555L, 66666L, 77777L, 88888L, 99999L, 111111L, 222222L, 333333L, 444444L, 555555L, 666666L, 777777L, 888888L, 999999L, 1111111L, 2222222L, 3333333L, 4444444L, 5555555L, 6666666L, 7777777L, 8888888L, 9999999L, 11111111L, 22222222L, 33333333L, 44444444L, 55555555L, 66666666L, 77777777L, 88888888L, 99999999L, 111111111L, 222222222L, 333333333L, 444444444L, 555555555L, 666666666L, 777777777L, 888888888L, 999999999L, 1111111111L, 2222222222L, 3333333333L, 4444444444L, 5555555555L, 6666666666L, 7777777777L, 8888888888L, 9999999999L, 11111111111L, 22222222222L, 33333333333L, 44444444444L, 55555555555L, 66666666666L, 77777777777L, 88888888888L, 99999999999L, 111111111111L, 222222222222L, 333333333333L, 444444444444L, 555555555555L, 666666666666L, 777777777777L, 888888888888L, 999999999999L, 1111111111111L, 2222222222222L, 3333333333333L, 4444444444444L, 5555555555555L, 6666666666666L, 7777777777777L, 8888888888888L, 9999999999999L, 11111111111111L, 22222222222222L, 33333333333333L, 44444444444444L, 55555555555555L, 66666666666666L, 77777777777777L, 88888888888888L, 99999999999999L, 111111111111111L, 222222222222222L, 333333333333333L, 444444444444444L, 555555555555555L, 666666666666666L, 777777777777777L, 888888888888888L, 999999999999999L, 1111111111111111L, 2222222222222222L, 3333333333333333L, 4444444444444444L, 5555555555555555L, 6666666666666666L, 7777777777777777L, 8888888888888888L, 9999999999999999L, 11111111111111111L, 22222222222222222L, 33333333333333333L, 44444444444444444L, 55555555555555555L, 66666666666666666L, 77777777777777777L, 88888888888888888L, 99999999999999999L, 111111111111111111L, 222222222222222222L, 333333333333333333L, 444444444444444444L, 555555555555555555L, 666666666666666666L, 777777777777777777L, 888888888888888888L, 999999999999999999L, 1111111111111111111L, 2222222222222222222L, 3333333333333333333L, 4444444444444444444L, 5555555555555555555L, 6666666666666666666L, 7777777777777777777L, 8888888888888888888L};
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        long min = scan.nextLong();
        long max = scan.nextLong();
        scan.close();

        int count = 0;
        for (Long num : nums)
            if (num >= min && num <= max) count++;
        
        System.out.println(count);
    }
}