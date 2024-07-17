package org.antihetman.testtask;

import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {



    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            UUID uuid = UUID.randomUUID();
            document.setId(uuid.toString());
        }
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        List<Document> documents = new ArrayList<>();
        List<Document> requestedDocuments = new ArrayList<>();
        requestedDocuments = documents.stream()
                .filter(el -> {
                    if (request.createdTo == null)
                        return true;
                    else return el.created.isBefore(request.createdTo);
                })
                .filter(el -> {
                    if (request.createdFrom == null)
                        return true;
                    else return el.created.isAfter(request.createdFrom);
                })
                .filter(el -> {
                    if (request.titlePrefixes == null) {
                        return true;
                    }
                    List<String> titlePrefixes = request.titlePrefixes;
                    for (String prefix : titlePrefixes)
                        if (el.getTitle().startsWith(prefix)) {
                            return true;
                        }
                    return false;
                })
                .filter(el -> {
                    if (request.authorIds == null) {
                        return true;
                    }
                    List<String> authorIds = request.authorIds;
                    for (String id : authorIds)
                        if (el.getAuthor().id.equals(id)){
                            return true;
                        }
                    return false;
                })
                .filter(el -> {
                    if (request.containsContents == null) {
                        return true;
                    }
                    List<String> containsContents = request.containsContents;
                    for (String content : containsContents)
                        if (el.getContent().contains(content)){
                            return true;
                        }
                    return false;
                })
                .toList();
        return requestedDocuments;
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        List<Document> documents = new ArrayList<>();
        return documents.stream().filter(el -> el.getId().equals(id)).findFirst();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}