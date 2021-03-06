package com.capstone.exff.controllers;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.*;
import com.capstone.exff.services.ItemServices;
import com.capstone.exff.services.TransactionDetailServices;
import com.capstone.exff.services.TransactionServices;
import com.capstone.exff.utilities.ExffMessage;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.capstone.exff.constants.ExffStatus.ITEM_DONATED;

@RestController
public class TransactionController {

    private final TransactionServices transactionService;
    private final TransactionDetailServices transactionDetailServices;
    private final ItemServices itemServices;

    @Autowired
    public TransactionController(TransactionServices transactionService,
                                 TransactionDetailServices transactionDetailServices,
                                 ItemServices itemServices) {
        this.transactionService = transactionService;
        this.itemServices = itemServices;
        this.transactionDetailServices = transactionDetailServices;
    }

    @GetMapping("/transaction")
    public ResponseEntity getTransactionByUserId(ServletRequest servletRequest) {
        List<TransactionEntity> transactionEntities;
        try {
            int receiverId = getLoginUserId(servletRequest);
            transactionEntities = transactionService.getTopTransactionByUserId(receiverId);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionEntities, HttpStatus.OK);
    }

    @GetMapping("/transaction/history/count")
    public ResponseEntity getCountAllTransactionsByUserID(ServletRequest servletRequest) {
        int i = 0;
        try {
            int userID = getLoginUserId(servletRequest);
            i = transactionService.getCountAllTransactionsByUserID(userID);

        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity(i, HttpStatus.OK);
    }

    @GetMapping("/transaction/history")
    public ResponseEntity getAllTransactionsByUserID(ServletRequest servletRequest) {
        List<TransactionEntity> transactionEntities;
        try {
            int userID = getLoginUserId(servletRequest);
            transactionEntities = transactionService.getAllTransactionByUserID(userID);

        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity(transactionEntities, HttpStatus.OK);

    }

    @GetMapping("/transaction/confirm")
    public ResponseEntity getTransactionBySenderId(ServletRequest servletRequest) {
        List<TransactionEntity> transactionEntities;
        try {
            int senderId = getLoginUserId(servletRequest);
            transactionEntities = transactionService.getTopTransactionBySenderId(senderId);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionEntities, HttpStatus.OK);
    }

    @GetMapping("/transaction/{id:[\\d]+}")
    public ResponseEntity getTransactionById(@PathVariable("id") int id, ServletRequest servletRequest) {
        TransactionRequestWrapper transactionRequestWrapper = new TransactionRequestWrapper();
        try {
            int receiverId = getLoginUserId(servletRequest);
            TransactionEntity transactionEntity = transactionService.getTransactionByTransactionId(id);
            if (receiverId != transactionEntity.getReceiverId() && receiverId != transactionEntity.getSenderId()) {
                return new ResponseEntity(new ExffMessage("You are not owner of this transaction"), HttpStatus.BAD_REQUEST);
            }
            List<TransactionDetailEntity> details = transactionDetailServices.getTransactionDetailsByTransactionId(id);
            transactionRequestWrapper.setTransaction(transactionEntity);
            transactionRequestWrapper.setDetails(details);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionRequestWrapper, HttpStatus.OK);
    }

    @PutMapping("/transaction/{id:[\\d]+}")
    public ResponseEntity confirmTransaction(@PathVariable("id") int id, ServletRequest servletRequest) {
        List<TransactionDetailEntity> transactionDetailEntities;
        try {
            int userId = getLoginUserId(servletRequest);
            if (!transactionService.isValidTransaction(userId, id)) {
                return new ResponseEntity(new ExffMessage("Invalid user"), HttpStatus.BAD_REQUEST);
            }
            TransactionEntity transactionEntity = transactionService.getTransactionByTransactionId(id);
            transactionDetailEntities = transactionDetailServices.
                    getTransactionDetailsByTransactionId(id);
            List<Integer> itemIdList = transactionDetailEntities.stream()
                    .map(t -> Integer.valueOf(t.getId()))
                    .collect(Collectors.toList());
            transactionDetailServices.confirmTransactionDetail(itemIdList);
            transactionService.confirmTransaction(transactionEntity.getId());
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ExffMessage("Transaction confirmed"), HttpStatus.OK);
    }

    @PostMapping("/transaction")
    @Transactional
    public ResponseEntity createTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        int transId;
        TransactionDetails transactionDetails = new TransactionDetails();
        //int senderId = getLoginUserId(servletRequest);
        int senderId = requestWrapper.getTransaction().getSenderId();
        transactionDetails.setTransactionDetails(requestWrapper.getDetails());
        List<ItemEntity> unavailableItems = verifyItemsAvailabity(transactionDetails.getItemIds());
        List<ItemEntity> userOwnedItems = checkUserOwnedItem(senderId, transactionDetails.getItemIds());

        if (!unavailableItems.isEmpty()) {
            return new ResponseEntity(new ExffMessage("There are unavailable items: " + unavailableItems), HttpStatus.OK);
        }

        TransactionEntity transaction;
        transaction = requestWrapper.getTransaction();
        try {
            if (transaction.getDonationPostId() != null) {
                if (userOwnedItems.size() < transactionDetails.getItemIds().size()) {
                    return new ResponseEntity(new ExffMessage("Cannot donate item"), HttpStatus.BAD_REQUEST);
                }
            }
            int transactionId =
                    transactionService.createTransaction(senderId, transaction);
            transactionDetails.setTransactionId(transactionId);
            transactionDetails.getTransactionDetails().stream()
                    .forEach(t -> {
                        transactionDetailServices.createDetailTrans(transactionId, t.getItemId(), t.getUserId());
//                        itemServices.setItemUnavailable(t.getItemId());
                    });
            if (transaction.getDonationPostId() != null) {
                itemServices.changeItemsStatus(ITEM_DONATED, transactionDetails.getItemIds());
            }
            transId = transactionId;
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("" + transId), HttpStatus.OK);
    }

    @PutMapping("/transaction")
    public ResponseEntity updateTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        try {
            //int loginUserId = getLoginUserId(servletRequest);
            int loginUserId = requestWrapper.getTransaction().getSenderId();
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setTransactionDetails(requestWrapper.getDetails());
//            List<TransactionDetailEntity> transactionDetails = requestWrapper.getDetails();
            List<ItemEntity> unavailableItems = verifyItemsAvailabity(transactionDetails.getItemIds());
            if (!unavailableItems.isEmpty()) {
                return new ResponseEntity(new ExffMessage("There are unavailable items: " + unavailableItems), HttpStatus.OK);
            }
            TransactionEntity transaction = requestWrapper.getTransaction();
            if (loginUserId == transaction.getReceiverId()) {
                transaction.setStatus(ExffStatus.TRANSACTION_RESEND);
            } else {
                transaction.setStatus(ExffStatus.TRANSACTION_SEND);
            }
            transaction.setModifyTime(new Timestamp(System.currentTimeMillis()));
            transactionService.updateTransaction(transaction);

            transactionDetails.getTransactionDetails().stream()
                    .forEach((transactionDetail) -> {
                        if (transactionDetail.getTransactionId() == null)
                            transactionDetailServices.deleteTransactionDetail(transactionDetail);
                        else {
                            transactionDetail.setTransactionId(transaction.getId());
                            transactionDetailServices.updateTransactionDetail(transactionDetail);
                        }
                    });
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Updated and resend"), HttpStatus.OK);
    }
    @PutMapping("/transaction/uploadReceipt")
    public ResponseEntity uploadReceiptTransaction(@RequestBody Map<String, Object> body,
                                                    ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            Integer transactionId = (Integer) body.get("id");
            String imageUrl = (String) body.get("url");
            TransactionEntity transactionEntity = transactionService.uploadReceiptImage(transactionId,loginUserId, imageUrl);
            if (transactionEntity == null) {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(transactionEntity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
    @PutMapping("/transaction/confirmReceipt")
    public ResponseEntity confirmReceiptTransaction(@RequestBody Map<String, Object> body,
                                            ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            Integer transactionId = (Integer) body.get("id");
            TransactionEntity transactionEntity = transactionService.confirmReceiptImage(transactionId,loginUserId);
            if (transactionEntity == null) {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(transactionEntity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
    @DeleteMapping("/transaction")
    public ResponseEntity cancelTransaction(@RequestBody TransactionRequestWrapper requestWrapper,
                                            ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            TransactionEntity transaction = requestWrapper.getTransaction();
            transaction = transactionService.getTransactionByTransactionId(transaction.getId());
            if (loginUserId == transaction.getReceiverId()) {
                transactionDetailServices.deleteTransactionDetailByTransactionId(transaction.getId());
                transactionService.deleteTransaction(transaction);
            } else {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Deleted"), HttpStatus.OK);
    }

    @DeleteMapping("/transaction/{id:[\\d]+}")
    public ResponseEntity cancelTransactionByID(@PathVariable("id") int id,
                                                ServletRequest servletRequest) {
        try {
            int loginUserId = getLoginUserId(servletRequest);
            TransactionEntity transaction = transactionService.getTransactionByTransactionId(id);
            if (loginUserId == transaction.getReceiverId() || loginUserId == transaction.getSenderId()) {
                transactionDetailServices.deleteTransactionDetailByTransactionId(transaction.getId());
                transactionService.deleteTransaction(transaction);
            } else {
                return new ResponseEntity(new ExffMessage("Not permission"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new ExffMessage("Deleted"), HttpStatus.OK);
    }

    private List<ItemEntity> verifyItemsAvailabity(List<Integer> itemIds) {
        List<ItemEntity> result = itemServices.verifyItems(ExffStatus.ITEM_DISABLE, itemIds);
        return result;
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }

    private List<ItemEntity> checkUserOwnedItem(int userId, List<Integer> itemIds) {
        List<ItemEntity> result = itemServices.checkUserOwnedItems(userId, itemIds);
        return result;
    }

    @GetMapping("/donators/{donationPostId:[\\d]+}")
    public ResponseEntity getTransactionByDonationPostId(@PathVariable("donationPostId") int donationPostId, ServletRequest servletRequest) {
        List<TransactionRequestWrapper> transactionRequestWrapperList = new ArrayList<>();
        try {
            List<TransactionEntity> transactionList = transactionService.getTransactionByDonationPostId(donationPostId);
            if (!transactionList.isEmpty()) {
                for (int i = 0; i < transactionList.size(); i++) {
                    List<TransactionDetailEntity> details = transactionDetailServices.getTransactionDetailsByTransactionId(transactionList.get(i).getId());
                    TransactionRequestWrapper transactionRequestWrapper = new TransactionRequestWrapper();
                    transactionRequestWrapper.setDetails(details);
                    transactionRequestWrapper.setTransaction(transactionList.get(i));
                    transactionRequestWrapperList.add(transactionRequestWrapper);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionRequestWrapperList, HttpStatus.OK);
    }


    @GetMapping("/transaction/donation")
    public ResponseEntity getDonationTransactionByReceiverAgentId(ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);
        List<TransactionRequestWrapper> transactionRequestWrapperList = new ArrayList<>();

        try {
            List<TransactionEntity> transactionList = transactionService.getDonationTransactionByUserId(userId);
            if (!transactionList.isEmpty()) {
                for (int i = 0; i < transactionList.size(); i++) {
                    List<TransactionDetailEntity> details = transactionDetailServices.getTransactionDetailsByTransactionId(transactionList.get(i).getId());
                    TransactionRequestWrapper transactionRequestWrapper = new TransactionRequestWrapper();
                    transactionRequestWrapper.setDetails(details);
                    transactionRequestWrapper.setTransaction(transactionList.get(i));
                    transactionRequestWrapperList.add(transactionRequestWrapper);
                }
            } else {
                return new ResponseEntity(new ExffMessage("No Donation"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(transactionRequestWrapperList, HttpStatus.OK);
    }

}
