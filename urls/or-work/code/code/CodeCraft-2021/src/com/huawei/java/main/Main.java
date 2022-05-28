package com.huawei.java.main;

import com.huawei.java.main.common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) throws IOException {

        AlgoParam param = new AlgoParam(
                "VM server scheduling",       // problem_name
                "201870122",     // TODO: your student id
                "Zou Yuyang",   // TODO: your name
                "algo_demo",        // TODO: your algo_name
                "training-2", ".txt",  // teston_prefix, teston_extension
                "../training-data",   // path_data
                "release"     //判题器
                //"local"     //本地
        );
        if (param.version.equals("local")) {
            preparePaths(param);
            ArrayList<File> files = readInstances(param);
            System.out.println(files.size() + " files > ");

            files.forEach(file -> System.out.println("\t" + file.getName()));

            for (File file : files) {
                long startTime = System.currentTimeMillis();

                Cache cache = new Cache(param);
                cache.fName = file.getName();
                Instance instance = new Instance();
                instance.readFile(cache, file);
                Solver solver = new Solver();
                solver.initialize(instance, cache);

                Log.start(new File(param.path_result_csv), true);
                Double time = 0.001 * (System.currentTimeMillis() - startTime);
                Log.writeln(file.getName() + "," + param.author_id + "," + param.author_name + "," + param.algo_name + "," + time);
                Log.end();
                cache.output();
            }
        } else {
            Cache cache = new Cache(param);
            Instance instance = new Instance();
            instance.readFile(cache, null);

            Solver solver = new Solver();
            solver.initialize(instance, cache);

        }


    }

    static void preparePaths(AlgoParam param) {
        try {
            File dir_result = new File("result");
            if (!dir_result.exists() || !dir_result.isDirectory()) {
                dir_result.mkdir();
            }
            File dir_problem = new File(dir_result, param.problem_name);
            if (!dir_problem.exists() || dir_problem.isDirectory() == false) {
                dir_problem.mkdir();
            }
            File dir_algo = new File(dir_problem, param.algo_name);
            if (dir_algo.exists() == false || dir_algo.isDirectory() == false) {
                dir_algo.mkdir();
            }

            param.path_result_sol = dir_algo.getAbsolutePath();
            param.path_result_csv = dir_problem.getAbsolutePath() + "/" + param.csv_name();
            param.initial_result_csv();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static ArrayList<File> readInstances(AlgoParam param) throws FileNotFoundException {
        File data = new File(param.path_data);

        Queue<File> que = new LinkedList<>();
        if (data.isDirectory()) {
            que.offer(data);
        }

        ArrayList<File> files = new ArrayList<>();
        while (que.isEmpty() == false) {
            File folder = que.poll();
            File[] tmpFiles = folder.listFiles();
            for (File file : tmpFiles) {
                if (file.isDirectory()) {
                    que.offer(file);
                } else {
                    String fname = file.getName();
                    if (fname.startsWith(param.teston_prefix) && fname.endsWith(param.teston_extension)) {
                        files.add(file);
                    }
                }
            }
        }

//        System.out.println(files.size() + " instances > ");
//        files.forEach(file -> System.out.println("\t" + file.getName()));
        return files;
    }
}
