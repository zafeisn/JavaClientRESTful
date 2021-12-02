# JavaClientRESTful
Use (RESTful) java client to call the model deployed by TensorFlow Serving

### OKHTTPTest.java
In Advance：Model deployment for RESTful  
[how to deployment?](https://blog.csdn.net/weixin_44324036/article/details/119761699?spm=1001.2014.3001.5501)  
The following is the code structure  
1、input prepare  
2、change input size（Need to be the same size as the training model）  
3、use OKHTTP to send post request   
4、return result  
