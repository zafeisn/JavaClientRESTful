package com.linjie.rest.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.linjie.rest.data.Mnist;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title：TestRest
 * @Package：com.linjie.rest.test
 * @Description：
 * @author：done
 * @date：2021/8/18 14:51
 */
public class TestRest {

    private final String USER_AGENT = "Mozilla/5.0";
    private static String url = "http://192.168.110.100:8501/v1/models/saved_model:predict";

    public static void main(String[] args) {

        // 加载数据
        Mnist mnist = new Mnist();
        mnist.load();
        Mnist.Data testData = mnist.getTestData(0);
        float[] input = testData.input;
        System.out.println("data[0]的真实标签为：" + testData.label);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("instances", input);
        System.out.println("发送的请求数据为：" + jsonObject);
        System.out.println("---------------------------");

        // 发送post请求
        TestRest testRest = new TestRest();
        String result = testRest.sendPost(url, jsonObject);
        JSONObject json = JSONObject.parseObject(result);
        JSONArray predictions = json.getJSONArray("predictions");
        JSONArray jsonArray = predictions.getJSONArray(0);
        List<Float> pre = jsonArray.toJavaList(Float.class);
        int pre_y = pre.indexOf(pre.stream().max((o1, o2) -> o1.compareTo(o2)).get());
        System.out.println("data[0]的分类结果为：" + pre_y);

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
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println("响应返回结果为：" + result);
            System.out.println("---------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
