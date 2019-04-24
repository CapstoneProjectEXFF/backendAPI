package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.entities.DonationPostTargetEntity;

import java.util.ArrayList;
import java.util.List;

public interface DonationPostTargetServices {
    Iterable<DonationPostTargetEntity> createDonationTargets(List<DonationPostTargetEntity> donationPostTargetEntities);
    void updateDonationTargets(List<DonationPostTargetEntity> donationPostTargetEntities, List<Integer> removeIds, int donationPostId);
}
