package com.jnetdata.msp.config.systemmsg.util;

/**
 * Created by TF on 2019/3/27.
 */
public class ServerUtil {

    public String getServerId() {
        ServerUtilImpl impl = new ServerUtilImpl();
        String temp = "";

        temp = getServer(impl.isGeronimo(),temp);
        temp = getServer(impl.isJBoss(),temp);
        temp = getServer( impl.isJetty(),temp);
        temp = getServer( impl.isJOnAS(),temp);
        temp = getServer(impl.isOC4J(),temp);
        temp = getServer(impl.isOrion(),temp);
        temp = getServer(impl.isPramati(),temp);
        temp = getServer(impl.isResin(),temp);
        temp = getServer( impl.isRexIP(),temp);
        temp = getServer(impl.isSun7(),temp);
        temp = getServer(impl.isSun8(),temp);
        temp = getServer(impl.isTomcat(),temp);
        temp = getServer(impl.isWebSphere(),temp);

        return temp;

    }

    public String getServer(String id,String temp){
        if(id!=null){
            temp = id;
        }
        return temp;
    }

}
