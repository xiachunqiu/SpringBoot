package pers.xiachunqiu.obsidian.demo;

public interface Strategy {
    int execute(String userName);

    String getType();
}