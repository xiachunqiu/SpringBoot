package pers.xiachunqiu.obsidian.demo;

public class ScanQACodeStrategy implements Strategy {
    @Override
    public int execute(String userName) {
        System.out.println("用户" + userName + "扫描了二维码");
        return 1;
    }

    @Override
    public String getType() {
        return OperationType.SCAN_QA_CODE.getValue();
    }
}