package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostTargetEntity;
import com.capstone.exff.repositories.DonationPostTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonationPostTargetServicesImpl implements DonationPostTargetServices {

    private DonationPostTargetRepository donationPostTargetRepository;

    @Autowired
    public DonationPostTargetServicesImpl(DonationPostTargetRepository donationPostTargetRepository) {
        this.donationPostTargetRepository = donationPostTargetRepository;
    }

    @Override
    public Iterable<DonationPostTargetEntity> createDonationTargets(List<DonationPostTargetEntity> donationPostTargetEntities) {
        return donationPostTargetRepository.saveAll(donationPostTargetEntities);
    }

    @Override
    public void updateDonationTargets(List<DonationPostTargetEntity> updateTarget, List<Integer> removeIds, int donationPostId) {
        List<DonationPostTargetEntity> donationPostTargetEntities = donationPostTargetRepository.findByDonationPostId(donationPostId);

        for (Integer removeId :
                removeIds) {
            if (donationPostTargetEntities.stream().filter(target -> target.getId() == removeId).findAny().isPresent()) {
                donationPostTargetRepository.deleteById(removeId);
            }
        }
        for (DonationPostTargetEntity target :
                updateTarget) {
            if (donationPostTargetEntities
                    .stream()
                    .filter(t -> t.getId() == target.getId())
                    .findAny()
                    .isPresent()
            ) {
                target.setDonationPostId(donationPostId);
                donationPostTargetRepository.save(target);
            } else if (!(donationPostTargetEntities
                    .stream()
                    .filter(t -> target.getCategoryId() == t.getCategoryId())
                    .findAny()
                    .isPresent()
            )){
                target.setDonationPostId(donationPostId);
                donationPostTargetRepository.save(target);
            }
            }
    }


}
