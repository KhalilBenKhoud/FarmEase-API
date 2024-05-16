package com.pi.farmease.services;

import com.pi.farmease.dao.PostRepository;
import com.pi.farmease.dao.SignalPostRepository;
import com.pi.farmease.dao.UserRepository;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.Signal_post;
import com.pi.farmease.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

     private final UserService userService ;
     private final PostRepository postRepository ;
     private final SignalPostRepository signalrepository;
     private final UserRepository userrepository;
    public void addPost(Post requestBody, Principal connected) {
        // Vérifier s'il y a des mots inappropriés dans le titre ou la description
        User connectedUser = userService.getCurrentUser(connected);
        if (containsBadWords(requestBody)) {
            banUser(connectedUser.getId());
            return;
        }

        // Le post ne contient pas de mots inappropriés, poursuivre avec l'ajout

        Post post = Post.builder()
                .user(connectedUser)
                .date_Post(new Date())
                .title_Post(requestBody.getTitle_Post())
                .description_Post(requestBody.getDescription_Post())
                .nbr_like_post(requestBody.getNbr_like_post())
                .nbr_signal_post(requestBody.getNbr_signal_post())
                .category_post(requestBody.getCategory_post())
                .sondage1(requestBody.getSondage1())
                .sondage2(requestBody.getSondage2())
                .stat1(requestBody.getStat1())
                .stat2(requestBody.getStat2())
                .build();

        postRepository.save(post);
    }


    @Override
    public void banUser(Integer id) {
        User concernedUser = userrepository.findById(id).orElse(null) ;

        if(concernedUser == null)  throw new RuntimeException("user doesn't exist") ;
        if(!concernedUser.isEnabled()) throw new RuntimeException("user already banned") ;
        concernedUser.setEnabled(false);
        userrepository.save(concernedUser) ;

    }
    public List<Post> getPost()
    {
        return postRepository.findAll() ;
    }


    @Override
    public void updatePost(Post post, Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        // Obtenez la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Convertissez la date du post en LocalDate
        Instant instant = post.getDate_Post().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate postDate = instant.atZone(zoneId).toLocalDate();

        // Vérifiez si la date actuelle correspond à la date du post ou à la date suivante
        if (currentDate.equals(postDate) || currentDate.equals(postDate.plusDays(1))) {
            // Mettre à jour les attributs avec les nouvelles valeurs
            existingPost.setTitle_Post(post.getTitle_Post());
            existingPost.setDescription_Post(post.getDescription_Post());
            existingPost.setDate_Post(post.getDate_Post());
            existingPost.setNbr_like_post(post.getNbr_like_post());
            existingPost.setNbr_signal_post(post.getNbr_signal_post());
            existingPost.setCategory_post(post.getCategory_post());
            existingPost.setSondage1(post.getSondage1());
            existingPost.setSondage2(post.getSondage2());
            existingPost.setStat1(post.getStat1());
            existingPost.setStat2(post.getStat2());

            // Enregistrer les modifications dans la base de données
            postRepository.save(existingPost);
        } else {
            // La date actuelle ne correspond pas à la date du post ou à la date suivante
            // Ne pas effectuer de mise à jour
            throw new IllegalArgumentException("Cannot update post. vous avez depasser 24h.");
        }
    }


    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    @Override
    public List<Post> getPostsSortedByLikes() {

        Sort sort = Sort.by(Sort.Direction.DESC, "nbr_like_post");
        System.out.println("2");
        return postRepository.findAll(sort);
    }

    @Override
    public boolean containsBadWords(Post post) {
        List<String> badWords = Arrays.asList("badword1", "badword2", "badword3"); // Ajoutez vos mots inappropriés ici

        // Vérification du titre
        String title = post.getTitle_Post();
        for (String word : badWords) {
            if (title.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }

        // Vérification de la description
        String description = post.getDescription_Post();
        for (String word : badWords) {
            if (description.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
    @Override
    public void calculateStatPercentage(Post post) {
        double stat1 = post.getStat1();
        double stat2 = post.getStat2();
        double total = stat1 + stat2;
        double stat1Percentage = (double) stat1 / total * 100;
        double stat2Percentage = (double) stat2 / total * 100;
    }
    @Override
    public void incrementLike(Post post) {
        // Incrémenter le nombre de likes
        long currentLikes = post.getNbr_like_post();
        post.setNbr_like_post(currentLikes + 1);

        // Enregistrer les modifications dans la base de données
        postRepository.save(post);
    }
    @Override
    public void incrementsignal(Long postId, Principal principal) {
        User user = userService.getCurrentUser(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        // Vérifier si l'utilisateur a déjà aimé ce post
        Signal_post existingSignalPostPost = signalrepository.findByPostIdAndUserId(post.getId_Post(), user.getId());
        if (existingSignalPostPost != null) {
            // Supprimer le like existant
            signalrepository.delete(existingSignalPostPost);

            // Mettre à jour le nombre de likes sur le post
            post.setNbr_signal_post(post.getNbr_signal_post() - 1);
        } else {
            // Créer un nouveau like
            Signal_post signalPost = Signal_post.builder()
                    .post_id_signal(post.getId_Post())
                    .user_id_signal(user.getId())
                    .build();
            signalrepository.save(signalPost);

            // Mettre à jour le nombre de likes sur le post
            post.setNbr_signal_post(post.getNbr_signal_post() + 1);
            if (post.getNbr_signal_post() >= 10) {
                banUser(user.getId());
            }
        }

        // Enregistrer les modifications apportées au post
        postRepository.save(post);
    }
    @Override
    public void incrementStat1(Post post) {
        // Incrémenter le nombre de likes
        double currentStat1 = post.getStat1();
        post.setStat1(currentStat1 + 1);

        // Enregistrer les modifications dans la base de données
        postRepository.save(post);
    }
    @Override
    public void incrementStat2(Post post) {
        // Incrémenter le nombre de likes
        double currentStat2 = post.getStat2();
        post.setStat2(currentStat2 + 1);

        // Enregistrer les modifications dans la base de données
        postRepository.save(post);
    }


}
