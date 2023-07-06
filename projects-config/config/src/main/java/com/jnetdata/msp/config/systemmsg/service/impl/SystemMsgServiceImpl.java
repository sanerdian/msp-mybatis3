package com.jnetdata.msp.config.systemmsg.service.impl;

import com.jnetdata.msp.config.systemmsg.mapper.SystemMsgMapper;
import com.jnetdata.msp.config.systemmsg.model.DatabaseMsg;
import com.jnetdata.msp.config.systemmsg.model.SystemMsg;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.config.systemmsg.service.DatabaseMsgService;
import com.jnetdata.msp.config.systemmsg.service.SystemMsgService;
import com.jnetdata.msp.config.systemmsg.util.ServerUtil;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by TF on 2019/3/27.
 */
@Service
@Slf4j
public class SystemMsgServiceImpl extends BaseServiceImpl<SystemMsgMapper, SystemMsg> implements SystemMsgService {

    @Autowired
    DatabaseMsgService databaseMsgService;

    /**
     * 获取系统配置信息
     * @return
     */
    @Override
    public Map<String, Object> getSystemMsg() throws Exception{
        DatabaseMsg dataMsg = databaseMsgService.getMsg();

        SystemMsg msg = new SystemMsg();
        msg = getLocalMac(msg);
        msg = getOsType(msg);
        msg = getLocalIP(msg);
        msg = getPath(msg);
        msg = getMemery(msg);
        msg = getVessel(msg);

        Map<String,Object> map = new HashMap<>();
        map.put("system",msg);
        map.put("data",dataMsg);

        return map;
    }

    /**
     * 获取mac地址
     * @return
     */
    public SystemMsg getLocalMac(SystemMsg msg)  {
        StringBuffer sb = new StringBuffer();
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[]    mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            for(int i=0; i<mac.length; i++) {
                if(i!=0) {
                    sb.append("-");
                }
                int temp = mac[i]&0xff;//字节转换为整数
                String str = Integer.toHexString(temp);
                if(str.length()==1) {
                    sb.append("0"+str);
                }else {
                    sb.append(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setMac(sb.toString().toUpperCase());
        return msg;
    }

    /**
     * 获取操作系统类型
     * 名称，架构，版本
     * @return
     */
    public SystemMsg getOsType(SystemMsg msg)  {

        Properties props=System.getProperties(); //获得系统属性集
        msg.setSystemName(props.getProperty("os.name"));//操作系统名称
        msg.setFramework(props.getProperty("os.arch"));//操作系统构架
        msg.setSystemVersion(props.getProperty("os.version"));//操作系统版本

        return msg;
    }

    /**
     * 获取ip地址
     * @param msg
     * @return
     */
    public SystemMsg getLocalIP(SystemMsg msg){
        StringBuilder sb = new StringBuilder();

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress().toString()+"\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setIp(sb.toString());

        return msg;
    }

    /**
     * 获取当前运行路径
     * @return
     */
    public SystemMsg  getPath(SystemMsg msg) {
        File directory = new File("");//设定为当前文件夹File directory = new File("..")
        String path="";

        try{
            path=directory.getCanonicalPath();//获取标准的路径
//            path=directory.getAbsolutePath();//获取绝对路径
        }catch(Exception e){

        }
        msg.setWebAddress(path);
        return msg;
    }

    /**
     * 获取内存使用率,cpu核心数
     * @return
     */
    public SystemMsg getMemery(SystemMsg msg) {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalvirtualMemory = osmxb.getTotalSwapSpaceSize()/1024/1024/1024;// 总的物理内存+虚拟内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize()/1024/1024/1024;    // 剩余的物理内存
        Long userRam = totalvirtualMemory - freePhysicalMemorySize;//使用的内存
        int availProcessors = Runtime.getRuntime().availableProcessors();//cpu核心数

        msg.setMemorySum(String.valueOf(totalvirtualMemory));
        msg.setMemoryFree(String.valueOf(freePhysicalMemorySize));
        msg.setMemoryUse(String.valueOf(userRam));
        msg.setCpuNum(String.valueOf(availProcessors));
        return msg;
    }


    public SystemMsg getVessel(SystemMsg msg){
        Map<String,String> map = System.getenv();
        ServerUtil util = new ServerUtil();

        msg.setUserName(map.get("USERNAME"));//用户名
        msg.setCompaterName(map.get("COMPUTERNAME"));//计算机名
        msg.setJavaEnvironment(System.getProperty("java.version"));//jdk版本
        msg.setJavaAddress(System.getProperty("java.home"));//jdk路径
        msg.setCode(System.getProperty("file.encoding"));//系统默认字符编码
        msg.setLanguage(System.getProperty("user.language"));
        msg.setVessel(util.getServerId());
        return msg;
    }

}
