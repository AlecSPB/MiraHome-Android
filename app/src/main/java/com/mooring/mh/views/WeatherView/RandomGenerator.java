package com.mooring.mh.views.WeatherView;

import java.util.Random;

/**
 * Created by Will on 16/3/24.
 */
public class RandomGenerator {

    private static final java.util.Random RANDOM = new java.util.Random();


    /**
     * 区间随机
     *
     * @param lower
     * @param upper
     * @return
     */
    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    /**
     * 上界随机 float
     *
     * @param upper
     * @return
     */
    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    /**
     * 上界随机 int
     *
     * @param upper
     * @return
     */
    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }

    /**
     * 获取范围内随机数
     *
     * @param smallistNum
     * @param BiggestNum
     * @return
     */
    public int getRandomNum(int smallistNum, int BiggestNum) {
        Random random = new Random();
        return (Math.abs(random.nextInt()) % (BiggestNum - smallistNum + 1)) + smallistNum;
    }

    /**
     * 随机产生雨点起始位置
     *
     * @param height
     * @param width
     * @return
     */
    public int[] getLine(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        int temp = getRandomWidth(width);
        for (int i = 0; i < 4; i += 4) {
            tempCheckNum[i] = temp;
            tempCheckNum[i + 1] = (int) (Math.random() * height / 4);
            tempCheckNum[i + 2] = temp;
            tempCheckNum[i + 3] = (int) (Math.random() * height / 2);
        }
        return tempCheckNum;
    }

    /**
     * 随机产生制定参数数值
     *
     * @param width
     * @return
     */
    public int getRandomWidth(int width) {
        return (int) (Math.random() * width);
    }


}
