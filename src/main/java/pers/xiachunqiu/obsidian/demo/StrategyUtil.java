package pers.xiachunqiu.obsidian.demo;


import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyUtil {
    private StrategyUtil() {
    }

    private static Map<String, Strategy> map;

    static {
        List<Strategy> strategies = Lists.newArrayList();
        strategies.add(new ScanQACodeStrategy());
        strategies.add(new FollowStrategy());
        map = strategies.stream().collect(Collectors.toMap(Strategy::getType, strategy -> strategy));
    }

    public static Strategy get(String type) {
        return map.get(type);
    }
}