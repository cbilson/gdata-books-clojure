(ns gdata-books-clojure.core
  (:use [clojure.contrib.string :only [join split replace-re]])
  (:import (com.google.gdata.client.books VolumeQuery BooksService)
           (com.google.gdata.data.books VolumeFeed)
           (java.net URL)))

(def settings (atom {:application-name "unknown"
                     :base-url "http://www.google.com/books/feeds/volumes"}))

(defn- url [s] (URL. s))

(defn- in-author [s]
  (str "inauthor:" s))

(defn- build-author-operators
  "Given an author name, like \"Joe O'Brien King\"
   returns a string like \"inauthor:Joe inauthor:O'Brien inauthor:King\"
   so we can use google book search advance operators to
   get books by a specific author."
  [author]
  (let [tokens (split #"\s+" author)
        name-tokens (map #(replace-re #"[^A-Za-z'-]" "" %) tokens)
        long-name-tokens (filter #(< 1 (.length %)) name-tokens)
        author-operators (map in-author long-name-tokens)]
    (if (empty? author-operators)
      nil
      (join " " author-operators))))

(defn- create-query
  "Creates a google books query for a title and optional author"
  [{title :title, author :author}]
  (let [query (VolumeQuery. (url (:base-url @settings)))
        author-operators (if author (build-author-operators author))
        query-text (if (empty? author-operators)
                     title
                     (join " " [title author-operators]))]
    (.setFullTextQuery query query-text)
    query))

(defn- do-query
  "Executes a google books query"
  [query]
  (let [service (BooksService. (:application-name @settings))]
    (.query service query VolumeFeed)))

(defn search
  "Searches for a book using google books. Can be called like:
   (seach \"Stranger in a strange land\")
   -or-
   (search {:title \"Stranger in a strange land\", :author \"Robert Heinlein\"})"
  [args]
  (let [criteria (if (map? args) args {:title args})
        query (create-query criteria)]
    (do-query query)))

(defn etag [x] (.getEtag x))
(defn href [x] (.getHref x))
(defn rel [x] (.getRel x))
(defn text [x] (.getText x))
(defn title [x] (.getTitle x))
(defn link-type [x] (.getType x))
(defn value [x] (.getValue x))
(defn values [xs] (map value xs))

(defn alternate-links [x] (.getAtomAlternateLinks x))

(defn comments [x] (.getComments x))
(defn comments? [x] (.hasComments x))

(defn creators? [x] (.hasCreators x))
(defn creators [x] (values (.getCreators x)))

(defn descriptions? [x] (.hasDescriptions x))
(defn descriptions [x] (values (.getDescriptions x)))

(defn entries [x] (.getEntries x))

(defn identifiers? [x] (.hasIdentifiers x))
(defn identifiers [x] (values (.getIdentifiers x)))

(defn languages? [x] (.hasLanguages x))
(defn languages [x] (values (.getLanguages x)))

(defn publishers? [x] (.hasPublishers x))
(defn publishers [x] (values (.getPublishers x)))

(defn rating? [x] (.hasRating x))
(defn rating [x] (value (.getRating x)))

(defn review? [x] (.hasReview x))
(defn review [x] (.getReview x))

(defn subjects? [x] (.hasSubjects x))
(defn subjects [x] (values (.getSubjects x)))

(defn titles? [x] (.hasTitles x))
(defn titles [x] (values (.getTitles x)))

(defn thumbnail [x] (href (.getThumbnailLink x)))
