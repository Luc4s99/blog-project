package com.br.blog.repository;

import com.br.blog.dto.response.PostDto;
import com.br.blog.entity.Post;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {

    /*
    * No caso abaixo é feita uma agregação, para buscar os dados dos autores de cada post
    *
    * $lookup: realiza uma espécie de join com a coleção 'users' e busca o dado relacionado
    *
    * Para que isso funcione, tanto o campo 'author' e o campo '_id' devem ser do tipo 'ObjectId'
    * Caso não seja, uma conversão teria de ser feita, o que não foi o caso abaixo
    *
    * $unwind: A agregação lookup sempre retorna um array, mesmo que ela identifique apenas um documento no join
    * Então, o $unwind transforma o array em um documento, possibilitando que o Java o converta em um objeto, nesse caso
    * um objeto User, que representa o autor
    *
    * */
    @Aggregation(pipeline = {
            "{ $lookup: { from: \"users\", localField: \"author\", foreignField: \"_id\", as: \"author\" } }",
            "{ $unwind: \"$author\" }"
    })
    List<PostDto> getAllPostsWithAuthor();

    /*
    * Nesse caso, foi adicionada apenas a agregação $match, que filtra os resultados. Nesse caso foi feito o filtro pelo
    * identificador do post
    * */
    @Aggregation(pipeline = {
            "{ $lookup: { from: \"users\", localField: \"author\", foreignField: \"_id\", as: \"author\" } }",
            "{ $unwind: \"$author\" }",
            "{ $match: { _id: ObjectId(?0) } }"
    })
    Optional<PostDto> getPostById(String id);

    @Query("{'comments': {$eq: []}}")
    List<Post> getPostsWithNoComments();

    @Query("{'comments': {$ne: []}}")
    List<Post> getPostsWithComments();

    @Query("{$and: [ {'createdAt': {$gt: ?0}}, {'createdAt': {$lt: ?1}} ]}")
    List<Post> getPostsByDate(Date firstDate, Date lastDate);

    @Query("{'author': ?0}}")
    List<Post> getPostsByAuthor(String author);

    @Query("{'title': {$regex: /?0/i}}")
    List<Post> getPostsByTitle(String title);
}
