//package com.jnetdata.msp.manage;
//
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.LogCommand;
//import org.eclipse.jgit.api.RemoteAddCommand;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.revwalk.RevCommit;
//import org.eclipse.jgit.transport.URIish;
//import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
//import org.junit.Test;
//
//import java.io.File;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class test3 {
//
//    @Test
//    public void test() throws GitAPIException {
//        String filepath = "D:\\temp2";
//        if (new File(filepath+"\\.git").isDirectory()){
//            System.out.println("该路径下已存在git项目。");
//        } else {
//            Git git = Git.cloneRepository().setURI("http://47.93.246.70/framework/module_demo.git")
//                    .setDirectory(new File(filepath))
//                    .setCredentialsProvider(provide())
//                    .call();
//            File file = git.getRepository().getDirectory();
//            String projectPath = file.getParent();
//            System.out.println(projectPath);
//        }
//    }
//
//    @Test
//    public void test2() {
//        try {
//            Git git = Git.open(new File("D:\\temp2\\.git"));
//            LogCommand logCommand = git.log();
//            Iterable<RevCommit> commitLst = logCommand.call();
//            List<String> allLogs = new ArrayList<String>();
//            for (RevCommit revCommit : commitLst) {
//                allLogs.add(revCommit.toString());
//            }
//            allLogs.forEach(e -> {
//                System.out.println(e);
//            });
//            git.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static UsernamePasswordCredentialsProvider provide() {
//        return new UsernamePasswordCredentialsProvider("li.donghui", "1593574862");
//    }
//    @Test
//    public void pushFiles()
//            throws GitAPIException, URISyntaxException {
//        File file = new File("E:\\IdeaProjects\\web\\.git");
//        Git git = Git.init().setDirectory(file).call();
//        RemoteAddCommand remoteAddCommand = git.remoteAdd();
//        remoteAddCommand.setName("master");
//        remoteAddCommand.setUri(new URIish("ssh://git@47.93.246.70:2222/fastdevplatform/web.git"));
//        remoteAddCommand.call();
//        git.add().addFilepattern(".").call();
//        git.commit().setMessage("init").call();
//        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider("wang.shubin", "123123123")).call();
//    }
//}
