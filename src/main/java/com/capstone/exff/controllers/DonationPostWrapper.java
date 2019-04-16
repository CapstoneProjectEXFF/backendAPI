package com.capstone.exff.controllers;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.entities.DonationPostTargetEntity;

import java.util.ArrayList;

public class DonationPostWrapper {
    private DonationPostEntity donationPost;
    private ArrayList<String> urls;
    private ArrayList<String> newUrls;
    private ArrayList<Integer> removedUrlIds;
    private ArrayList<DonationPostTargetEntity> targets;
    private ArrayList<Integer> removeTargets;

    public DonationPostEntity getDonationPost() {
        return donationPost;
    }

    public void setDonationPost(DonationPostEntity donationPost) {
        this.donationPost = donationPost;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public ArrayList<DonationPostTargetEntity> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<DonationPostTargetEntity> targets) {
        this.targets = targets;
    }

    public ArrayList<String> getNewUrls() {
        return newUrls;
    }

    public void setNewUrls(ArrayList<String> newUrls) {
        this.newUrls = newUrls;
    }

    public ArrayList<Integer> getRemovedUrlIds() {
        return removedUrlIds;
    }

    public void setRemovedUrlIds(ArrayList<Integer> removedUrlIds) {
        this.removedUrlIds = removedUrlIds;
    }

    public ArrayList<Integer> getRemoveTargets() {
        return removeTargets;
    }

    public void setRemoveTargets(ArrayList<Integer> removeTargets) {
        this.removeTargets = removeTargets;
    }
}
