package com.jnetdata.msp.zdff.publishdistribution.service.impl;
import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.config.config.service.impl.ConfigModelServiceImpl;

public  class ConfigUtil {

	/**
	 * 配置项查询,根据配置项里的key查询value,key不允许重复(1对1查询)
	 * @author lipenghe
	 * @return String value
	 **/

	public static String getSysConfigByKey(String uniqueKey) {
//		SysconfigService configService=null;
//		configService=new SysconfigService();
//		String value=configService.getValueByUnique(uniqueKey);
		ConfigModelService configModelService=null;
		configModelService=new ConfigModelServiceImpl();
//		String value = configModelService.getUploadPath(uniqueKey);
//		String value = configModelService.getPath("dir_pub");
		String value = configModelService.getPath(uniqueKey);
		if(value!=null){
			value=value.replaceAll("\\\\","/");
		}
		return value;
	}

	public static void main(String[] args) {
		System.out.println(getSysConfigByKey("dir_pub"));
		/*long[] longArr={1000,291,270,1040,319,1041,1531,1574,1594};
		int[] intArr={1,1,1,2,2,2,3,3,3};
		for(int i=0;i<longArr.length;i++){
    		long id=longArr[i];
    		int type=intArr[i];
    		Map<String, Object> map=ConfigUtil.getTableInfo(id,type);
    		//System.out.println(id+"  " +type+"  "+map.get("TABLENAME")+map.get("PK"));
		}*/
		/*String str=ConfigUtil.getPublishPath("upload",411,2,"JPG_20140921153020373_8487.jpg");
		//System.out.println(ConfigUtil.getFileName("rm"));
		//System.out.println(ConfigUtil.getFilePath("upload",1000, 1, "jpg"));
		//System.out.println(ConfigUtil.createFolders("D:\\AAA1\\111\\baidu\\201409\\20140924\\JPG_20140924001210284_55942.jpg"));
		//System.out.println(ConfigUtil.createFolders("D:\\AAA2\\upload"));*/
		//List<Map<String,Object>> list=ConfigUtil.getFieldGroup(1168);
		//System.out.println(list.size());
		//String pa=ConfigUtil.getSysConfigByKey("pub");
		//System.out.println(pa);
		//System.out.println(ConfigUtil.getWebFilePath("webpic","PNG_20141130222439265_80937.png",1020,1));
		//System.out.println(ConfigUtil.getDownloadPath("upload","12306","TXT_20141019111420816_60308.txt"));
		//System.out.println(ConfigUtil.getChnlPath(1277));
		/*
		System.out.println(ConfigUtil.getWebFilePath("systemp", "PNG_20141130222439265_80937.png"));
		System.out.println(ConfigUtil.getRelativeWebsiteChnlUrl(1321,"index","htm"));
		System.out.println(ConfigUtil.getRelativeWebsiteChnlUrl(1340,"index","html"));
		StringBuffer sb=new StringBuffer();
		sb.insert(0, "/").insert(0,"a");
		System.out.println(sb.toString());
		*/
		//System.out.println(ConfigUtil.getRelativeUrl(406,408,false,"index","html"));
		//System.out.println(ConfigUtil.getRelativeUrl(1321,408,false,"index","html"));
		//System.out.println(ConfigUtil.getRelativeUrl(1321,1120,true,"index","html"));
		/*String path=ConfigUtil.getRealChnlPublishFolder("pub", 1140, null);
		System.out.println(path);
		System.out.println(ConfigUtil.getRealAppendixPath(path,"zkjw","JPG_20150918191854150_82737.jpg",new Date()));
		System.out.println(ConfigUtil.getRealAppendixPath("pub",path,"JPG_20150918191854150_82737.jpg",new Date()));
		System.out.println(ConfigUtil.getRealDocPublishFoleder("pub",path,new Date(),"123.html"));
		System.out.println(ConfigUtil.getRealDocPublishFoleder("pub",path,new Date(),"123.html"));
		System.out.println(ConfigUtil.getAbsoluteChnlUrl(1343,null));*/
//		System.out.println(ConfigUtil.getFullChnlUrl(1874, "ss", "html"));
	}
}
