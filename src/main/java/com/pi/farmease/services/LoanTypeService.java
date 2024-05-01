package com.pi.farmease.services;

import com.pi.farmease.dao.LoanTypeRepository;
import com.pi.farmease.entities.Loan_Type;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    public Loan_Type addLoanType(Loan_Type loanType, MultipartFile image) throws IOException {
        // Vérifiez si une image est fournie
        if (image != null && !image.isEmpty()) {
            // Convertissez l'image en tableau de bytes
            byte[] imageB = IOUtils.toByteArray(image.getInputStream());
            // Associez les données de l'image au type de prêt
            loanType.setImage(imageB);
        }

        // Sauvegardez le type de prêt dans la base de données
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
