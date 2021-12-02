package com.linjie.rest.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linjie.rest.data.Excel;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Title：TestBLE
 * @Package：com.linjie.rest.test
 * @Description：
 * @author：done
 * @date：2021/8/19 9:22
 */
public class OKhttpXY {

    // 发送请求准备
    private final String USER_AGENT = "Mozilla/5.0";
    MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
    private static String url = "http://119.29.179.102:8501/v1/models/xy_model:predict";

    public static void main(String[] args)  {


        float[][][] input = new float[1][1][10];
        float[][][][] in = new float[1][7][7][1];
        // 输入数据
        float[] temp1 = {-62	,-75	,-89	,-69	,-77	,-91	,-82	,-68	,-89	,-70};
        File file = new File("D:\\file\\DATA\\MyBluetooth\\5-14.xls");
        Excel excel = new Excel();
        try {
            List<List<String>> allData = excel.readExcel(file);
            JSONObject jsonObject = new JSONObject();
            List<String> temp = allData.get(0);
//            for (int i=0; i<temp.size(); i++){
//                input[0][0][i] = Float.parseFloat(temp.get(i));
//            }

            // 简单数据处理（需要和你训练模型的数据处理保持一致）
            for (int i = 0; i < temp1.length; i++) {
                if (temp1[i] <= 0){
                    temp1[i] = -(temp1[i] + 100) / 100;
                } else {
                    temp1[i] = (temp1[i] - 100) / 100;
                }
            }

            // 构造模型输入尺寸
            int ibeacon[][] = new int[2][10];
            int index1[] = {3,5,2,4,6,3,5,2,4,6};
            int index2[] = {6,6,5,5,5,3,3,2,2,2};
            for (int i=0; i<index1.length; i++){
                ibeacon[0][i] = index1[i];
                ibeacon[1][i] = index2[i];
            }

            for (int i=0; i<temp1.length; i++){
                in[0][ibeacon[0][i]][ibeacon[1][i]][0] = temp1[i];
            }

//            // 赋值
//            in[0][3][6][0] = temp1[0];
//            in[0][5][6][0] = temp1[1];
//            in[0][2][5][0] = temp1[2];
//            in[0][4][5][0] = temp1[3];
//            in[0][6][5][0] = temp1[4];
//            in[0][3][3][0] = temp1[5];
//            in[0][5][3][0] = temp1[6];
//            in[0][2][2][0] = temp1[7];
//            in[0][4][2][0] = temp1[8];
//            in[0][6][2][0] = temp1[9];

            // 创建http对象
            OKhttpXY test = new OKhttpXY();
            float[][][] X = test.calStarDeviation(input);  // 数据归一化不一致，暂不用

            // 减少数据量
//            float[][][] reduce_input = test.reduceValue(in);
            jsonObject.put("instances",in);

            System.out.println("发送请求数据为：" + jsonObject);

            // 发送post请求
            String result = test.sendPost(url, jsonObject);
            JSONObject json = JSONObject.parseObject(result);
            JSONArray predictions = json.getJSONArray("predictions");
            System.out.println("result为：" + result);
            JSONArray jsonArray = predictions.getJSONArray(0);
            List<Float> pre = jsonArray.toJavaList(Float.class);
            System.out.println("预测结果为：" + pre);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public String sendPost(String url, JSONObject json){
        String result = "";

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            System.out.println("response " + response);
            result = response.body().string();
            System.out.println("result " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 计算标准差
    public float[][][] calStarDeviation(float[][][] input){
        float[][][] X = new float[1][1][10];
        float temp = 0;
        float sum = 0;
        for (int i=0; i<input[0][0].length; i++){
            sum += input[0][0][i];
        }
        System.out.println("总和：" + sum);
        float average = sum / input[0][0].length;
        float standarDeviation = 0;
        float total = 0;
        for (int i=0; i<input[0][0].length; i++){
            total += (input[0][0][i] - average) * (input[0][0][i] - average);
        }
        standarDeviation = total / input[0][0].length;

        for (int i=0; i<input[0][0].length; i++){
            temp = (input[0][0][i] - average) / standarDeviation;
            X[0][0][i] = temp;
        }
        System.out.println(standarDeviation + " 均值：" + average);
        System.out.println("---------------------------");
        System.out.println(X[0][0][0]);
        return X;
    }

    // 减少数据值
    public float[][][] reduceValue(float[][][] input){
        float[][][] X = new float[1][1][10];
        for (int i=0; i<input[0][0].length; i++){
            if(input[0][0][i] <= 0){
                X[0][0][i] = (input[0][0][i] + 100)/100;
            } else {
                X[0][0][i] = (input[0][0][i] - 100)/100;
            }
        }
        return X;
    }

    // 将矩阵转置
    public static float[][] reverse(float temp[][]) {
        int N = temp.length;   // 行
        int M = temp[0].length;   // 列
        float input_t [][] = new float[M][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < M; j++) {
                input_t[j][i] = temp[i][j];
            }
        }
        return input_t;
    }

}
