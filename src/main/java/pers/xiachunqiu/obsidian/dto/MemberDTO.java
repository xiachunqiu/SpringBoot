package pers.xiachunqiu.obsidian.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class MemberDTO implements VotingInterface, Serializable {
    private Integer id;
    private String name;
    private Double score;
    private List<Integer> memberIdsOfVoting;
    private Double weight;
}