package com.br.blog.repository;

import com.br.blog.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    //Nesse caso, o spring consegue realizar a busca desses dados pois eles estão com a assinatura padrão
    //findBy[Nome do atributo]
    //Então não é necessário escrever a query manualmente

    RefreshToken findByToken(String refreshToken);

    RefreshToken findByUser(String user);

    void deleteByToken(String token);
}
