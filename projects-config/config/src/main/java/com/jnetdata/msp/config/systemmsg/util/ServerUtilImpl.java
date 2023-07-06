package com.jnetdata.msp.config.systemmsg.util;

/**
 * Created by TF on 2019/3/27.
 */
public class ServerUtilImpl {

    public static final String GERONIMO_CLASS = "/org/apache/geronimo/system/main/Daemon.class";
    public static final String JBOSS_CLASS = "/org/jboss/Main.class";
    public static final String JETTY_CLASS = "/org/mortbay/jetty/Server.class";
    public static final String JONAS_CLASS = "/org/objectweb/jonas/server/Server.class";
    public static final String OC4J_CLASS = "/oracle/jsp/oc4jutil/Oc4jUtil.class";
    public static final String ORION_CLASS = "/com/evermind/server/ApplicationServer.class";
    public static final String PRAMATI_CLASS = "/com/pramati/Server.class";
    public static final String RESIN_CLASS = "/com/caucho/server/resin/Resin.class";
    public static final String REXIP_CLASS = "/com/tcc/Main.class";
    public static final String SUN7_CLASS = "/com/iplanet/ias/tools/cli/IasAdminMain.class";
    public static final String SUN8_CLASS = "/com/sun/enterprise/cli/framework/CLIMain.class";
    public static final String TOMCAT_CLASS = "/org/apache/catalina/startup/Bootstrap.class";
    public static final String WEBLOGIC_CLASS = "/weblogic/Server.class";
    public static final String WEBSPHERE_CLASS = "/com/ibm/websphere/product/VersionInfo.class";

    private static ServerUtilImpl entity = new ServerUtilImpl();

    public String isGeronimo() {
        Class temp =  entity.getClass();
        if (temp.getResource(GERONIMO_CLASS) != null) {
            return "geronimo";
        }
        return null;
    }

    public  String isJBoss() {
        Class temp =  entity.getClass();
        if (temp.getResource(JBOSS_CLASS) != null) {
            return "jBoss";
        }
        return null;
    }

    public String isJetty() {
        Class temp =  entity.getClass();
        if (temp.getResource(JETTY_CLASS) != null) {
            return "jetty";
        }
        return null;
    }

    public String isJOnAS() {
        Class temp =  entity.getClass();
        if (temp.getResource(JONAS_CLASS) != null) {
            return "jonas";
        }
        return null;
    }

    public String isOC4J() {
        Class temp =  entity.getClass();
        if (temp.getResource(OC4J_CLASS) != null) {
            return"oc4j";
        }
        return null;
    }

    public String isOrion() {
        Class temp =  entity.getClass();
        if (temp.getResource(ORION_CLASS) != null) {
            return"orion";
        }
        return null;
    }

    public String isPramati() {
        Class temp =  entity.getClass();
        if (temp.getResource(PRAMATI_CLASS) != null) {
           return "pramati";
        }
        return null;
    }

    public String isResin() {
        Class temp =  entity.getClass();
        if (temp.getResource(RESIN_CLASS) != null) {
           return"resin";
        }
        return null;
    }

    public String isRexIP() {
        Class temp =  entity.getClass();
        if (temp.getResource(REXIP_CLASS) != null) {
            return"rexIP";
        }
        return null;
    }

    public String isSun7() {
        Class temp =  entity.getClass();
        if (temp.getResource(SUN7_CLASS) != null) {
            return"sun7";
        }
        return null;
    }

    public String isSun8() {
        Class temp =  entity.getClass();
        if (temp.getResource(SUN8_CLASS) != null) {
            return"sun8";
        }
        return null;
    }

    public String isTomcat() {
        Class temp =  entity.getClass();
        if (temp.getResource(TOMCAT_CLASS) != null) {
            return "tomcat";
        }
        return null;
    }

    public String isWebLogic() {
        Class temp =  entity.getClass();
        if (temp.getResource(WEBLOGIC_CLASS) != null) {
            return "webLogic";
        }
        return null;
    }

    public String isWebSphere() {
        Class temp =  entity.getClass();
        if (temp.getResource(WEBSPHERE_CLASS) != null) {
            return "webSphere";
        }
        return null;
    }
}
