package com.pi.farmease.services;


import com.pi.farmease.entities.Loan_Type ;
import java.util.List;
import java.util.Optional;

public interface ILoanTypeService {
    List<Loan_Type> getLoanType();
     Optional<Loan_Type> getLoanTypeById(long loanType_id);
     boolean existById(Long loanType_id);


    Loan_Type addLoanType(Loan_Type loanType_id);
    void updateLoanType(Loan_Type loanType, Long loanType_id);

    void DeleteLoanType(Long loanType_id);

}
