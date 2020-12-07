package pers.xiachunqiu.obsidian.demo;

public class FollowStrategy implements Strategy {
    @Override
    public int execute(String userName) {
        System.out.println("用户" + userName + "关注了公众号");
        return 1;
    }

    @Override
    public String getType() {
        return OperationType.FOLLOW.getValue();
    }
}