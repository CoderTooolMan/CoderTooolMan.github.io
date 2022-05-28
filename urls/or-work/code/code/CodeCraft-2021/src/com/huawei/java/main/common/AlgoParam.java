package com.huawei.java.main.common;

import java.io.File;

/**
 * AlgoParam
 * - parameters of algorithm
 */

public class AlgoParam {
    public static int seed = 3;
    public static double RC_EPS = 1.0e-6; // Tolerance;

    public String problem_name;
    public String author_id;
    public String author_name;
    public String algo_name;
    public String teston_prefix;
    public String teston_extension;
    public String path_data;
    public String path_result_sol;
    public String path_result_csv;
    public String version;

    public AlgoParam(String problem_name, String author_id, String author_name, String algo_name, String teston_prefix, String teston_extension, String path_data, String version) {
        this.problem_name = problem_name;
        this.author_id = author_id;
        this.author_name = author_name;
        this.algo_name = algo_name;
        this.teston_prefix = teston_prefix;
        this.teston_extension = teston_extension;;
        this.path_data = path_data;
        this.version = version;
    }

    public String csv_name() {
        return "result_" + algo_name + ".csv";
    }

    public void initial_result_csv() {
        try {
            File csv = new File(path_result_csv);
            Log.start(csv, false);
            Log.writeln("instance,author_id,author_name,algo,time,device_cost,energy_cost,total_cost");
            Log.end();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}