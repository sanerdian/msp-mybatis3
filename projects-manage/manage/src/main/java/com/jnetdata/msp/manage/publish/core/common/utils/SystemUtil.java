package com.jnetdata.msp.manage.publish.core.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;


public class SystemUtil {
    /**
     * 系统名
     */
    private String os_name;
    /**
     * 系统架构
     */
    private String os_arch;
    /**
     * 系统版本号
     */
    private String os_version;
    /**
     * 系统IP
     */
    private List<String> os_ip = new ArrayList<String>();
    /**
     * 系统MAC地址
     */
    private List<String> os_mac = new ArrayList<String>();
    /**
     * 系统时间
     */
    private Date os_date;
    /**
     * 系统CPU个数
     */
    private Integer os_cpus;
    /**
     * 系统用户名
     */
    private String os_user_name;
    /**
     * 用户的当前工作目录
     */
    private String os_user_dir;
    /**
     * 用户的主目录
     */
    private String os_user_home;

    /**
     * Java的运行环境版本
     */
    private String java_version;
    /**
     * java默认的临时文件路径
     */
    private String java_io_tmpdir;

    /**
     * java 平台
     */
    private String sun_desktop;

    /**
     * 文件分隔符  在 unix 系统中是＂／＂
     */
    private String file_separator;
    /**
     * 路径分隔符  在 unix 系统中是＂:＂
     */
    private String path_separator;
    /**
     * 行分隔符   在 unix 系统中是＂/n＂
     */
    private String line_separator;

    /**
     * 服务context
     **/
    private String server_context;
    /**
     * 服务器名
     */
    private String server_name;
    /**
     * 服务器端口
     */
    private Integer server_port;
    /**
     * 服务器地址
     */
    private String server_addr;
    /**
     * 获得客户端电脑的名字，若失败，则返回客户端电脑的ip地址
     */
    private String server_host;
    /**
     * 服务协议
     */
    private String server_protocol;

    private static SystemUtil SYSTEMUTIL;

    private SystemUtil() {
        super();
        init();
    }

    private SystemUtil(HttpServletRequest request) {
        super();
        init();
        /**
         * 额外信息 
         */
        ServerInfo(request);
    }

    public static SystemUtil getInstance() {
        if (SYSTEMUTIL == null) {
            synchronized (SystemUtil.class) {
                SYSTEMUTIL = new SystemUtil();
            }
        }
        return SYSTEMUTIL;
    }


    public static SystemUtil getInstance(HttpServletRequest request) {
        if (SYSTEMUTIL == null) {
            synchronized (SystemUtil.class) {
                SYSTEMUTIL = new SystemUtil(request);
            }
        } else {
            SYSTEMUTIL.ServerInfo(request);
        }
        return SYSTEMUTIL;
    }

    /**
     * 输出信息
     */
    public void PrintInfo() {
        Properties props = System.getProperties();
        System.out.println("Java的运行环境版本：" + props.getProperty("java.version"));
        System.out.println("默认的临时文件路径：" + props.getProperty("java.io.tmpdir"));
        System.out.println("操作系统的名称：" + props.getProperty("os.name"));
        System.out.println("操作系统的构架：" + props.getProperty("os.arch"));
        System.out.println("操作系统的版本：" + props.getProperty("os.version"));
        System.out.println("文件分隔符：" + props.getProperty("file.separator"));   //在 unix 系统中是＂／＂
        System.out.println("路径分隔符：" + props.getProperty("path.separator"));   //在 unix 系统中是＂:＂
        System.out.println("行分隔符：" + props.getProperty("line.separator"));   //在 unix 系统中是＂/n＂
        System.out.println("用户的账户名称：" + props.getProperty("user.name"));
        System.out.println("用户的主目录：" + props.getProperty("user.home"));
        System.out.println("用户的当前工作目录：" + props.getProperty("user.dir"));
    }

    /**
     * 初始化基本属性
     */
    private void init() {
        Properties props = System.getProperties();

        this.java_version = props.getProperty("java.version");
        this.java_io_tmpdir = props.getProperty("java.io.tmpdir");
        this.os_name = props.getProperty("os.name");
        this.os_arch = props.getProperty("os.arch");
        this.os_version = props.getProperty("os.version");
        this.file_separator = props.getProperty("file.separator");
        this.path_separator = props.getProperty("path.separator");
        this.line_separator = props.getProperty("line.separator");

        this.os_user_name = props.getProperty("user.name");
        this.os_user_home = props.getProperty("user.home");
        this.os_user_dir = props.getProperty("user.dir");


        this.sun_desktop = props.getProperty("sun.desktop");

        this.os_date = new Date();
        this.os_cpus = Runtime.getRuntime().availableProcessors();  
          
        /*try {
			ipMac();
		} catch (Exception e) {
			e.printStackTrace();
		}  */
        try {
            ipMac();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ip和mac地址
     *
     * @throws SocketException
     * @throws Exception
     */
    @SuppressWarnings("resource")  
   /* private void ipMac() throws Exception{  
        InetAddress address = InetAddress.getLocalHost();  
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);  
        ni.getInetAddresses().nextElement().getAddress();  
        byte[] mac = ni.getHardwareAddress();  
        String sIP = address.getHostAddress();  
        String sMAC = "";  
        Formatter formatter = new Formatter();  
        for (int i = 0; i < mac.length; i++) {  
            sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],  
                    (i < mac.length - 1) ? "-" : "").toString();  
  
        }  
        this.os_ip = sIP;  
        this.os_mac = sMAC;
    }  */

    private void ipMac() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        StringBuffer sb = new StringBuffer();
        NetworkInterface netInterface = null;
        Enumeration<InetAddress> addresses = null;
        byte[] bytes = null;
        while (allNetInterfaces.hasMoreElements()) {
            netInterface = allNetInterfaces.nextElement();
            addresses = netInterface.getInetAddresses();
            while (netInterface != null && netInterface.isUp() && addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    os_ip.add(ip.getHostAddress());
                }
            }
            bytes = netInterface.getHardwareAddress();
            if (netInterface != null && netInterface.isUp() && bytes != null && bytes.length == 6) {
                for (byte b : bytes) {
                    //与11110000作按位与运算以便读取当前字节高4位
                    sb.append(Integer.toHexString((b & 240) >> 4));
                    //与00001111作按位与运算以便读取当前字节低4位
                    sb.append(Integer.toHexString(b & 15));
                    sb.append("-");
                }
                sb.deleteCharAt(sb.length() - 1);
                os_mac.add(sb.toString().toUpperCase());
                sb.setLength(0);
            }
        }
        String defIp = "127.0.0.1";
        if (os_ip.size() > 1 && os_ip.contains(defIp)) {
            os_ip.remove(defIp);
        }
    }

    private void allIpMac() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        StringBuffer sb = new StringBuffer();
        NetworkInterface netInterface = null;
        Enumeration<InetAddress> addresses = null;
        byte[] bytes = null;
        while (allNetInterfaces.hasMoreElements()) {
            netInterface = allNetInterfaces.nextElement();
            addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    os_ip.add(ip.getHostAddress());
                }
            }
            bytes = netInterface.getHardwareAddress();
            if (netInterface != null && bytes != null && bytes.length == 6) {
                for (byte b : bytes) {
                    //与11110000作按位与运算以便读取当前字节高4位
                    sb.append(Integer.toHexString((b & 240) >> 4));
                    //与00001111作按位与运算以便读取当前字节低4位
                    sb.append(Integer.toHexString(b & 15));
                    sb.append("-");
                }
                sb.deleteCharAt(sb.length() - 1);
                os_mac.add(sb.toString());
                sb.setLength(0);
            }
        }
        String defIp = "127.0.0.1";
        if (os_ip.size() > 1 && os_ip.contains(defIp)) {
            os_ip.remove(os_ip);
        }
    }

    /**
     * 获取服务器信息
     *
     * @param request
     */
    public void ServerInfo(HttpServletRequest request) {
        this.server_name = request.getServerName();
        this.server_port = request.getServerPort();
        this.server_addr = request.getRemoteAddr();
        this.server_host = request.getRemoteHost();
        this.server_protocol = request.getProtocol();
        this.server_context = request.getContextPath();
    }

    /**
     * 获取linux计算机名称
     */

    public String getHostname() {
        return sysExec("/bin/hostname");
    }

    public String sysExec(String command) {
        String result = new String();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);

            InputStream inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(
                    inputstream);
            BufferedReader bufferedreader = new BufferedReader(
                    inputstreamreader);

            String line;
            while ((line = bufferedreader.readLine()) != null) {
                result += line;
                result += "\n";
            }
            try {
                if (proc.waitFor() != 0) {
                    System.err.println("exit value = " + proc.exitValue());
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public String getOs_name() {
        return os_name;
    }

    public String getOs_arch() {
        return os_arch;
    }

    public String gOss_version() {
        return os_version;
    }

    public List<String> getOs_ip() {
        return os_ip;
    }

    public List<String> getOs_mac() {
        return os_mac;
    }

    public Date getOs_date() {
        return os_date;
    }

    public Integer getOs_cpus() {
        return os_cpus;
    }

    public String getOs_user_name() {
        return os_user_name;
    }

    public String getOs_user_dir() {
        return os_user_dir;
    }

    public String getOs_user_home() {
        return os_user_home;
    }

    public String getJava_version() {
        return java_version;
    }

    public String getJava_io_tmpdir() {
        return java_io_tmpdir;
    }

    public String getSun_desktop() {
        return sun_desktop;
    }

    public String getFile_separator() {
        return file_separator;
    }

    public String getPath_separator() {
        return path_separator;
    }

    public String getLine_separator() {
        return line_separator;
    }

    public String getServer_context() {
        return server_context;
    }

    public String getServer_name() {
        return server_name;
    }

    public Integer getServer_port() {
        return server_port;
    }

    public String getServer_addr() {
        return server_addr;
    }

    public String getServer_host() {
        return server_host;
    }

    public String getServer_protocol() {
        return server_protocol;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String osVersion) {
        os_version = osVersion;
    }
}
