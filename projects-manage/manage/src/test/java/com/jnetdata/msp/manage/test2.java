//package com.jnetdata.msp.manage;
//
//import org.eclipse.jgit.api.CreateBranchCommand;
//import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.api.errors.GitAPIException;
//import org.eclipse.jgit.lib.Ref;
//import org.junit.Test;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class test2 {
//
//    @Test
//    public void test1() throws GitAPIException {
//
//        List<String> remoteBranch = new ArrayList<>();
//        remoteBranch.add("master");
//        Git git = Git.cloneRepository()
//                .setURI("ssh://git@47.93.246.70:2222/framework/module_demo.git")
//                .setBranchesToClone(remoteBranch)
//                .setDirectory(new File("d:\\temp"))
//                .call();
//        git.checkout().setCreateBranch(true).setName("master").setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).call();
//        git.pull().call();
//
//    }
//
//    @Test
//    public void test() throws GitAPIException, IOException {
//        File gitFile = new File("D:\\temp");
//        if (cloneRepository1(gitFile)) {
//            System.out.println("clone success");
//            checkFiles(gitFile);
//        } else {
//            System.out.println("clone failure");
//        }
//    }
//
//    private boolean cloneRepository1(File gitFile) throws GitAPIException, IOException {
//        Git git = null;
//        if (new File(gitFile.getAbsolutePath() + "/.git").exists()) {
//            git = Git.open(gitFile);
//            //检测dev分支是否已经存在 若不存在则新建分支
//            List<Ref> localBranch = git.branchList().call();
//            boolean isCreate = true;
//            for (Ref branch : localBranch) {
//                if (branch.getName().endsWith("dev")) {
//                    isCreate = false;
//                    break;
//                }
//            }
//            git.checkout().setCreateBranch(isCreate).setName("dev").setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).call();
//            git.pull().call();
//        } else {
//            List<String> remoteBranch = new ArrayList<>();
//            remoteBranch.add("master");
//            git = Git.cloneRepository().setURI("ssh://git@47.93.246.70:2222/framework/module_demo.git").setBranchesToClone(remoteBranch).setDirectory(gitFile).call();
//            git.checkout().setCreateBranch(true).setName("master").setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).call();
//            git.pull().call();
//        }
//        return true;
//    }
//
//    private void checkFiles(File file) {
//        File files[] = file.listFiles();
//        if (files != null && files.length > 0) {
//            for (File file1 : files) {
//                String fileName = file1.getName();
//                if (file1.isDirectory() && fileName.endsWith(".git")) continue;
//                System.out.println(fileName);
//                System.out.println("内容是：\n" + readFile(file1));
//                System.out.println("------------------------------------");
//                checkFiles(file1);
//            }
//        }
//    }
//
//    private String readFile(File file) {
//        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//        String line = null;
//        try {
//            br = new BufferedReader(new FileReader(file));
//            while ((line = br.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//            return sb.toString();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "";
//    }
//}
