package com.linjie.rest.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linjie.rest.data.Excel;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title：TestBLE
 * @Package：com.linjie.rest.test
 * @Description：
 * @author：done
 * @date：2021/8/19 9:22
 */
public class TestZ {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String url = "http://192.168.110.100:8501/v1/models/z_model:predict";

    public static void main(String[] args)  {
        // 回归模型的请求数据列表
        float[][][] input = new float[1][1][10];
        File file = new File("D:\\file\\DATA\\MyBluetooth\\5-14.xls");
        Excel excel = new Excel();
        try {
            List<List<String>> allData = excel.readExcel(file);
            JSONObject jsonObject = new JSONObject();
            List<String> temp = allData.get(0);
            for (int i=0; i<temp.size(); i++){
                input[0][0][i] = Float.parseFloat(temp.get(i));
            }
            TestZ test = new TestZ();
            float[][][] X = test.calStarDeviation(input);

            // 减少数据量
            float[][][] reduce_input = test.reduceValue(input);
            jsonObject.put("instances",reduce_input);

            System.out.println("发送请求数据为：" + jsonObject);

            // post请求
            String result = test.sendPost(url, jsonObject);
            JSONObject json = JSONObject.parseObject(result);
            JSONArray predictions = json.getJSONArray("predictions");
            JSONArray jsonArray = predictions.getJSONArray(0);
            List<Float> pre = jsonArray.toJavaList(Float.class);
            int pre_y = pre.indexOf(pre.stream().max((o1, o2) -> o1.compareTo(o2)).get());
            System.out.println("分类结果为：" + pre_y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendPost(String url, JSONObject json){
        String result = "";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // 添加请求头
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity entity = new StringEntity(String.valueOf(json), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
//            System.out.println(response.getEntity());
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println("响应返回结果为：" + result);
            System.out.println("---------------------------");
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
//        System.out.println("总和：" + sum);
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
//        System.out.println(standarDeviation + " 均值：" + average);
        System.out.println("---------------------------");
        System.out.println(X[0][0][0]);
        return X;
    }

    // 减少数据值
    public float[][][] reduceValue(float[][][] input){
        float[][][] X = new float[1][1][10];
        for (int i=0; i<input[0][0].length; i++){
            if(input[0][0][i] <= 0){
                X[0][0][i] = input[0][0][i] + 105;
            } else {
                X[0][0][i] = input[0][0][i] - 100;
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
