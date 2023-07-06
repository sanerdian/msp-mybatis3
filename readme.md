## 分支描述

 master 主分支，产品分支 会打标签 v1.0.0 ,v1.0.1                        v对外发布号.内部版本号.测试集成版本号

develop是开发随时提交的分支,当计划开发完毕，会合并入master，发布产品新版本

hotfix是产品分支报回来的bug解决后合入master分支及develop分支

feature是对应某个功能开发分出的分支，源自于develop合回到develop分支



CI描述

鉴于gitlab-runner版本问题，暂时用jenkins集成

1.  新建一个freestyle的item

2. 集成机公钥上传到gitlab里,在Credentials里加个能access本项目仓库的账号

3. branch 要标明 */develop

4. **Build Triggers**  H H * * 5 每周五 集成

5. 要启一个tomcat9来运行最后集成的结果

   docker run -it --restart=always -p 8080:8080 -v `pwd`/run/target/fastdevapp:/var/tomcat/webapps/ROOT --name tomcat9 -d oupula/tomcat9_jdk8

6. **Build** shell 

   `mvn clean package -Dmaven.test.skip=true
   docker stop tomcat9
   docker start tomcat9`

   

 这样就可以每周自动集成了





