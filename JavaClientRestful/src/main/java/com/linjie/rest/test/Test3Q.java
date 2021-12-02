package com.linjie.rest.test;

import com.linjie.rest.data.Excel;

import java.io.File;
import java.util.List;

/**
 * @Title：Test3Q
 * @Package：com.linjie.rest.test
 * @Description：
 * @author：done
 * @date：2021/11/7 15:31
 */
public class Test3Q {
    float[] temp1 = {-65,	-56,	-69,	-69,	-65	,-76	,-82	,-79	,-62	,-67};
    float[] temp2 = {-54,	-67,	-68,	-68,	-66	,-73	,-71	,-76	,-70	,-69};
    float[] temp3 = {-54,	-66,	-68,	-68,	-66	,-74	,-70	,-78	,-70	,-69};
    float[] temp4 = {-76,	-66,	-62,	-66,	-83	,-72	,-71	,-85	,-71	,-68};

//    float[] temp1 = {-54	,-68,	-61,	-63,	-82,	-72,	-71,	-80	,-61	,-67};
//    float[] temp2 = {-50	,-68,	-61,	-65,	-78,	-72,	-71,	-91	,-61	,-66};
//    float[] temp3 = {-54	,-66,	-61,	-64,	-79,	-71,	-71,	-72	,-62	,-66};
//    float[] temp4 = {-54	,-66,	-59,	-64,	-80,	-71,	-70,	-78	,-62	,-67};
    float[] temp5 = {-56	,-66,	-60,	-65,	-82,	-71	,-71	,-81	,-72	,-68};
    float[] temp6 = {-100	,-100,	-60,	-66,	-88,	-72,	-71,	-100,	-100,	-68};
    float[] temp7 = {-100	,-100,	-60,	-65,	-83,	-72	,-71	,-100	,-100	,-68};
    float[] temp8 = {-100	,-100,	-60,	-65,	-88,	-72	,-71	,-100	,-100	,-68};
    float[] temp9 = {-100	,-100,	-60,	-66,	-63,	-72,	-70	,-100	,-100	,-68};
    float[] temp10 = {-100	,-100,	-60,	-66,	-62,	-72	,-71	,-100	,-100	,-68};
    float[] temp11 = {-100	,-100,	-91,	-66,	-62,	-72,	-71	,-100	,-100	,-64};

    float rawData[][] = {temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8,temp9,temp10,temp11};

    public void readTest(){
        float[] mean = getMean(rawData);
        float[] std = getStd(rawData, mean);
        float[][] newData = getNewData(rawData, mean);
        float[] max = getMax(newData);
        System.out.println(max[0]);
        System.out.println(std[0]);
        System.out.println(newData[0][2]);
        for (int i=0; i<max.length; i++){
            if (max[i] > std[i]*2){
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        }
    }

    /**
     * 获取数组最大值
     * @param data
     * @return
     */
    public float[] getMax(float[][] data){
        float[] max = new float[data.length];
        for (int i=0; i<data.length; i++){
            float m = data[i][0];
            for (int j=0; j<data[i].length; j++){
                if (data[i][j] > m){
                    m = data[i][j];
                }
            }
            max[i] = m;
        }
        return max;
    }

    /**
     * 计算平均值
     * @param data
     * @return
     */
    public float[] getMean(float[][] data){
        float mean[] = new float[data.length];
        for (int i=0; i<data.length; i++){
            float temp = 0;
            for (int j=0; j<data[i].length; j++){
                temp += data[i][j];
            }
            temp = temp / data[i].length;
            mean[i] = temp;
        }
        return mean;
    }

    /**
     * 计算方差
     * @param data
     * @param mean
     * @return
     */
    public float[] getStd(float[][] data, float[] mean){
        float[] std = new float[data.length];
        for (int i=0; i<data.length; i++){
            float temp = 0;
            for (int j=0; j<data[i].length; j++){
                temp += (data[i][j] - mean[i]) * (data[i][j] - mean[i]);
            }
            temp = (float) Math.sqrt(Double.valueOf(String.valueOf(temp/data[i].length)));
            std[i] = temp;
        }
        return std;
    }

    /**
     * 得到新数据
     * @param data
     * @param mean
     * @return
     */
    public float[][] getNewData(float[][] data, float[] mean){
        float[][] newData = new float[data.length][10];
        for (int i=0; i<data.length; i++){
            for (int j=0; j<data[i].length; j++){
                newData[i][j] = data[i][j] - mean[i];
            }
        }
        return newData;
    }

    public static void main(String[] args) {
        Test3Q test3Q = new Test3Q();
        test3Q.readTest();
    }

}
