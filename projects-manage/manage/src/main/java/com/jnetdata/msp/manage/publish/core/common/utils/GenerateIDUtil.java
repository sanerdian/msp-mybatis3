package com.jnetdata.msp.manage.publish.core.common.utils;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GenerateIDUtil {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static long nodeid = getLocalMac();

    public static long generateID() {
        if (counter.get() > 9999) {
            counter.set(1);
        }
        long time = System.currentTimeMillis();
        return time * 10000 + nodeid + incrementAndGet();
    }

    public static String generateIDStr() {
        return UUID.randomUUID().toString();
    }

    private static long incrementAndGet() {
        return counter.incrementAndGet();
    }

    /**
     * 该方法被废弃:当有虚拟网卡会获取虚拟网卡或者linux系统获取为127.0.0.1对应的网卡
     *
     * @return
     */
	/*public static long getLocalMac() {
		// 获取网卡，获取地址
		try {
			InetAddress ia = InetAddress.getLocalHost();
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// 字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			//System.out.println("网卡:"+sb.toString().toUpperCase());
			return Math.abs(sb.toString().hashCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigDecimal(Math.random() * 100).longValue();

	}*/
    public static long getLocalMac() {
        // 获取网卡，获取地址
        try {
            String mac = SystemUtil.getInstance().getOs_mac().get(0);
            System.out.println("mac:" + mac);
            return Math.abs(mac.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(Math.random() * 100).longValue();

    }

    public static void main(String[] args) {
        //getLocalMac();
        System.out.println(System.currentTimeMillis() * 10000);
        System.out.println(GenerateIDUtil.generateID());
        System.out.println(GenerateIDUtil.generateID());
        System.out.println(GenerateIDUtil.generateID());
    }

}