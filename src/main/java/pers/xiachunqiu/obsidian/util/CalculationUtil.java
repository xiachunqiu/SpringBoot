package pers.xiachunqiu.obsidian.util;

import com.google.common.collect.Lists;
import pers.xiachunqiu.obsidian.dto.MemberDTO;
import pers.xiachunqiu.obsidian.dto.TeamDTO;
import pers.xiachunqiu.obsidian.dto.VotingInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CalculationUtil {
    private CalculationUtil() {
    }

    private static List<MemberDTO> memberList;
    private static List<TeamDTO> teamList;

    static {
        // init memberList
        initMemberList();

        // init teamList
        initTeamList();

        // calculate the weight of each member
        memberList.forEach(member -> {
            List<TeamDTO> teamListOfMember = getTeamListOfMemberByMemberId(member.getId());
            int teamNumberOfMember = teamListOfMember.size();
            double totalWeightOfMember = 0d;
            for (TeamDTO team : teamListOfMember) {
                totalWeightOfMember += 1d / team.getMemberIds().size();
            }
            double weightOfMember = totalWeightOfMember / teamNumberOfMember;
            member.setWeight(weightOfMember);
        });
    }

    public static void main(String[] args) {
        System.out.println("Outstanding individual voting score");
        printScore(memberList);
        System.out.println("Excellent team voting score");
        printScore(teamList);
    }

    private static void initMemberList() {
        // init teamList
        memberList = Lists.newArrayList();
        memberList.add(new MemberDTO().setId(1).setName("Kim").setMemberIdsOfVoting(Arrays.asList(2, 7, 4)));
        memberList.add(new MemberDTO().setId(2).setName("Hugh").setMemberIdsOfVoting(Arrays.asList(1, 5, 3)));
        memberList.add(new MemberDTO().setId(3).setName("Dell"));
        memberList.add(new MemberDTO().setId(4).setName("Ivor"));
        memberList.add(new MemberDTO().setId(5).setName("Christian"));
        memberList.add(new MemberDTO().setId(6).setName("Jo"));
        memberList.add(new MemberDTO().setId(7).setName("Fiona").setMemberIdsOfVoting(Arrays.asList(9, 8, 6)));
        memberList.add(new MemberDTO().setId(8).setName("Tracer"));
        memberList.add(new MemberDTO().setId(9).setName("Leo"));
    }

    private static void initTeamList() {
        // init teamList
        teamList = Lists.newArrayList();
        teamList.add(new TeamDTO().setName("QQE Team").setId(1).setMemberIds(Arrays.asList(1, 2, 3)).setMemberIdsOfVoting(Arrays.asList(1, 6, 5, 3, 4)));
        teamList.add(new TeamDTO().setName("APP Team").setId(2).setMemberIds(Arrays.asList(4, 5)));
        teamList.add(new TeamDTO().setName("Fix Team").setId(3).setMemberIds(Collections.singletonList(5)).setMemberIdsOfVoting(Arrays.asList(2, 8)));
        teamList.add(new TeamDTO().setName("CRM Team").setId(4).setMemberIds(Arrays.asList(6, 3)));
        teamList.add(new TeamDTO().setName("Test Team").setId(5).setMemberIds(Arrays.asList(7, 8)).setMemberIdsOfVoting(Arrays.asList(7, 6)));
        teamList.add(new TeamDTO().setName("DevOpsTeam Team").setId(6).setMemberIds(Collections.singletonList(9)));
    }

    private static List<TeamDTO> getTeamListOfMemberByMemberId(Integer memberId) {
        List<TeamDTO> teamListOfMember = Lists.newArrayList();
        for (TeamDTO team : teamList) {
            List<Integer> mIds = team.getMemberIds();
            if (mIds.contains(memberId)) {
                teamListOfMember.add(team);
            }
        }
        return teamListOfMember;
    }

    private static MemberDTO getMemberById(Integer memberId) {
        for (MemberDTO member : memberList) {
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    private static void printScore(List<? extends VotingInterface> list) {
        list.forEach(l -> {
            double score = 0d;
            List<Integer> memberIdsOfVoting = l.getMemberIdsOfVoting();
            if (memberIdsOfVoting != null) {
                for (Integer memberIdOfVoting : memberIdsOfVoting) {
                    MemberDTO memberOfVoting = getMemberById(memberIdOfVoting);
                    assert memberOfVoting != null : "No member found! Id is :" + memberIdOfVoting;
                    score += memberOfVoting.getWeight();
                }
            }
            l.setScore(score);
        });
        List<VotingInterface> sortList = list.stream().sorted(Comparator.comparing(VotingInterface::getScore).reversed()).collect(Collectors.toList());
        sortList.forEach(l -> {
            Double score = l.getScore();
            if (score > 0) {
                System.out.println(l.getName() + " : " + score);
            }
        });
        System.out.println("---------------------------------------------------");
    }
}