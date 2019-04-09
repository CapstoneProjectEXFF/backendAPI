package com.capstone.exff.controllers;

import com.capstone.exff.entities.TransactionDetailEntity;
import com.capstone.exff.entities.TransactionEntity;
import com.capstone.exff.entities.UserEntity;

import java.util.List;

public class RelationshipWrapper implements Comparable<RelationshipWrapper>{
    private UserEntity user;
    private Integer mutualFriendNumber;

    public RelationshipWrapper(UserEntity user, Integer mutualFriendNumber) {
        this.user = user;
        this.mutualFriendNumber = mutualFriendNumber;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getMutualFriendNumber() {
        return mutualFriendNumber;
    }

    public void setMutualFriendNumber(Integer mutualFriendNumber) {
        this.mutualFriendNumber = mutualFriendNumber;
    }

    @Override
    public int compareTo(RelationshipWrapper o) {
        return this.getMutualFriendNumber().compareTo(o.getMutualFriendNumber());
    }
}
