package pers.xiachunqiu.obsidian.demo;

public class Demo {
    public static void main(String[] args) {
        String key = "scan_qa_code";
        int result1 = StrategyUtil.get(key).execute("张三");
        System.out.println("key->>" + key + " result->>" + result1);

        key = "follow";
        int result2 = StrategyUtil.get(key).execute("李四");
        System.out.println("key->>" + key + " result->>" + result2);
    }
}