package com.pi.farmease.services;

import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.User;
import com.pi.farmease.entities.Loan_Type ;
import com.pi.farmease.dao.LoanTypeRepository ;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoanTypeService implements ILoanTypeService {
    private LoanTypeRepository LoanTypeRepository ;
    private final UserService userService ;


    
    public List<Loan_Type> getLoanType()
    {
        return LoanTypeRepository.findAll() ;
    }

    @Override
    public Optional<Loan_Type> getLoanTypeById(long loanType_id) {
        return LoanTypeRepository.findById(loanType_id);
    }

    @Override
    public boolean existById(Long loanType_id) {
        return LoanTypeRepository.existsById(loanType_id);
    }


    public Loan_Type addLoanType(Loan_Type loanType) {

       return LoanTypeRepository.save(loanType);
    }

    @Override
    public void updateLoanType(Loan_Type loanType, Long loanType_id) {
        LoanTypeRepository.save(loanType);
    }

    @Override
    public void DeleteLoanType(Long loanType_id) {
        LoanTypeRepository.deleteById(loanType_id);
    }


}
