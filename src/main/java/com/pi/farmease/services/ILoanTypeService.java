package com.pi.farmease.services;


import com.pi.farmease.entities.Loan_Type;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ILoanTypeService {
    List<Loan_Type> getLoanType();
     Optional<Loan_Type> getLoanTypeById(long loanType_id);
     boolean existById(Long loanType_id);


    public Loan_Type addLoanType(Loan_Type loanType, MultipartFile image) throws IOException;
    void updateLoanType(Loan_Type loanType, Long loanType_id);

    void DeleteLoanType(Long loanType_id);

}
