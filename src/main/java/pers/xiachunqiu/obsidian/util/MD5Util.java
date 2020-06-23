package pers.xiachunqiu.obsidian.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Map;

public class MD5Util {
    private MD5Util() {
    }

    // id name weight
    // id name member_ids

    private static List<List<Map<String, Object>>> teamList;
    private static Map<String, Map<String, Object>> memberWeightMapMap;

    static {
        teamList = Lists.newArrayList();
        memberWeightMapMap = Maps.newHashMap();
        Map<String, Object> kim = Maps.newHashMap();
        kim.put("name", "Kim");
        Map<String, Object> hugh = Maps.newHashMap();
        hugh.put("name", "Hugh");
        Map<String, Object> kimber = Maps.newHashMap();
        kimber.put("name", "Kimber");
        Map<String, Object> dell = Maps.newHashMap();
        dell.put("name", "Dell");
        Map<String, Object> ivor = Maps.newHashMap();
        ivor.put("name", "Ivor");
        Map<String, Object> christian = Maps.newHashMap();
        christian.put("name", "Christian");
        Map<String, Object> jo = Maps.newHashMap();
        jo.put("name", "Jo");
        Map<String, Object> fiona = Maps.newHashMap();
        fiona.put("name", "Fiona");
        Map<String, Object> tracer = Maps.newHashMap();
        tracer.put("name", "Tracer");
        Map<String, Object> leo = Maps.newHashMap();
        leo.put("name", "Leo");

        List<Map<String, Object>> qqeTeam = Lists.newArrayList();
        qqeTeam.add(kim);
        qqeTeam.add(hugh);
        qqeTeam.add(kimber);
        qqeTeam.add(dell);
        teamList.add(qqeTeam);

        List<Map<String, Object>> appTeam = Lists.newArrayList();
        appTeam.add(ivor);
        appTeam.add(christian);
        teamList.add(appTeam);

        List<Map<String, Object>> fixTeam = Lists.newArrayList();
        fixTeam.add(christian);
        teamList.add(fixTeam);

        List<Map<String, Object>> crmTeam = Lists.newArrayList();
        crmTeam.add(jo);
        crmTeam.add(kimber);
        crmTeam.add(dell);
        teamList.add(crmTeam);

        List<Map<String, Object>> testTeam = Lists.newArrayList();
        testTeam.add(fiona);
        testTeam.add(tracer);
        teamList.add(testTeam);

        List<Map<String, Object>> devOpsTeam = Lists.newArrayList();
        devOpsTeam.add(leo);
        teamList.add(devOpsTeam);
    }

    public static void main(String[] args) {
        System.out.println(teamList);
        // weight
        teamList.forEach(team -> {
            int teamMemberCount = team.size();
            team.forEach(member -> {
                String memberName = (String) member.get("name");
                Object memberWeightObject = memberWeightMapMap.get(memberName);
                Map<String, Object> memberWeightMap = memberWeightObject == null ? Maps.newHashMap() : (Map<String, Object>) memberWeightObject;
                double weightCurrent = 1d / teamMemberCount;
                Object weightTotalObject = memberWeightMap.get("weightTotal");
                Object teamCountObject = memberWeightMap.get("teamCount");
                double weightTotal = weightTotalObject == null ? 0d : (double) weightTotalObject;
                int teamCount = teamCountObject == null ? 0 : (int) teamCountObject;
                weightTotal += weightCurrent;
                teamCount++;
                double weight = weightTotal / teamCount;
                memberWeightMap.put("weight", weight);
                memberWeightMap.put("weightTotal", weightTotal);
                memberWeightMap.put("teamCount", teamCount);
                memberWeightMapMap.put(memberName, memberWeightMap);
            });
        });

        List<String> memberWhoVotedForHugh = Lists.newArrayList();
        memberWhoVotedForHugh.add("Kim");
        memberWhoVotedForHugh.add("Christian");
        memberWhoVotedForHugh.add("Kimber");
        memberWhoVotedForHugh.add("Dell");
        memberWhoVotedForHugh.forEach(m -> {
            Map<String, Object> memberWeightMap = memberWeightMapMap.get(m);
            double weight = (double) memberWeightMap.get("weight");

            Map<String, Object> hughMap = memberWeightMapMap.get("Hugh");
            Object scoreObject = hughMap.get("score");
            double score = scoreObject == null ? 0d : (double) scoreObject;
            score += weight;
            hughMap.put("score", score);
            memberWeightMapMap.put("Hugh", hughMap);
        });

        List<String> memberWhoVotedForFiona = Lists.newArrayList();
        memberWhoVotedForFiona.add("Leo");
        memberWhoVotedForFiona.add("Tracer");
        memberWhoVotedForFiona.add("Jo");
        memberWhoVotedForFiona.forEach(m -> {
            Map<String, Object> memberWeightMap = memberWeightMapMap.get(m);
            double weight = (double) memberWeightMap.get("weight");

            Map<String, Object> fionaMap = memberWeightMapMap.get("Fiona");
            Object scoreObject = fionaMap.get("score");
            double score = scoreObject == null ? 0d : (double) scoreObject;
            score += weight;
            fionaMap.put("score", score);
            memberWeightMapMap.put("Fiona", fionaMap);
        });

        List<String> memberWhoVotedForKim = Lists.newArrayList();
        memberWhoVotedForKim.add("Hugh");
        memberWhoVotedForKim.add("Fiona");
        memberWhoVotedForKim.add("Ivor");
        memberWhoVotedForKim.forEach(m -> {
            Map<String, Object> memberWeightMap = memberWeightMapMap.get(m);
            double weight = (double) memberWeightMap.get("weight");

            Map<String, Object> kimMap = memberWeightMapMap.get("Kim");
            Object scoreObject = kimMap.get("score");
            double score = scoreObject == null ? 0d : (double) scoreObject;
            score += weight;
            kimMap.put("score", score);
            memberWeightMapMap.put("Kim", kimMap);
        });
        for (Map.Entry<String, Map<String, Object>> entry : memberWeightMapMap.entrySet()) {
            Map<String, Object> map = entry.getValue();
            Object scoreObject = map.get("score");
            if (scoreObject != null) {
                System.out.println(entry.getKey() + ":" + scoreObject.toString());
            }
        }

        // qqe Kim Leo Christian Dell Ivor
        System.out.println("QQE Team:" + (
                (double) memberWeightMapMap.get("Kim").get("weight") +
                        (double) memberWeightMapMap.get("Leo").get("weight") +
                        (double) memberWeightMapMap.get("Christian").get("weight") +
                        (double) memberWeightMapMap.get("Dell").get("weight") +
                        (double) memberWeightMapMap.get("Ivor").get("weight")
        ));
        // test Fiona Kimber Jo
        System.out.println("Test Team:" + (
                (double) memberWeightMapMap.get("Fiona").get("weight") +
                        (double) memberWeightMapMap.get("Kimber").get("weight") +
                        (double) memberWeightMapMap.get("Jo").get("weight")
        ));
        // fix Hugh Tracer
        System.out.println("Fix Team:" + (
                (double) memberWeightMapMap.get("Hugh").get("weight") +
                        (double) memberWeightMapMap.get("Tracer").get("weight")
        ));
    }

    public static String encryptByMD5(String str) {
        if (StringUtils.isNotNull(str)) {
            str += "fauingh";
            str = DigestUtils.md5Hex(str) + DigestUtils.sha512Hex(str).substring(120);
        }
        return str;
    }
}