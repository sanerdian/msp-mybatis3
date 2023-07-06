package com.jnetdata.msp.util;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UeditorUtil {

    public static String downFile(HttpServletRequest request, String content,String path,String basePath){
        if (StringUtils.isEmpty(content)) return content;
        List<String> srcs = getImgSrc(content);
        for (String res : srcs) {
            if(res.startsWith(getUrlPerfix(request))) continue;
            InputStream inputStream = HttpUtil.createGet(res).execute().bodyStream();
            String s = savePic(request, inputStream,path,basePath);
            content = content.replace(res, s);
        }
        return content;
    }

    public static List<String> getImgSrc(String htmlStr) {

        if (htmlStr == null) {
            return null;
        }

        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return new ArrayList<>(new HashSet<>(pics));
    }

    private static String getUrlPerfix(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }


    public static String savePic(HttpServletRequest request, InputStream inStream, String path, String basePath) {
        String url = "";
        String title = "";
        Map<String, Object> map = new HashMap<>();

            title = UUID.randomUUID() + ".jpg";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File imageFile = new File(path, title);

            //图片出入文件夹
            try {
                byte[] data = readInputStream(inStream);
                //new一个文件对象用来保存图片，默认保存当前工程根目录
                //创建输出流
                FileOutputStream outStream = new FileOutputStream(imageFile);
                //写入数据
                outStream.write(data);
                //关闭输出流
                outStream.close();


                String fileUrl = path.replace(basePath, "");
                fileUrl = fileUrl.replace("\\", "/");
                url = getUrlPerfix(request) + "/files" + fileUrl + "/" + title;

                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }


    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();

    }
}
