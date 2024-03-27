package com.pi.farmease.services;

import com.pi.farmease.dao.MortgageRepository;
import com.pi.farmease.entities.Mortgage;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MortgageServiceImpl implements MortgageService {

    @Autowired
    private final UserService userService ;
    private final  MortgageRepository mortgageRepository;

    @Override
    public Mortgage addMortgage(Mortgage requestBody) {


        // Construire l'objet Mortgage
        Mortgage mortgage = Mortgage.builder()
                .description_mortgage(requestBody.getDescription_mortgage())
                .prize_mortgage(requestBody.getPrize_mortgage())
                .category_mortgage(requestBody.getCategory_mortgage())
                .type_mortgage(requestBody.getType_mortgage())
                .price_mortgage(requestBody.getPrice_mortgage())
                .applications(requestBody.getApplications())  // Si vous souhaitez conserver les applications
                .build();
        // Enregistrer le mortgage dans la base de données
        return mortgageRepository.save(mortgage);
    }

    @Override
    public List<Mortgage> getAllMortgages() {
        return mortgageRepository.findAll();
    }

    @Override
    public Optional<Mortgage> getMortgageById(Long id) {
        return mortgageRepository.findById(id);
    }

    @Override
    public Mortgage updateMortgage(Long id, Mortgage mortgageDetails) {
        Optional<Mortgage> mortgageOptional = mortgageRepository.findById(id);
        if (mortgageOptional.isPresent()) {
            Mortgage existingMortgage = mortgageOptional.get();
            existingMortgage.setDescription_mortgage(mortgageDetails.getDescription_mortgage());
            existingMortgage.setPrize_mortgage(mortgageDetails.getPrize_mortgage());
            existingMortgage.setCategory_mortgage(mortgageDetails.getCategory_mortgage());
            existingMortgage.setType_mortgage(mortgageDetails.getType_mortgage());
            existingMortgage.setPrice_mortgage(mortgageDetails.getPrice_mortgage());
            return mortgageRepository.save(existingMortgage);
        } else {
            return null; // Gérer le cas où l'identifiant n'est pas trouvé
        }
    }

    @Override
    public void deleteMortgage(Long id) {
        mortgageRepository.deleteById(id);
    }
}
