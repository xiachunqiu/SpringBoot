package pers.xiachunqiu.obsidian.dto;

import java.util.List;

public interface VotingInterface {
    List<Integer> getMemberIdsOfVoting();

    Double getScore();

    VotingInterface setScore(Double score);

    String getName();
}